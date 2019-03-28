package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.screens.LoadingScreen;
import com.mygdx.game.model.screens.utils.Assets;

public class CardGame extends Game {
	public final static int HEIGHT = 1080; // Dev: DEL in production, sets window size to mobile
	public final static int WIDTH = 1920;
	public final static String TITLE = "C.H.A.D";

	public SpriteBatch batch;
	private Assets assets;
	public Engine engine;
	
	@Override
	public void create () {
		assets = new Assets();
		engine = new Engine();

		Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);

		batch = new SpriteBatch();

		Gdx.gl.glClearColor(1, 0, 0, 1);
    
		//this.setScreen(new MenuScreen(this));
		this.setScreen(new LoadingScreen(this, getEngine()));


	}

	@Override
	public void render () {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0.5f, 1.0f, 1.0f, 1.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

	public Engine getEngine() { return this.engine; }
}
