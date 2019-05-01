package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.CardGame;
import com.mygdx.game.World;
import com.mygdx.game.model.screens.utils.Assets;
import com.mygdx.game.model.systems.BoardSystem;
import com.mygdx.game.model.systems.CardSystem;
import com.mygdx.game.model.systems.PlayerSystem;
import com.mygdx.game.view.BoardView;

import java.util.List;

public class GameScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private World world;
    private Engine engine;
    private BoardView bv;
    private List<Entity> players;
    private Stage gameStage;

  
    protected GameScreen(CardGame game, Engine engine) {
        this.game = game;

        this.engine = engine;
        this.world = new World(engine);

        create();


    }

    @Override
    public void create() {
        gameStage = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(gameStage); // Set inputs to be handled by the stage

        players = world.createPlayers();
        Entity boardEntity = world.createBoard();

        engine.addSystem(new PlayerSystem());
        engine.addSystem(new CardSystem());
        engine.addSystem(new BoardSystem());
        engine.getSystem(BoardSystem.class).addPlayer(boardEntity, players);

        for (int i = 0; i < 5; i++) {
            engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(0));
            //engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(1));
        }

        bv = new BoardView(boardEntity);

        final Button quitBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.quitBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.quitBtn))));
        quitBtn.setTransform(true);
        quitBtn.setSize(quitBtn.getWidth()/2, quitBtn.getHeight()/2);
        quitBtn.setOrigin(quitBtn.getWidth()/2, quitBtn.getHeight()/2);

        quitBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ConfirmationScreen(game, engine, "Are you sure you want to end this game?"));
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                quitBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                quitBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        Table menuTable = new Table(); // Table containing the buttons on the screen
        menuTable.add(quitBtn);
        menuTable.getCell(quitBtn).height(quitBtn.getHeight()).width(quitBtn.getWidth());
        menuTable.setFillParent(true);
        menuTable.moveBy((-Gdx.graphics.getWidth()/2 + 150),(Gdx.graphics.getHeight()/2 - 60));

        gameStage.addActor(menuTable); // Add the table containing the buttons to the stage

    }

    @Override
    public void update(float dt) {
        handleInput();
        engine.update(dt);
        gameStage.act(Gdx.graphics.getDeltaTime());

    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Winning: player 1
        if (engine.getSystem(PlayerSystem.class).getHealth(players.get(0)) == 0 && engine.getSystem(PlayerSystem.class).getHealth(players.get(1)) != 0) {
            game.setScreen(new GameOverScreen(game, engine, players.get(1), players.get(0)));
        }

        // Winning: player 2
        else if(engine.getSystem(PlayerSystem.class).getHealth(players.get(1)) == 0 && engine.getSystem(PlayerSystem.class).getHealth(players.get(0)) != 0) {
            game.setScreen(new GameOverScreen(game, engine, players.get(0), players.get(1)));
        }

        else {
            bv.draw(game.batch);
        }
//        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        gameStage.draw();

    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    @Override
    public void handleInput() {
    }
}
