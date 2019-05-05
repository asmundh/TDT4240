package com.mygdx.game.view;


public interface ScreenInterface {

    public void create(); // Used for one-time startup operations

    public void update(float dt); // Used to update data that is going to be rendered

    public void draw(); // Used to actually draw the elements to screen

    public void handleInput(); // Should contain methods to handle input from used


    //OBS: render and dispose must be added manually (via "Generate" in IntelliJ)
}
