package com.mygdx.game.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class Assets {
    public static String attackIcon = "textures/attackIcon.png";
    public static String healtchIcon = "textures/healthIcon.png";
    public static String greenRect = "textures/greenRect.png";
    public static String blackRect = "textures/blackRect.png";

    public static String quitBtn = "textures/quit_btn.png";
    public static String yesBtn = "textures/yes_btn.png";
    public static String noBtn = "textures/no_btn.png";
    public static String stepBackBtn = "textures/stepBackBtn.png";
    public static String stepForwardBtn = "textures/stepForwardBtn.png";
    public static String gameOverText = "textures/game_over.png";
    public static String tutorialBtn = "textures/tutorialBtn.png";
    public static String end_turnBtn = "textures/end_turnBtn.png";
    public static String card_back = "textures/card_back.png";
    public static String game_screen_bg = "textures/game_screen_bg.png";
    public static String replayBtn = "textures/replayBtn.png";
    public static String menuBtn = "textures/menuBtn.png";
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

    public static String goblin = "textures/cards/goblin2.png";
    public static String ogre = "textures/cards/ogre.png";
    public static String orc = "textures/cards/orc.png";
    public static String iceGolem = "textures/cards/ice_golem.png";
    public static String magmaGolem = "textures/cards/magma_golem.png";
    public static String mossGolem = "textures/cards/moss_golem.png";

    public static String signInButton = "textures/sign_in_button.png";
    public static String lookingForMatch = "textures/lookingForMatch.png";

    // Music and sound:
    public static String backgroundMusic = "music/RobbH_-_Magic_Moon_-_Restless_heart_(rnb_beat_mix).mp3";
    public static String btnClick = "music/sfx/16930_1461335337.mp3";

    public static HashMap<String, String> cards = new HashMap<String, String>() {{
        put("goblin", "textures/cards/goblin2.png");
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

    public static void load() {
        assetManager.load(healtchIcon, Texture.class);
        assetManager.load(attackIcon, Texture.class);
        assetManager.load(greenRect, Texture.class);
        assetManager.load(blackRect, Texture.class);
        assetManager.load(quitBtn, Texture.class);
        assetManager.load(yesBtn, Texture.class);
        assetManager.load(noBtn, Texture.class);
        assetManager.load(stepBackBtn, Texture.class);
        assetManager.load(stepForwardBtn, Texture.class);
        assetManager.load(gameOverText, Texture.class);
        assetManager.load(tutorialBtn, Texture.class);
        assetManager.load(end_turnBtn, Texture.class);
        assetManager.load(card_back, Texture.class);
        assetManager.load(game_screen_bg, Texture.class);
        assetManager.load(replayBtn, Texture.class);
        assetManager.load(menuBtn, Texture.class);
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
        assetManager.load(signInButton, Texture.class);

        assetManager.load(lookingForMatch, Texture.class);

        for (String card : cards.keySet()) {
            assetManager.load(cards.get(card), Texture.class);
        }

        assetManager.load(menuBG, Texture.class);
        assetManager.load(backBtn, Texture.class);
        assetManager.load(exitBtn, Texture.class);
        assetManager.load(exit_gameBtn, Texture.class);
        assetManager.load(signInButton, Texture.class);
        assetManager.load(lookingForMatch, Texture.class);

        // Music and sound:
        assetManager.load(backgroundMusic, Music.class);
        assetManager.load(btnClick, Sound.class);
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

    public static Music getMusic(String path) {
        return assetManager.get(path, Music.class);
    }

    public  static Sound getSound(String path) {
        return assetManager.get(path, Sound.class);
    }
}