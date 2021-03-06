package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.Assets;
import com.mygdx.game.view.LoadingScreen;
import com.mygdx.game.model.screens.utils.MusicStateManager;


public class CardGame extends Game {
	public final static int HEIGHT = 1080; // Dev: DEL in production, sets window size to mobile
	public final static int WIDTH = 1920;
	public final static String TITLE = "C.H.A.D";

	public SpriteBatch batch;
	private Assets assets;
	public Engine engine;
	public AndroidInterface androidInterface;
	private Music bgMusic;
	public MusicStateManager musicStateManager;

	public CardGame(AndroidInterface androidInterface){
		this.androidInterface = androidInterface;
	}
	
	@Override
	public void create () {
		assets = new Assets();
		engine = new Engine();

		Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);

		batch = new SpriteBatch();
		Gdx.gl.glClearColor(1, 0, 0, 1);

		this.setScreen(new LoadingScreen(this, engine));
		this.musicStateManager = new MusicStateManager(this);
	}

	@Override
	public void render () {
		GL20 gl = Gdx.gl;
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assets.dispose();
	}

	public float getWidth() {
		return Gdx.graphics.getWidth();
	}

	public float getHeight() {
		return Gdx.graphics.getHeight();
	}

	public Music getBgMusic() {
		return Assets.getMusic(Assets.backgroundMusic);
	}

	public void playMusic(Music music){
		Music bgMusic = getBgMusic();
		if(musicStateManager.getMusicState() && !bgMusic.isPlaying()){
			bgMusic.play();
			bgMusic.setLooping(true);
		}
	}
}
