package com.mygdx.game.model.screens.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.CardGame;

public class MusicStateManager {
    private Preferences prefs;
    private CardGame game;

    private boolean isPlaying;

    public MusicStateManager(CardGame game) {
        this.isPlaying = true;
        this.game = game;
    }

    public void changeState() {
        this.isPlaying = !isPlaying;
        if(isPlaying)
            game.getBgMusic().play();
        else
            game.getBgMusic().stop();
    }

    public boolean getMusicState() {
        return isPlaying;
    }
}
