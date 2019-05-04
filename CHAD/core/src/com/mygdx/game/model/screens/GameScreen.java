package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.CardGame;
import com.mygdx.game.controller.BoardSystem;
import com.mygdx.game.controller.CardSystem;
import com.mygdx.game.controller.PlayerSystem;
import com.mygdx.game.model.Assets;
import com.mygdx.game.model.GameStateObject;
import com.mygdx.game.model.World;
import com.mygdx.game.view.BoardView;
import com.mygdx.game.view.GameOverScreen;
import com.mygdx.game.view.ScreenInterface;

import java.util.ArrayList;
import java.util.List;


public class GameScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private World world;
    private Engine engine;
    private BoardView bv;
    private List<Entity> players;
    private Stage gameStage;
    private Entity boardEntity;

    private String userName = null;
    private String opponentUserName = null;


    private int turnCounter = 0;
    private boolean loadedNewTurn = false;
  
    protected GameScreen(CardGame game, Engine engine) {
        this.game = game;

        this.engine = engine;
        this.world = new World(engine);

        create();
    }

    @Override
    public void create() {
        gameStage = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(gameStage); // Set inputs to be handled by the stage

        players = world.createPlayers();
        boardEntity = world.createBoard();

        engine.addSystem(new PlayerSystem());
        engine.addSystem(new CardSystem());
        engine.addSystem(new BoardSystem());
        engine.getSystem(BoardSystem.class).addPlayer(boardEntity, players);

        for (int i = 0; i < 5; i++) {
            engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(0));

            engine.getSystem(PlayerSystem.class).increaseYourTurnNumber(players.get(0)); //hvorfor funker ikke dette?!


            final Button quitBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.quitBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.quitBtn))));
            quitBtn.setTransform(true);
            quitBtn.setSize(quitBtn.getWidth() / 2, quitBtn.getHeight() / 2);
            quitBtn.setOrigin(quitBtn.getWidth() / 2, quitBtn.getHeight() / 2);

            quitBtn.addListener(new ClickListener() {
                @Override // Fires when the user lets go of the button
                public void clicked(InputEvent event, float x, float y) {
                    //game.setScreen(new ConfirmationScreen(game, engine, "Are you sure you want to end this game?"));
                    loadTurnCounter();
                }

                @Override // Fires when the button is pressed down
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    quitBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override // Fires when the button is released
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    quitBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
                }
            });

            Table menuTable = new Table(); // Table containing the buttons on the screen
            menuTable.add(quitBtn);
            menuTable.getCell(quitBtn).height(quitBtn.getHeight()).width(quitBtn.getWidth());
            menuTable.setFillParent(true);
            menuTable.moveBy((-Gdx.graphics.getWidth() / 2 + 150), (Gdx.graphics.getHeight() / 2 - 60));

            gameStage.addActor(menuTable); // Add the table containing the buttons to the stage


            bv = new BoardView(game, boardEntity);

            // Initiate player-names
            this.userName = this.game.androidInterface.getDisplayName();
            this.opponentUserName = this.game.androidInterface.getOpponentDisplayName();
            engine.getSystem(PlayerSystem.class).setPlayerName(players.get(0), userName);
            engine.getSystem(PlayerSystem.class).setPlayerName(players.get(1), opponentUserName);
        }

        checkAndLoadNewTurn();
    }

    @Override
    public void update(float dt) {
        handleInput();
        engine.update(dt);
        gameStage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Winning: player 1
        if (engine.getSystem(PlayerSystem.class).getHealth(players.get(0)) == 0 && engine.getSystem(PlayerSystem.class).getHealth(players.get(1)) != 0) {
            endTurn(boardEntity);
            game.androidInterface.endMatch();
            game.setScreen(new GameOverScreen(game, engine, players.get(1), players.get(0)));
        }

        // Winning: player 2
        else if(engine.getSystem(PlayerSystem.class).getHealth(players.get(1)) == 0 && engine.getSystem(PlayerSystem.class).getHealth(players.get(0)) != 0) {
            endTurn(boardEntity);
            game.androidInterface.endMatch();
            game.setScreen(new GameOverScreen(game, engine, players.get(0), players.get(1)));
        }

        else {
            bv.draw(game.batch);
        }
        gameStage.draw();
    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    @Override
    public void dispose () {
        super.dispose();
    }

    // Check if gameData on server != null --> means that the state has been updated
    public boolean checkNewTurn() {
        return (game.androidInterface.getGameData() != null);
    }
    public boolean checkNotFirstTurn() {
        if(game.androidInterface.getTurnCounter() == 9000){
            System.out.println("There seems to be a new game and it is now you who started");
            return false;
        }
        return true;
        /*else{
            System.out.println("checkNewTurn(): getGameData: "+ game.androidInterface.getGameData());
            System.out.println("checkNewTurn(): my local turncounter: "+ turnCounter);

            game.androidInterface.getGameData();
            if(!(game.androidInterface.getTurnCounter() == turnCounter)){
                System.out.println("checkNewTurn(): getTurnCounter: "+ game.androidInterface.getTurnCounter());
                turnCounter = game.androidInterface.getTurnCounter();
                return (game.androidInterface.getGameData() != null);

            return false;
        }*/
    }

    public void loadTurnCounter(){
        System.out.println("loadTurnCounter(): my local turncounter: " + turnCounter);

        // Loads the turncounter on the server
        int loadedTurnCounter = game.androidInterface.getTurnCounter();
        System.out.println("loadTurnCounter(): received turnCounter: " + loadedTurnCounter);


        // Compare the turnCounter to the local turncounter
        if(loadedTurnCounter != turnCounter){   // received new turnCounter, there has been an update
            turnCounter = loadedTurnCounter;
            // load new turn
            loadedNewTurn = false;
            checkAndLoadNewTurn();
        }
        else{
            System.out.println("loadTurnCounter(): the counter received from server is equal to your local counter...");
        }
    }

    /*TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (checkNotFirstTurn()) {

                System.out.println("Received something else than null from server...");
                System.out.println("gameData received: " + game.androidInterface.getGameData());
                parseNewTurn(game.androidInterface.getGameData());
            }
        }
    };*/

    public void checkAndLoadNewTurn() {
        /*Timer timer = new Timer();
        long delay = 0;
        long interval = 30000;
        timer.schedule(task, delay, interval);*/
        if (checkNotFirstTurn()) {

            //System.out.println("Received something else than null from server...");
            System.out.println("gameData received: " + game.androidInterface.getGameData());
            if(!(loadedNewTurn)){
                parseNewTurn(game.androidInterface.getGameData());
                loadedNewTurn = true;
            }
        }

    }


    public void parseNewTurn(String gameState) {
        if(gameState.equals("First turn")){
            System.out.println("Received - first turn - from server, game just started, will not parse...");
            return;
        }


        System.out.println("parseNewTurn starting...");
        System.out.println("parseNewTurn(): gameState: " + gameState);

        int playerHealth = 0;
        int enemyHealth = 0;
        List<Integer> playerHand = new ArrayList();
        List<Integer> enemyHand = new ArrayList();
        List<List<Integer>> playerBoard = new ArrayList<>();
        List<List<Integer>> enemyBoard = new ArrayList<>();

        int currentCategory = 0;
        int currentCardCategory = 0;
        String sb = "";
        int currentCardId = 0;
        int currentCardHealth = 0;
        int currentCardAttack = 0;
        boolean categoryFlag = false;
        boolean currentCardCategoryFlag = false;

        for (int i = 0; i < gameState.length(); i++) {
            if(categoryFlag){
                categoryFlag = false;
                currentCategory ++;
            }
            if(currentCardCategoryFlag){
                currentCardCategoryFlag = false;
                currentCardCategory ++;
            }
            if (currentCategory == 0) {
                if (!(gameState.charAt(i) == '#')) {
                    sb = sb + gameState.charAt(i);
                } else {
                    //currentCategory++;
                    categoryFlag = true;
                    enemyHealth = Integer.valueOf(sb);
                    sb = "";
                }
            }
            if (currentCategory == 1) {
                if (!(gameState.charAt(i) == '#')) {
                    sb = sb + gameState.charAt(i);
                } else {
                    //currentCategory++;
                    categoryFlag = true;
                    playerHealth = Integer.valueOf(sb);
                    sb = "";
                }
            }
            /*if (currentCategory == 2) {
                if (!(gameState.charAt(i) == '#')) {
                    if (!(gameState.charAt(i) == 'i')) {
                        sb = sb + gameState.charAt(i);
                    } else {
                        //currentCardCategory++;
                        enemyHand.add(Integer.valueOf(sb));
                        sb = "";
                    }
                } else {
                    // currentCategory++;
                    categoryFlag = true;
                    sb = "";
                }
            }
            if (currentCategory == 3) {
                if (!(gameState.charAt(i) == '#')) {
                    if (!(gameState.charAt(i) == 'i')) {
                        sb = sb + gameState.charAt(i);
                    } else {
                        //currentCardCategory++;
                        playerHand.add(Integer.valueOf(sb));
                        sb = "";
                    }
                } else {
                    //currentCategory++;
                    categoryFlag = true;
                    sb = "";
                }
            }*/

            if (currentCategory == 2) {
                System.out.println("parseNewturn(): currentCategory == 2");
                boolean finishedCard = false;
                if (!(gameState.charAt(i) == '#')) {
                    if(gameState.charAt(i) == 'c'){

                    }
                    else{
                        if (currentCardCategory == 0) {
                            System.out.println("parseNewturn(): currentCardCategory == 0");
                            if (!(gameState.charAt(i) == 'i')) {
                                sb = sb + gameState.charAt(i);
                            } else {
                                //currentCardCategory++;
                                System.out.println("parseNewTurn(): Found an 'i', setting currentCardCategoryFlag to true");
                                currentCardCategoryFlag = true;
                                currentCardId = Integer.valueOf(sb);
                                sb = "";

                            }
                        }
                        if (currentCardCategory == 1) {
                            System.out.println("parseNewturn(): currentCardCategory == 1");
                            if (!(gameState.charAt(i) == 'h')) {
                                sb = sb + gameState.charAt(i);
                            } else {
                                //currentCardCategory++;
                                System.out.println("parseNewTurn(): Found an 'h', setting currentCardCategoryFlag to true");
                                currentCardCategoryFlag = true;
                                currentCardHealth = Integer.valueOf(sb);
                                sb = "";
                            }
                        }
                        if (currentCardCategory == 2) {
                            System.out.println("parseNewturn(): currentCardCategory == 2");
                            if (!(gameState.charAt(i) == 'a')) {
                                sb = sb + gameState.charAt(i);
                            } else {
                                //currentCardCategory++;
                                //currentCardCategoryFlag = true;
                                System.out.println("parseNewTurn(): Found an 'a', setting currentCardCategoryFlag to true");

                                currentCardAttack = Integer.valueOf(sb);
                                sb = "";
                                finishedCard = true;
                            }
                        }
                    }

                    if(finishedCard) {
                        List<Integer> newCard = new ArrayList();
                        newCard.add(currentCardId);
                        newCard.add(currentCardHealth);
                        newCard.add(currentCardAttack);
                        enemyBoard.add(newCard);
                        System.out.println("parseNewTurn(): Attempted to add a card to enemyBoard");
                        System.out.println("parseNewTurn(): These were the stats of the attempted card: " + "id: " + currentCardId+ ", health: "+ currentCardHealth + ", attack: " + currentCardAttack +".");
                        currentCardCategory = 0;
                        finishedCard = false;
                    }
                }
                else {
                    //currentCategory++;
                    categoryFlag = true;
                    currentCardCategory = 0;
                    sb = "";
                }

            }
            if (currentCategory == 3) {
                System.out.println("parseNewTurn(): currentCatergory is 3.");
                System.out.println("parseNewTurn(): currentCardCategory is: " + currentCardCategory);
                boolean finishedCard = false;
                if (!(gameState.charAt(i) == '#')) {
                    if(gameState.charAt(i) == 'c'){

                    }
                    else{
                        if (currentCardCategory == 0) {
                            System.out.println("parseNewTurn(): currentCardCategory == 0");
                            if (!(gameState.charAt(i) == 'i')) {
                                sb = sb + gameState.charAt(i);
                            } else {
                                //currentCardCategory++;
                                System.out.println("parseNewTurn(): Found an 'i', setting currentCardCategoryFlag to true");
                                currentCardCategoryFlag = true;
                                currentCardId = Integer.valueOf(sb);
                                sb = "";

                            }
                        }
                        if (currentCardCategory == 1) {
                            System.out.println("parseNewTurn(): currentCardCategory == 1");
                            if (!(gameState.charAt(i) == 'h')) {
                                sb = sb + gameState.charAt(i);
                            } else {
                                System.out.println("parseNewTurn(): Found an 'h', setting currentCardCategoryFlag to true");

                                //currentCardCategory++;
                                currentCardCategoryFlag = true;
                                currentCardHealth = Integer.valueOf(sb);
                                sb = "";
                            }
                        }
                        if (currentCardCategory == 2) {
                            System.out.println("parseNewTurn(): currenCardCategory == 2");
                            if (!(gameState.charAt(i) == 'a')) {
                                sb = sb + gameState.charAt(i);
                            } else {
                                System.out.println("parseNewTurn(): Found an 'a', setting currentCardCategoryFlag to true");
                                System.out.println("parseNewTurn(): found an 'a' setting finishedCard to true");
                                //currentCardCategory++;
                                //currentCardCategoryFlag = true;
                                currentCardAttack = Integer.valueOf(sb);
                                sb = "";
                                finishedCard = true;
                            }
                        }
                    }

                    if(finishedCard) {
                        List<Integer> newCard = new ArrayList();
                        newCard.add(currentCardId);
                        newCard.add(currentCardHealth);
                        newCard.add(currentCardAttack);
                        playerBoard.add(newCard);
                        System.out.println("parseNewTurn(): Attempted to add a card to playerBoard");
                        System.out.println("parseNewTurn(): These were the stats of the attempted card: " + "id: " + currentCardId+ ", health: "+ currentCardHealth + ", attack: " + currentCardAttack +".");
                        currentCardCategory = 0;
                        finishedCard = false;
                    }
                }
                else {
                    //currentCategory++;
                    categoryFlag = true;
                    currentCardCategory = 0;
                    sb = "";
                }

            }
        }

        // printing hands of players
        for(int i = 0; i < playerHand.size(); i++){
            System.out.println("parseNewTurn(): playerHand array: index of :" + i + ": " + playerHand.get(i));
        }

        for(int i = 0; i < enemyHand.size(); i++){
            System.out.println("parseNewTurn(): enemyHand array: index of :" + i + ": " + enemyHand.get(i));
        }

        // printing boards of players
        for(int i = 0; i < playerBoard.size(); i++){
            System.out.println("parseNewTurn(): playerBoard array: index of :" + i + ": " + playerBoard.get(i));
        }

        for(int i = 0; i < enemyBoard.size(); i++){
            System.out.println("parseNewTurn(): enemyBoard array: index of :" + i + ": " + enemyBoard.get(i));
        }

        //Updating players healths
        engine.getSystem(PlayerSystem.class).setHealth(players.get(0), playerHealth);
        engine.getSystem(PlayerSystem.class).setHealth(players.get(1), enemyHealth);

        //Creating card entities for player and enemy
        /*List<Entity> handEntityList = new ArrayList();
        List<Entity> enemyHandEntityList = new ArrayList();
        for (int i = 0; i < playerHand.size(); i++) {
            handEntityList.add(world.createCard(playerHand.get(i)));
        }
        for (int i = 0; i < enemyHand.size(); i++) {
            enemyHandEntityList.add(world.createCard(enemyHand.get(i)));
        } */

        /*//Clearing hands and adding updated cards
        engine.getSystem(PlayerSystem.class).clearHand(players.get(0));
        System.out.println("Size of hand after clearing: " + engine.getSystem(PlayerSystem.class).getCardsOnHand(players.get(0)).size());
        engine.getSystem(PlayerSystem.class).clearHand(players.get(1));

        for (int i = 0; i < handEntityList.size(); i++) {
            engine.getSystem(PlayerSystem.class).addCardToHand(players.get(0), handEntityList.get(i));
        }
        for (int i = 0; i < enemyHandEntityList.size(); i++) {
            engine.getSystem(PlayerSystem.class).addCardToHand(players.get(1), enemyHandEntityList.get(i));
        } */

        //Creating card entities for the board
        List<Entity> playerBoardCards = new ArrayList<>();
        List<Entity> enemyBoardCards = new ArrayList<>();
        for (int i = 0; i < playerBoard.size(); i++) {
            playerBoardCards.add(world.createBoardCard(playerBoard.get(i).get(0), playerBoard.get(i).get(1), playerBoard.get(i).get(2)));
        }
        for (int i = 0; i < enemyBoard.size(); i++) {
            enemyBoardCards.add(world.createBoardCard(enemyBoard.get(i).get(0), enemyBoard.get(i).get(1), enemyBoard.get(i).get(2)));
        }

        //clearing entire board and adding updated cards
        engine.getSystem(PlayerSystem.class).clearBoard(players.get(0));
        engine.getSystem(PlayerSystem.class).clearBoard(players.get(1));

        System.out.println("parseNewTurn(): playerBoardCards.size(): " + playerBoardCards.size());

        for (int i = 0; i < playerBoardCards.size(); i++) {
            engine.getSystem(PlayerSystem.class).addCardToTable(players.get(0), playerBoardCards.get(i));
        }
        System.out.println("parseNewTurn(): enemyBoardCards.size(): " + enemyBoardCards.size());
        for (int i = 0; i < enemyBoardCards.size(); i++) {
            engine.getSystem(PlayerSystem.class).addCardToTable(players.get(1), enemyBoardCards.get(i));
        }

        engine.getSystem(PlayerSystem.class).setIsYourTurn(players.get(0), true); //Set your turn to true
        engine.getSystem(PlayerSystem.class).increaseYourTurnNumber(players.get(0)); //Increase your turn number by 1
        engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(0)); //draw new card
        int yourTurnNumber = engine.getSystem(PlayerSystem.class).getYourTurnNumber(players.get(0));
        engine.getSystem(PlayerSystem.class).setManaPoints(players.get(0), 10); //Reset mana points. All turns after 9, players mana points will be reset to 10

        System.out.println("parseNewTurn() ended...");
        System.out.println("parseNewTurn() playerBoardCards size:" + playerBoardCards.size());
        System.out.println("parseNewTurn() enemyBoardCards size:" + enemyBoardCards.size());
        System.out.println("parseNewTurn(): Playerhand size: "+ playerHand.size());
        System.out.println("parseNewTurn(): enemyHand size: "+ enemyHand.size());

        wakeAllCards();


    }

    public void startNewTurn(Entity boardEntity) {
        bv = new BoardView(game, boardEntity);
        this.boardEntity = boardEntity;
        this.players = engine.getSystem(BoardSystem.class).getPlayers(boardEntity);

        engine.getSystem(PlayerSystem.class).setIsYourTurn(players.get(0), true); //Set your turn to true
        engine.getSystem(PlayerSystem.class).increaseYourTurnNumber(players.get(0)); //Increase your turn number by 1
        engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(0)); //draw new card
        int yourTurnNumber = engine.getSystem(PlayerSystem.class).getYourTurnNumber(players.get(0));
        engine.getSystem(PlayerSystem.class).setManaPoints(players.get(0), yourTurnNumber); //Reset mana points. All turns after 9, players mana points will be reset to 10

        wakeAllCards();
    }

    public void endTurn(Entity boardEntity) {
        engine.getSystem(PlayerSystem.class).setIsYourTurn(players.get(0), false); //set your turn to false

        List<Entity> players = engine.getSystem(BoardSystem.class).getPlayers(boardEntity);

        int playerHealth = engine.getSystem(PlayerSystem.class).getHealth(players.get(0));
        int enemyHealth = engine.getSystem(PlayerSystem.class).getHealth(players.get(1));
        List<Entity> playerHand = engine.getSystem(PlayerSystem.class).getCardsOnHand(players.get(0));
        List<Entity> enemyHand = engine.getSystem(PlayerSystem.class).getCardsOnHand(players.get(1));
        List<Entity> playerBoard = engine.getSystem(PlayerSystem.class).getCardsOnTable(players.get(0));
        List<Entity> enemyBoard = engine.getSystem(PlayerSystem.class).getCardsOnTable(players.get(1));
        List playerHandId = new ArrayList<>();
        List enemyHandId = new ArrayList<>();
        List playerBoardId = new ArrayList<>();
        List enemyBoardId = new ArrayList<>();



        /*for(int i = 0; i < playerHand.size(); i++) {
            playerHandId.add(engine.getSystem(CardSystem.class).getId(playerHand.get(i)));
        }
        for(int i = 0; i < enemyHand.size(); i++) {
            enemyHandId.add(engine.getSystem(CardSystem.class).getId(playerHand.get(i)));
        }*/
        for(int i = 0; i < playerBoard.size(); i++) {
            List card = new ArrayList();
            card.add(engine.getSystem(CardSystem.class).getId(playerBoard.get(i)));
            card.add(engine.getSystem(CardSystem.class).getHealth(playerBoard.get(i)));
            card.add(engine.getSystem(CardSystem.class).getAttackPower(playerBoard.get(i)));

            playerBoardId.add(card);
            System.out.println("endTurn(): added these stats for a card on player: id: " + card.get(0) + ", health: " +card.get(1) + ", attack: " + card.get(2) + ".");
        }
        for(int i = 0; i < enemyBoard.size(); i++) {
            List card = new ArrayList();
            card.add(engine.getSystem(CardSystem.class).getId(enemyBoard.get(i)));
            card.add(engine.getSystem(CardSystem.class).getHealth(enemyBoard.get(i)));
            card.add(engine.getSystem(CardSystem.class).getAttackPower(enemyBoard.get(i)));

            enemyBoardId.add(card);
            System.out.println("endTurn(): added these stats for a card on enemy: id: " + card.get(0) + ", health: " +card.get(1) + ", attack: " + card.get(2) + ".");

        }

        GameStateObject gameState = new GameStateObject();
        gameState.playerHealth = playerHealth;
        gameState.enemyHealth = enemyHealth;
        //gameState.playerHand = playerHandId;
        //gameState.enemyHand = enemyHandId;
        gameState.playerBoard = playerBoardId;
        gameState.enemyBoard = enemyBoardId;


        System.out.println("Trying to send game data to server and end turn");
        game.androidInterface.sendGameDataAndEndTurn(gameState.toString());
        System.out.println("Have sent data to server and ended turn AFAIK");
    }





    public void searchAndDestroyDeadCards() {
        for (Entity player : engine.getSystem(BoardSystem.class).getPlayers(boardEntity)) {
            ArrayList<Integer> cardsToRemove = new ArrayList<>();
            for (Entity card : engine.getSystem(PlayerSystem.class).getCardsOnTable(player)) {
                if (engine.getSystem(CardSystem.class).getHealth(card) == 0) {
                    cardsToRemove.add(engine.getSystem(PlayerSystem.class).getCardsOnTable(player).indexOf(card));
                }
            }
            for (int i : cardsToRemove) {
                engine.getSystem(PlayerSystem.class).removeCardOnTable(player, i);
            }
        }

    }

    public void wakeAllCards() {
        for (Entity player : engine.getSystem(BoardSystem.class).getPlayers(boardEntity)) {
            for (Entity card : engine.getSystem(PlayerSystem.class).getCardsOnTable(player)) {
                if (engine.getSystem(CardSystem.class).isSleeping(card)) {
                    engine.getSystem(CardSystem.class).setSleeping(card, false);
                }
            }
        }
    }

    @Override
    public void handleInput() {
        //Input will not be handled if it is not your turn.
        if (!isMyTurn()) return;

        if (Gdx.input.justTouched()) {
            Vector2 pos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            pos.y = Gdx.graphics.getHeight() - pos.y;
            // Depending on where the player has clicked, act accordingly.
            Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);

            if (bv.getShowHandButtonRect().contains(pos)){ // Hides the hand when the button is clicked. Button for showing and hiding hand
                engine.getSystem(BoardSystem.class).changeShowHand(boardEntity);
                deselectCard(prevClickedCard);
            }

            else if (bv.getEndTurnButtonRect().contains(pos)) {
                //End turn and switch whose turn it is
                System.out.println("Trying to end turn");
                deselectCard(prevClickedCard);
                engine.getSystem(BoardSystem.class).turnSwitcher(boardEntity);
                endTurn(boardEntity);
            }

            else if (isHandShowing()) {
                this.handleInputHand(pos);
            }

            else if (bv.getEnemyRectangle().contains(pos)) { // Attack enemy card if we have selected a card from table.
                if (prevClickedCard == null) return;
                else {
                    engine.getSystem(PlayerSystem.class).takeDamage(players.get(1), engine.getSystem(CardSystem.class).getAttackPower(prevClickedCard));
                    deselectCard(prevClickedCard);
                    engine.getSystem(CardSystem.class).setSleeping(prevClickedCard, true);
                }
            }

            else {
                this.handleInputTable(pos);
            }
            searchAndDestroyDeadCards();
        }
    }

    public void handleInputTable(Vector2 pos) {
        int index = getCardTouchedOnTable(pos);
        Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);

        if (index == -1) { // Deselects a card if no card is clicked
            deselectCard(prevClickedCard);
            return;
        }
        else if (index < 4 && index >= 0) { // Friendly cards
            Entity cardClicked = engine.getSystem(PlayerSystem.class).getCardOnTable(players.get(0), index);
            if (isSleeping(cardClicked)) return;
            else {
                if (prevClickedCard == cardClicked) { // Deselect a card
                    deselectCard(cardClicked);
                } else { // Select a new card
                    deselectCard(prevClickedCard);
                    selectCard(cardClicked);
                }
            }
        }
        else if (index >= 4 && prevClickedCard != null) { // Enemy cards. prevClickedCard will not be null if we have already clicked a friendly card
            Entity cardClicked = engine.getSystem(PlayerSystem.class).getCardOnTable(players.get(1), index - 4);
            attackEnemyCard(prevClickedCard, cardClicked);
            deselectCard(prevClickedCard); // Deselects prev clicked card after attack
        }
    }

    public void handleInputHand(Vector2 pos) {
        int indexOfCardHit = getCardTouchedOnHand(pos);

        if (indexOfCardHit == -1) return; // Didnt touch a card.

        else if (indexOfCardHit >= 0) {
            Entity cardChosen = engine.getSystem(PlayerSystem.class).getCardFromHand(players.get(0), indexOfCardHit);
            Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);
            if (prevClickedCard == cardChosen) {
                if (canPlaceCard(cardChosen)) { //player has enough mana for card and table has room
                    placeCard(cardChosen, indexOfCardHit);
                } else {
                    deselectCard(cardChosen);
                }
            } else {
                deselectCard(prevClickedCard);
                selectCard(cardChosen);
            }
        }
    }

    private boolean isSleeping(Entity cardEntity) {
        return engine.getSystem(CardSystem.class).isSleeping(cardEntity);
    }

    private void attackEnemyCard(Entity attackingEntity, Entity attackedEntity) {
        engine.getSystem(CardSystem.class).dealDamage(attackingEntity, attackedEntity); // prevClicked is attacking card, cardClicked is the card being attacked.
        engine.getSystem(CardSystem.class).retaliate(attackedEntity, attackingEntity); // The attacked card attacks back. Ref issue #61
    }

    private int getCardTouchedOnHand(Vector2 pos) {
        List<Rectangle> handPos = bv.getHandPosition();
        int index = -1;

        for (int i = 0; i < engine.getSystem(PlayerSystem.class).getCardsOnHand(players.get(0)).size(); i++) {
            Rectangle rec = handPos.get(i);
            if (rec.contains(pos)) {
                index = handPos.indexOf(rec);
                System.out.println("Hit" + index);
                break;
            }
        }
        return index;
    }

    private int getCardTouchedOnTable(Vector2 pos) {
        int index = -1;
        List<Rectangle> boardPos = bv.getBoardPosition();
        System.out.println("Pos" + pos);

        for (int i = 0; i < engine.getSystem(PlayerSystem.class).getCardsOnTable(players.get(0)).size(); i++) {
            Rectangle rec = boardPos.get(i);
            if (rec.contains(pos)) {
                index = boardPos.indexOf(rec);
                System.out.println("Hit" + index);
                break;
            }
        }
        for (int i = 4; i < engine.getSystem(PlayerSystem.class).getCardsOnTable(players.get(1)).size() + 4; i++) {
            Rectangle rec = boardPos.get(i);
            if (rec.contains(pos)) {
                index = boardPos.indexOf(rec);
                System.out.println("Hit" + index);
                break;
            }
        }
        return index;
    }

    public void placeCard(Entity cardEntity, int indexOfCardToPlace) {
        engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(0), indexOfCardToPlace);
        engine.getSystem(CardSystem.class).deployCard(cardEntity);
        engine.getSystem(PlayerSystem.class).payForCard(players.get(0), engine.getSystem(CardSystem.class).getCost(cardEntity));
        deselectCard(cardEntity);
    }

    public void selectCard(Entity cardEntity) {
        engine.getSystem(BoardSystem.class).cardChosen(boardEntity, cardEntity);
        engine.getSystem(CardSystem.class).updateSelected(cardEntity);
    }


    public void deselectCard(Entity cardEntity) {
        try {
            engine.getSystem(CardSystem.class).updateSelected(cardEntity);
        } catch (NullPointerException e) {}
        engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
    }

    public boolean canPlaceCard(Entity cardChosen) {
        boolean hasRoom = engine.getSystem(PlayerSystem.class).tableHasRoom(players.get(0));
        boolean hasMana = engine.getSystem(PlayerSystem.class).getManaPoints(players.get(0)) >= engine.getSystem(CardSystem.class).getCost(cardChosen);
        return (hasMana && hasRoom);
    }


    private boolean isMyTurn() {
        return engine.getSystem(PlayerSystem.class).getIsYourTurn(players.get(0));
    }

    private boolean isHandShowing() {
        return engine.getSystem(BoardSystem.class).getShowHand(boardEntity);
    }
}
