package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.mygdx.game.model.screens.utils.Assets;
import com.mygdx.game.model.systems.PlayerSystem;

public class GameOverScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private Engine engine;
    private SpriteBatch sb;
    private Stage endGame;
    private Entity winningPlayer;
    private Entity losingPlayer;
    private BitmapFont font;

    public GameOverScreen(CardGame game, Engine engine, Entity winningPlayer, Entity losingPlayer) {
        super();
        this.winningPlayer = winningPlayer;
        this.losingPlayer = losingPlayer;
        this.game = game;
        sb = game.batch;
        create();
        this.engine = engine;
    }

    @Override
    public void create() {
        endGame = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(endGame); // Set inputs to be handled by the stage
        font = new BitmapFont();
        font.getData().setScale(5, 5);

        // Not in use at this point:
        /*final Button ReplayBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.replayBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.replayBtn))));
        ReplayBtn.setTransform(true);
        ReplayBtn.setSize(ReplayBtn.getWidth(), ReplayBtn.getHeight());
        ReplayBtn.setOrigin(ReplayBtn.getWidth()/2, ReplayBtn.getHeight()/2);*/

        // Initialize a  button using texture from Assets, first is up-texture, second is down. Set the size, make is transformable and set the origin to the middle
        final Button menuBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.menuBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.menuBtn))));
        menuBtn.setTransform(true);
        menuBtn.setSize(menuBtn.getWidth(), menuBtn.getHeight());
        menuBtn.setOrigin(menuBtn.getWidth()/2, menuBtn.getHeight()/2);

        // Initialize a  button using texture from Assets. Set the size, make is transformable and set the origin to the middle
        final Button exitBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.exit_gameBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.exit_gameBtn))));
        exitBtn.setTransform(true);
        exitBtn.setSize(exitBtn.getWidth(), exitBtn.getHeight());
        exitBtn.setOrigin(exitBtn.getWidth()/2, exitBtn.getHeight()/2);

        // Replay button not in use at this point:
        /*ReplayBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, engine));
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ReplayBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                ReplayBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });*/

        menuBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, engine));
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                menuBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                System.exit(0);
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                exitBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        Table menuTable = new Table(); // Table containing the buttons on the screen
        menuTable.add(menuBtn).pad(10);
        menuTable.getCell(menuBtn).height(menuBtn.getHeight()).width(menuBtn.getWidth());
        menuTable.row();
        // Replay not in use at this time:
        /*menuTable.add(ReplayBtn).pad(10);
        menuTable.getCell(ReplayBtn).height(ReplayBtn.getHeight()).width(ReplayBtn.getWidth());
        menuTable.row();*/
        menuTable.add(exitBtn);
        menuTable.getCell(exitBtn).height(exitBtn.getHeight()).width(exitBtn.getWidth());
        menuTable.setFillParent(true);
        menuTable.moveBy(0,-100);

        endGame.addActor(menuTable); // Add the table containing the buttons to the stage
    }

    @Override
    public void update(float dt) {
        endGame.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.35f, 0.34f, 0.32f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin(); // Draw elements to Sprite Batch
        /*sb.draw(Assets.getTexture(Assets.gameOverText),
                CardGame.WIDTH/2 - 350,
                CardGame.HEIGHT/2 + 350, Assets.getTexture(Assets.gameOverText).getWidth()/3,
                Assets.getTexture(Assets.gameOverText).getHeight()/3);*/
        font.draw(sb, "Congratulations " + engine.getSystem(PlayerSystem.class).getPlayerName(this.winningPlayer) + ", you won!", CardGame.WIDTH/2 - 400, CardGame.HEIGHT - 200);
        font.draw(sb, engine.getSystem(PlayerSystem.class).getPlayerName(this.losingPlayer) + " you lost, better luck next time!", CardGame.WIDTH/2 - 500, CardGame.HEIGHT - 300);
        sb.end();

        endGame.draw(); // Draw elements to Stage
    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void handleInput() {

    }
}
