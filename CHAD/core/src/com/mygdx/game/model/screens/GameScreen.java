package com.mygdx.game.model.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CardGame;
import com.mygdx.game.view.BoardView;
import com.mygdx.game.view.CardView;

public class GameScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;

    private BoardView bv;
    private SpriteBatch batch;
    private CardView cv1 = new CardView("goblin.png", 3, 5);



    protected GameScreen(CardGame game) {
        this.game = game;
        this.bv = new BoardView(game);
        this.batch = new SpriteBatch();

        this.bv.addCardToBoard(cv1);
        this.bv.addCardToBoard(cv1);
        this.bv.addCardToBoard(cv1);
        this.bv.addCardToBoard(cv1);
        this.bv.addCardToBoard(cv1);
        this.bv.addCardToBoard(cv1);
        this.bv.addCardToBoard(cv1);
        this.bv.addCardToBoard(cv1);


        this.bv.addCardToHand(cv1);
        this.bv.addCardToHand(cv1);
        this.bv.addCardToHand(cv1);
        this.bv.addCardToHand(cv1);
        this.bv.addCardToHand(cv1);

        this.bv.setShowHand(false);

    }

    @Override
    public void create() {}

    @Override
    public void update(float dt) {
        handleInput();
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
            game.setScreen(new MenuScreen(game));
        }

    }
}
