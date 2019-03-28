package com.mygdx.game;

public interface AndroidInterface {

    /*

    These are the getFunctions and sendFunctions that cardGame can use to get and send information
    to and from Google Play Games Services. The functions can be called by invoking
     game.androidInterface.getFunction(), where game is an instance of cardGame, and androidInterface
     is and instance of AndroidclassBasicAndroidInterface (which implements the AndroidInterface (this interface))

     */

    public String getMessage();

    public void changeView();

    public String getPlayerId();

    public String getDisplayName();

    public boolean getIsDoingTurn();

    public void startQuickMatch();

    public String getOpponentDisplayName();
}
