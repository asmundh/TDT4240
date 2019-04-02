package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.CardGame;

public class GameOverScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private Engine engine;
    private SpriteBatch sb;
    private Stage stage;
    private int winningPlayer;

    public GameOverScreen(CardGame game, Engine engine, int winningPlayer) {
        super();
        this.winningPlayer = winningPlayer;
        this.game = game;
        sb = game.batch;
        create();
        this.engine = engine;
    }

    @Override
    public void create() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.35f, 0.34f, 0.32f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        System.out.println("Player " + this.winningPlayer + " is winning");
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
