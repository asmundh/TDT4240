package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.AndroidInterface;
import com.mygdx.game.CardGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = CardGame.TITLE;
		//new LwjglApplication(new CardGame(), config);
		new LwjglApplication(new CardGame(new AndroidInterface() {
			@Override
			public String getMessage() {
				return null;
			}

			@Override
			public void changeView() {

			}

			@Override
			public String getPlayerId() {
				return null;
			}

			@Override
			public String getDisplayName() {
				return null;
			}

			@Override
			public boolean getIsDoingTurn() {
				return false;
			}

			@Override
			public void startQuickMatch() {

			}

			@Override
			public String getOpponentDisplayName() {
				return null;
			}

			@Override
			public String getGameData() {
				return null;
			}

			@Override
			public String getGameDataFromCore() {
				return null;
			}

			@Override
			public void sendGameData(String gameData) {

			}

			@Override
			public void endTurn() {

			}

			@Override
			public boolean endMatch() {
				return false;
			}

			@Override
			public void sendGameDataAndEndTurn(String gameData) {

			}

			@Override
			public boolean getIsSignedIn() {
				return true;
			}

			@Override
			public void manualSignIn() {

			}

			public void createNewMatch() {

			}

			public boolean getFoundOpponent() {
				return false;
			}

			@Override
			public int getTurnCounter() {
				return 0;
			}

			@Override
			public void gdxEndMatch() {

			}

			@Override
			public void setMatchNull() {

			}

			@Override
			public void dismissAllMatches() {

			}
		}), config);
  }

}
