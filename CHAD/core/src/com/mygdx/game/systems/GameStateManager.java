package com.mygdx.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.components.StateComponent;

import java.util.Stack;

public class GameStateManager {
    private Stack<StateComponent> states;

    public GameStateManager() {
        states = new Stack<StateComponent>();
    }

    public void push(StateComponent state) {
        states.push(state);
    }

    public void set(StateComponent state) {
        states.pop();
        states.push(state);
    }

    public void update(float dt) {
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }
}
