package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.CardGame;
import com.mygdx.game.World;
import com.mygdx.game.model.systems.RenderingSystem;

public class TestScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private World world;
    private Engine engine;


    protected TestScreen(CardGame game, Engine engine) {
        this.game = game;
        this.engine = engine;
        this.world = new World(engine);

        world.createTest();
        engine.addSystem(new RenderingSystem());
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
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 1, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    @Override
    public void handleInput() {
//        if(Gdx.input.isTouched()){
//            game.setScreen(new MenuScreen(game, engine));
//        }
    }
}
