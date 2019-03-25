package com.mygdx.game.model.screens.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {

    public static String badlogic = "badlogic.jpg";
    public static String playBtn = "textures/playBtn.png";
    public static String settingBtn = "textures/settingBtn.png";
    public static String background = "textures/background.png";
    public static String menuBG = "textures/menu_bg.png";
    public static String backBtn = "textures/back.png";
    public static String exitBtn = "textures/exit.png";
    public static String exit_gameBtn = "textures/exit_game.png";

    private static AssetManager assetManager;

    public Assets() {
        assetManager = new AssetManager();
    }

    public static void dispose(){
        assetManager.dispose();
    }

    public static void load() {
        assetManager.load(badlogic, Texture.class);
        assetManager.load(playBtn, Texture.class);
        assetManager.load(settingBtn, Texture.class);
        assetManager.load(background, Texture.class);
        assetManager.load(menuBG, Texture.class);
        assetManager.load(backBtn, Texture.class);
        assetManager.load(exitBtn, Texture.class);
        assetManager.load(exit_gameBtn, Texture.class);
    }

    public static float getProgress() {
        return assetManager.getProgress();
    }

    public static boolean update() {
        return assetManager.update();
    }

    public static Texture getTexture(String tex) {
        return assetManager.get(tex, Texture.class);
    }

    public static TextureAtlas getTextureAtlas(String tex){
        return assetManager.get(tex, TextureAtlas.class);
    }

}
