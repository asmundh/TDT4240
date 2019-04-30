package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.AndroidInterface;
import com.mygdx.game.CardGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
<<<<<<< HEAD
		config.title = CardGame.TITLE;
		new LwjglApplication(new CardGame(), config);
=======
		new LwjglApplication(new CardGame(new AndroidInterface() {
			@Override
			public String getMessage() {
				return null;
			}
		}), config);
>>>>>>> 5cd8f0f72736de97826c381961c64f447cc3305d
	}
}
