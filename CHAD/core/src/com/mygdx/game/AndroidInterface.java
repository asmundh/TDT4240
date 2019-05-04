package com.mygdx.game;

public interface AndroidInterface {

    /*

    These are the getFunctions and sendFunctions that cardGame can use to get and send information
    to and from Google Play Games Services. The functions can be called by invoking
     game.androidInterface.getFunction(), where game is an instance of cardGame, and androidInterface
     is and instance of AndroidclassBasicAndroidInterface (which implements the AndroidInterface (this interface))

     */

    // Does not do anything was just used to test the interface
    public String getMessage();

    // Used to change the view in en AndroidLauncher (between libGDX and my own GPGS button setup, only for tesintg
    // Use this for developer view of androidlauncher, shows the buttons for communicating with GPGS
    public void changeView();

    // Returns the logged in player's userID (not username, but actual id, long string of numbers and letters)
    public String getPlayerId();

    // Returns the logged in player's displayname (username)
    public String getDisplayName();

    // returns whether it is the logged in users turn or not
    public boolean getIsDoingTurn();

    // Used to start an auto-matched 1 v 1 match
    public void startQuickMatch();

    // Used to get the opponents displayname (username), will return null if there is no opponent
    public String getOpponentDisplayName();

    // Used to get the last received gameData that the androidLauncher has received from turnbasedMatch through updateMatch() in androidLauncher
    public String getGameData();

    // Used to get the last data the androidLauncher received from this core, can be used to check what you sent to the launcher and for comparing
    public String getGameDataFromCore();

    /*
    Used to send data from the core to the android launcher
    The android launcher will take its gameDataFromCore string and put in the mTurnData.data
    And thus passing it as the gameData to the other opponent. The opponent's androidLauncher will then
    take that data and pass it into its own mTurnData.
     */
    public void sendGameData(String gameData);

    // Used to call takeTurn() in the androidLauncher. Make sure you have called sendGameData before calling this method
    // If wanted I can make a sendGameDataAndEndTurn() function
    public void endTurn();

    /*
    Used to call endMatch() in androidLauncher. Make sure you ask the user in libGDX if they really
    want to end the match. Only after a second press of a "yes" button should this call be made.
    Returns true if game ended successfully, returns false if it didn't
    */
    public boolean endMatch();

    /*
    Used to first send the game data to launcher, then invoke the "takeTurn()" method.
    Not sure if this is susceptible to bugs. You should rather use sendGameData(String gameData) and endTurn()
    to ensure proper execution of the function calls. It also allows you make a call of getGameDataFromCore()
    to doublecheck that the sent data from sendGameData was properly received before calling endTurn()
    */
    public void sendGameDataAndEndTurn(String gameData);

    // Used to figure out if the user is signed in or not
    public boolean getIsSignedIn();

    // Used to manually sign in, is hooked up to the signInBtn in menuScreen
    public void manualSignIn();

    // Used to create a new match
    public void createNewMatch();

    // Used to check if our match has an opponent (if false, we are still searching)
    public boolean getFoundOpponent();

    // Used to check the turncounter
    public int getTurnCounter();

    // Used to forcibly end a match from GDX
    public void gdxEndMatch();

    // Used to forcibly set match to null
    public void setMatchNull();

    // Used to forcibly dismiss all matches on the server for logged in user
    public void dismissAllMatches();
}
