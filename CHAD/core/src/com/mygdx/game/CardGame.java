package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.screens.LoadingScreen;
import com.mygdx.game.model.screens.utils.assets;

public class CardGame extends Game {
	public final static int HEIGHT = 1080;
	public final static int WIDTH = 1920;
	public final static String TITLE = "C.H.A.D";

	public SpriteBatch batch;
	private assets assets;
	
	@Override
	public void create () {
		assets = new assets();
		Gdx.graphics.setWindowedMode(840, 400); // Dev: DEL in production, sets window size to mobile

		batch = new SpriteBatch();
		Gdx.gl.glClearColor(1, 0, 0, 1);
    
		//this.setScreen(new MenuScreen(this));
		this.setScreen(new LoadingScreen(this));
		Engine engine = new Engine();
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
}
