package com.mygdx.game;

import android.util.Log;

public class AndroidclassBasicAndroidInterface implements AndroidInterface {

    AndroidLauncher mAndroidLauncher = null;

    public AndroidclassBasicAndroidInterface(AndroidLauncher androidLauncher){
        this.mAndroidLauncher = androidLauncher;
    }

    public String getMessage(){
        return "Hello from android module.";
    }

    public void changeView(){
        mAndroidLauncher.changeView();
    }

    public String getPlayerId(){

        return mAndroidLauncher.getPlayerId();
    }

    public String getDisplayName(){

        return mAndroidLauncher.getDisplayName();
    }

    public boolean getIsDoingTurn(){
        return mAndroidLauncher.getIsDoingTurn();
    }

    public String getGameData(){
        return mAndroidLauncher.getGameData();
    }

    public String getGameDataFromCore(){
        return mAndroidLauncher.getGameDataFromCore();
    }

    public void startQuickMatch(){

        mAndroidLauncher.startQuickMatch();
    }

    public String getOpponentDisplayName(){

        return mAndroidLauncher.getOpponentDisplayName();
    }

    public void sendGameData(String gameData){
        mAndroidLauncher.receiveGameData(gameData);
    }

    public void endTurn(){
        mAndroidLauncher.takeTurn();
    }

    public boolean endMatch(){
        return mAndroidLauncher.endMatch();
    }

    public void sendGameDataAndEndTurn(String gameData){
        mAndroidLauncher.receiveGameData(gameData);
        // mAndroidLauncher.takeTurn();
    }

    public boolean getIsSignedIn(){
        return mAndroidLauncher.getIsSignedIn();
    }

    public void manualSignIn(){
        mAndroidLauncher.manualSignIn();
    }

    public void createNewMatch(){
        mAndroidLauncher.createNewMatch();
    }

    public boolean getFoundOpponent(){
        return mAndroidLauncher.getFoundOpponent();
    }

    public void gdxEndMatch(){
        mAndroidLauncher.gdxEndMatch();
    }
}
