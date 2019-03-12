package com.mygdx.game.model.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.components.StateComponent;
import com.mygdx.game.model.systems.GameStateManager;

public class MenuScreen extends StateComponent {
    private Texture playBtn;

    public MenuScreen(GameStateManager gsm) {
        super(gsm);
         playBtn = new Texture("badlogic.jpg");
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new GameScreen(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(playBtn, (Gdx.graphics.getWidth()/2) - (playBtn.getWidth() / 2),
                (Gdx.graphics.getHeight()/2));
        sb.end();
    }

    @Override
    public void dispose() {
        playBtn.dispose();
    }
}
