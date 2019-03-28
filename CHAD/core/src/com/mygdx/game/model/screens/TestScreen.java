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

        // try to print player name

    }

    @Override
    public void handleInput() {
        if(Gdx.input.isTouched()) {


            //game.getScreen().dispose();
            game.setScreen(new MenuScreen(game));

            // test changeView()
            game.androidInterface.changeView();
            game.androidInterface.startQuickMatch();
            System.out.println("PlayerId printed from com.mygdx.game core testscreen: " + game.androidInterface.getPlayerId());
            System.out.println("Displayname printed from com.mygdx.game core testscreen: " + game.androidInterface.getDisplayName());
            System.out.println("Opponent displayname: " + game.androidInterface.getOpponentDisplayName());
        }
    }

    @Override
    public void render (float dt) {
        draw();
        update(dt);
    }
}
