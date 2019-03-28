package com.mygdx.game.model.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.CardGame;

public class MenuScreen extends ScreenAdapter implements ScreenInterface {
    private Texture playBtn;
    private CardGame game;

    public MenuScreen(CardGame game) {
        playBtn = new Texture("badlogic.jpg");
        this.game = game;

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(playBtn, 0, 0, 320, 480);
        game.batch.end();
    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();

    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
        }

    }
}
