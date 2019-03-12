package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.screens.MenuScreen;
import com.mygdx.game.model.systems.GameStateManager;

public class CardGame extends Game {
	public SpriteBatch batch;
	Texture img;

	private GameStateManager gsm;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		Gdx.gl.glClearColor(1, 0, 0, 1);
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
		img.dispose();
	}
}
