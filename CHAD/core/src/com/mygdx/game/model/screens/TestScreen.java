package com.mygdx.game.model.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.CardGame;

public class TestScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;

    public TestScreen(CardGame game){
        this.game = game;
    }

    @Override
    public void update(float dt) {
        System.out.print("Test");
        handleInput();
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 1, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isTouched()) {
            game.setScreen(new MenuScreen(game));
            //game.getScreen().dispose();
        }
    }

    @Override
    public void render (float dt) {
        draw();
        update(dt);
    }
}
