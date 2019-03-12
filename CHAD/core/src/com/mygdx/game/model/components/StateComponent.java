package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.systems.GameStateManager;

public abstract class StateComponent implements Component {
    protected GameStateManager gsm;

    protected StateComponent(GameStateManager gsm) {
        this.gsm = gsm;
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}


