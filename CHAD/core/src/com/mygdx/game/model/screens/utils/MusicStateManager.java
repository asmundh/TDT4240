package com.mygdx.game.model.screens.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.CardGame;

public class MusicStateManager {
    private Preferences prefs;
    private CardGame game;

    private static final String PREF_NAME = "CHAD";
    private static final String MUSIC_STATE = "mstate";

    public MusicStateManager(CardGame game) {
        this.game = game;
        prefs = Gdx.app.getPreferences(PREF_NAME);
        game.musicBool = prefs.getBoolean(MUSIC_STATE, true);
    }

    public void saveState(boolean musicState) {
        game.musicBool = musicState;
        prefs.putBoolean(MUSIC_STATE, musicState);
        prefs.flush();

        if(game.musicBool)
            game.getBgMusic().play();
        else
            game.getBgMusic().stop();
    }

    public boolean getMusicState() {
        return prefs.getBoolean(MUSIC_STATE);
    }
}
