package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.screens.MenuScreen;

public class CardGame extends Game {
	public final static int HEIGHT = 1080;
	public final static int WIDTH = 1920;
	public final static String TITLE = "C.H.A.D";

	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MenuScreen(this));
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
	}
}
