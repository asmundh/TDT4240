package com.mygdx.game.model.screens.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class assets {

    public static String badlogic = "badlogic.jpg";

    private static AssetManager assetManager;

    public assets() {
        assetManager = new AssetManager();
    }

    public static void dispose(){
        assetManager.dispose();
    }

    public static void load() {
        assetManager.load(badlogic, Texture.class);
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
