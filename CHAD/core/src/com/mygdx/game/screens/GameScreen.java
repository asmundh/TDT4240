package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.systems.GameStateManager;

public class GameScreen extends StateComponent {
    protected GameScreen(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch sb) {
        // Logikken til Game screen går her:
        Gdx.gl.glClearColor(1, 1, 0, 1);
    }

    @Override
    public void dispose() {
    }
}
