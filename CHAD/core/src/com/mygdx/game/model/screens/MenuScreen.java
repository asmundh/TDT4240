package com.mygdx.game.model.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CardGame;

public class MenuScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private Sprite backgroud;
    private Sprite playBtn;
    private Sprite settingBtn;
    private SpriteBatch sb;

    public MenuScreen(CardGame game){
        super();
        this.game = game;
        sb = game.batch;
        backgroud = new Sprite(new Texture("textures/background.png"));
        playBtn = new Sprite(new Texture("textures/PlayBtn.png"));
        settingBtn = new Sprite(new Texture("textures/shitBtn.png"));

        this.create();
    }

    public void create() {
        playBtn.setSize(480,160);   // set size of play button
        settingBtn.setSize(480,160);   // set size of setting button
        playBtn.setPosition(CardGame.WIDTH/4,CardGame.HEIGHT/3);   //set position of play button
        settingBtn.setPosition(CardGame.WIDTH/4,CardGame.HEIGHT/6);   //set position of setting button
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(backgroud, 0,0, CardGame.WIDTH, CardGame.HEIGHT);
        playBtn.draw(sb);
        settingBtn.draw(sb);
        sb.end();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched()) {
            if (playBtn.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
                // Play button pressed
                game.setScreen(new GameScreen(game));
            }
            if (settingBtn.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
                // Setting button pressed
                game.dispose(); // TODO remove dispose and use setScreen
            }
        }
    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }
}
