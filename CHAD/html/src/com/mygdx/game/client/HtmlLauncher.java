package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mygdx.game.AndroidInterface;
import com.mygdx.game.CardGame;

public class HtmlLauncher extends GwtApplication {

        // USE THIS CODE FOR A FIXED SIZE APPLICATION
        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }
        // END CODE FOR FIXED SIZE APPLICATION

        // UNCOMMENT THIS CODE FOR A RESIZABLE APPLICATION
        // PADDING is to avoid scrolling in iframes, set to 20 if you have problems
        // private static final int PADDING = 0;
        // private GwtApplicationConfiguration cfg;
        //
        // @Override
        // public GwtApplicationConfiguration getConfig() {
        //     int w = Window.getClientWidth() - PADDING;
        //     int h = Window.getClientHeight() - PADDING;
        //     cfg = new GwtApplicationConfiguration(w, h);
        //     Window.enableScrolling(false);
        //     Window.setMargin("0");
        //     Window.addResizeHandler(new ResizeListener());
        //     cfg.preferFlash = false;
        //     return cfg;
        // }
        //
        // class ResizeListener implements ResizeHandler {
        //     @Override
        //     public void onResize(ResizeEvent event) {
        //         int width = event.getWidth() - PADDING;
        //         int height = event.getHeight() - PADDING;
        //         getRootPanel().setWidth("" + width + "px");
        //         getRootPanel().setHeight("" + height + "px");
        //         getApplicationListener().resize(width, height);
        //         Gdx.graphics.setWindowedMode(width, height);
        //     }
        // }
        // END OF CODE FOR RESIZABLE APPLICATION

        @Override
        public ApplicationListener createApplicationListener () {
                return new CardGame(new AndroidInterface() {
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
                                return false;
                        }

                        @Override
                        public void manualSignIn() {

                        }

                        @Override
                        public void createNewMatch() {

                        }

                        @Override
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
                });
        }
}