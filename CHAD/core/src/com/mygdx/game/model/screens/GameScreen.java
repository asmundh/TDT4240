package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.CardGame;
import com.mygdx.game.view.BoardView;
import com.mygdx.game.view.CardView;
import com.mygdx.game.World;

public class GameScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private World world;
    private Engine engine;

  
    protected GameScreen(CardGame game, Engine engine) {
        this.game = game;

        this.engine = engine;
        this.world = new World(engine);

        world.createBoard();
        world.createCard();
        world.createPlayer();
    }

    @Override
    public void create() {}

    @Override
    public void update(float dt) {
        handleInput();
        engine.update(dt);

    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bv.draw(batch);

    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isTouched()){
            game.setScreen(new MenuScreen(game, engine));
        }

    }
}
