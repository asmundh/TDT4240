package com.mygdx.game.model.screens;

public interface ScreenInterface {

    public void update(float dt);

    public void draw();

    public void handleInput();

    public void render(float dt);
}
