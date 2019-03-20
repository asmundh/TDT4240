package com.mygdx.game.model.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.screens.utils.Assets;

public class MenuScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;

    // Sprites for buttons and background
    private Sprite backgroud;
    private Sprite playBtn;
    private Sprite settingBtn;
    private SpriteBatch sb;

    public MenuScreen(CardGame game){ // Constructor initializes sprites and sets the sprite batch for the class
        super();
        this.game = game;
        sb = game.batch;
        backgroud = new Sprite(Assets.getTexture(Assets.background));
        playBtn = new Sprite(Assets.getTexture(Assets.playBtn));
        settingBtn = new Sprite(Assets.getTexture(Assets.settingBtn));

        this.create(); // Run create on one-time operations
    }

    public void create() {
        playBtn.setSize(480,160);   // set size of play button
        settingBtn.setSize(480,160);   // set size of setting button
        playBtn.setPosition(CardGame.WIDTH/2 -  playBtn.getWidth()/2,CardGame.HEIGHT/3);   //set position of play button
        settingBtn.setPosition(CardGame.WIDTH/2 - settingBtn.getWidth()/2,CardGame.HEIGHT/6);   //set position of setting button
    }

    @Override
    public void update(float dt) { // Only thing we're checking for is if user presses button
        handleInput();
    }

    @Override
    public void draw() { // Draws sprite batch
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(backgroud, 0,0, CardGame.WIDTH, CardGame.HEIGHT);
        playBtn.draw(sb);
        settingBtn.draw(sb);
        sb.end();
    }

    @Override
    public void handleInput() { // Checks if one of the buttons are pressed
        if (Gdx.input.isTouched()) {
            // Checks for clicks using Rectangle's .contains()-method
            if (playBtn.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
                // Play button pressed
                game.setScreen(new GameScreen(game));
                this.dispose();
            }
            if (settingBtn.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
                // Setting button pressed
                // TODO se setScreen on appropriate screen
            }
        }
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
}
