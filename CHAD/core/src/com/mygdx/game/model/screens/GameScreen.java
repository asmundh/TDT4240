package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.CardGame;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.systems.BoardSystem;
import com.mygdx.game.model.systems.CardSystem;
import com.mygdx.game.model.systems.PlayerSystem;
import com.mygdx.game.view.BoardView;
import com.mygdx.game.view.CardView;
import com.mygdx.game.World;

import java.util.List;

public class GameScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private World world;
    private Engine engine;
    private BoardView bv;

  
    protected GameScreen(CardGame game, Engine engine) {
        this.game = game;

        this.engine = engine;
        this.world = new World(engine);

        create();


    }

    @Override
    public void create() {
        List<Entity> players = world.createPlayers();
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



    }

    @Override
    public void update(float dt) {
        handleInput();
        engine.update(dt);

    }

    @Override
    public void draw() {


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bv.draw(game.batch);
//        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isTouched()){
            // game.setScreen(new MenuScreen(game, engine));

        }

    }
}
