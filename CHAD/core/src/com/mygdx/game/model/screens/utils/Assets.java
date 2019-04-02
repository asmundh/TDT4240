package com.mygdx.game.model.screens.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class Assets {

    public static String playBtn = "textures/playBtn.png";
    public static String settingBtn = "textures/settingBtn.png";
    public static String background = "textures/background.png";
    public static String menuBG = "textures/menu_bg.png";
    public static String backBtn = "textures/back.png";
    public static String exitBtn = "textures/exit.png";
    public static String exit_gameBtn = "textures/exit_game.png";

    public static String pathToAttackIcon = "textures/attackIcon.png";
    public static String pathToHealtchIcon = "textures/healthIcon.png";
    public static String pathToGreenRect = "textures/greenRect.png";
    public static String pathToBlackRect = "textures/blackRect.png";

    public static String deck = "textures/cardBackside.png";
    public static String boardBackground = "textures/background.png";
    public static String enemyRect = "textures/enemy.png";
    public static String handRect = "textures/handRect.png";

    public static String goblin = "textures/background.png";

    public static String orc = "textures/cards/goblin.png";

    public static String goblin_king = "textures/background.png";

    public static String deck1 = "decks/deck1.json";

    public static String deck2 = "decks/deck2.json";

    public static HashMap<String, String> cards = new HashMap<String, String>() {{
        put("goblin", "textures/cards/goblin.png");
        put("orc", "textures/cards/orc.png");
        put("ogre", "textures/cards/ogre.png");
        put("magma_golem", "textures/cards/magma_golem.png");
        put("ice_golem", "textures/cards/ice_golem.png");
        put("moss_golem", "textures/cards/moss_golem.png");
    }};


    private static AssetManager assetManager;

    public Assets() {
        assetManager = new AssetManager();
    }

    public static void dispose(){
        assetManager.dispose();
    }

    public void queueAddSkin() {
        SkinLoader.SkinParameter paramDeck1 = new SkinLoader.SkinParameter("decks/deck1.atlas");
        SkinLoader.SkinParameter paramDeck2 = new SkinLoader.SkinParameter("decks/deck2.atlas");

        assetManager.load(deck1, Skin.class, paramDeck1);
        assetManager.load(deck2, Skin.class, paramDeck2);

    }

    public static void load() {
        assetManager.load(playBtn, Texture.class);
        assetManager.load(settingBtn, Texture.class);
        assetManager.load(background, Texture.class);

        assetManager.load(pathToAttackIcon, Texture.class);
        assetManager.load(pathToHealtchIcon, Texture.class);
        assetManager.load(pathToGreenRect, Texture.class);
        assetManager.load(pathToBlackRect, Texture.class);

        assetManager.load(deck, Texture.class);
        assetManager.load(boardBackground, Texture.class);
        assetManager.load(enemyRect, Texture.class);
        assetManager.load(handRect, Texture.class);


        //assetManager.load(deck1, Skin.class);
        //assetManager.load(deck2, Skin.class);

        for (String card : cards.keySet()) {
            assetManager.load(cards.get(card), Texture.class);
        }

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

    public static Skin getSkin(String path) {
        return assetManager.get(path, Skin.class);
    }

}
