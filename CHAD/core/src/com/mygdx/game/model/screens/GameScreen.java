package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.CardGame;
import com.mygdx.game.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.screens.utils.Assets;
import com.mygdx.game.model.systems.BoardSystem;
import com.mygdx.game.model.systems.CardSystem;
import com.mygdx.game.model.systems.PlayerSystem;
import com.mygdx.game.view.BoardView;


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
    private Stage playing;

    private String userName = null;
    private String opponentUserName = null;
  
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
      
        Gdx.input.setInputProcessor(playing);
        engine.addSystem(new PlayerSystem());
        engine.addSystem(new CardSystem());
        engine.addSystem(new BoardSystem());
        engine.getSystem(BoardSystem.class).addPlayer(boardEntity, players);

        for (int i = 0; i < 5; i++) {
            engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(0));
            engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(1));
        }
        engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(1), 0);
        engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(1), 0);
        engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(1), 0);

        final Button quitBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.quitBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.quitBtn))));
        quitBtn.setTransform(true);
        quitBtn.setSize(quitBtn.getWidth()/2, quitBtn.getHeight()/2);
        quitBtn.setOrigin(quitBtn.getWidth()/2, quitBtn.getHeight()/2);

        quitBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ConfirmationScreen(game, engine, "Are you sure you want to end this game?"));
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
        menuTable.moveBy((-Gdx.graphics.getWidth()/2 + 150),(Gdx.graphics.getHeight()/2 - 60));

        gameStage.addActor(menuTable); // Add the table containing the buttons to the stage


        bv = new BoardView(boardEntity);

        // Initiate player-names
        this.userName = this.game.androidInterface.getDisplayName();
        this.opponentUserName = this.game.androidInterface.getOpponentDisplayName();
        engine.getSystem(PlayerSystem.class).setPlayerName(players.get(0), userName);
        engine.getSystem(PlayerSystem.class).setPlayerName(players.get(1), opponentUserName);
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
            game.setScreen(new GameOverScreen(game, engine, players.get(1), players.get(0)));
        }

        // Winning: player 2
        else if(engine.getSystem(PlayerSystem.class).getHealth(players.get(1)) == 0 && engine.getSystem(PlayerSystem.class).getHealth(players.get(0)) != 0) {
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

    public void startNewTurn() {
        wakeAllCards();
        engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(0)); //draw new card

    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 pos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            pos.y = Gdx.graphics.getHeight() - pos.y;

            // Depending on where the player has clicked, act accordingly.

            if (bv.getShowHandButtonRect().contains(pos)){
                // Hides the hand when the button is clicked. Button for showing and hiding hand
                engine.getSystem(BoardSystem.class).changeShowHand(boardEntity);
                Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);
                if (prevClickedCard == null) {
                    // Now showing the hand
                    return;
                }
                else {
                    // Unclicks the previously clicked card on the hand. If prev clicked card is green, make it not green and then make new card green.
                    engine.getSystem(CardSystem.class).updateSelected(prevClickedCard);
                    engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
                }
            }
            else if (bv.getEnemyRectangle().contains(pos)) {
                // Attack enemy card if we have selected a card.
                Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);
                if (prevClickedCard == null) {
                    return;
                } else {
                    engine.getSystem(PlayerSystem.class).takeDamage(players.get(1), engine.getSystem(CardSystem.class).getAttackPower(prevClickedCard));
                    engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
                    engine.getSystem(CardSystem.class).updateSelected(prevClickedCard);
                    engine.getSystem(CardSystem.class).setSleeping(prevClickedCard, true);
                }
            }
            else if (bv.getEndTurnButtonRect().contains(pos)) {
                //End turn
                Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);
                if (prevClickedCard == null) {
                    engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
                } else {
                    engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
                    engine.getSystem(CardSystem.class).updateSelected(prevClickedCard);
                }
                wakeAllCards();

                engine.getSystem(PlayerSystem.class).setManaPoints(players.get(0), 10);

                //testing:
                startNewTurn();
            }


            else if (engine.getSystem(BoardSystem.class).getShowHand(boardEntity) == true) {
                this.handleInputHand(pos);
            }

            else {
                this.handleInputTable(pos);
            }

            searchAndDestroyDeadCards();
        }
    }

    public void handleInputTable(Vector2 pos) {
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

        if (index == -1) { // Deselects a card
            Entity prevChosenCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);
            engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
            engine.getSystem(CardSystem.class).updateSelected(prevChosenCard);
            return;
        }

        Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);

        if (index < 4 && index >= 0) { // Friendly cards
            Entity cardClicked = engine.getSystem(PlayerSystem.class).getCardOnTable(players.get(0), index);
            boolean sleeping = engine.getSystem(CardSystem.class).isSleeping(cardClicked);
            if (prevClickedCard != null && !sleeping) {
                if (prevClickedCard == cardClicked) {
                    engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
                    engine.getSystem(CardSystem.class).updateSelected(cardClicked);
                } else {
                    engine.getSystem(CardSystem.class).updateSelected(prevClickedCard); // Deselects prev clicked card
                    engine.getSystem(CardSystem.class).updateSelected(cardClicked); // Selects current clicked card
                    engine.getSystem(BoardSystem.class).cardChosen(boardEntity, cardClicked);
                }
            } else if (prevClickedCard == null && !sleeping) {
                System.out.println("prev er null");
                engine.getSystem(CardSystem.class).updateSelected(cardClicked); // Deselects prev clicked card
                engine.getSystem(BoardSystem.class).cardChosen(boardEntity, cardClicked);

            }
        }

        else if (index >= 4 && prevClickedCard != null) { // Enemy cards. prevClickedCard will not be null if we have already clicked a friendly card

            Entity cardClicked = engine.getSystem(PlayerSystem.class).getCardOnTable(players.get(1), index - 4);
            engine.getSystem(CardSystem.class).updateSelected(prevClickedCard); // Deselects prev clicked card after attack

            engine.getSystem(CardSystem.class).dealDamage(prevClickedCard, cardClicked); // prevClicked is attacking card, cardClicked is the card being attacked.
            engine.getSystem(CardSystem.class).retaliate(cardClicked, prevClickedCard); // The attacked card attacks back. Ref issue #61
            engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);

        }
    }


    public void handleInputHand(Vector2 pos) {
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

        if(index == -1) {
            return;
        }

        if (index >= 0) {
            Entity cardChosen = engine.getSystem(PlayerSystem.class).getCardFromHand(players.get(0), index);
            Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);

            if (prevClickedCard != null) {
                if (prevClickedCard == cardChosen) {
                    // Confirm card and add to table
                    if (engine.getSystem(PlayerSystem.class).getManaPoints(players.get(0)) >= engine.getSystem(CardSystem.class).getCost(cardChosen)) { //player has enough mana for card
                        engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(0), index);
                        engine.getSystem(CardSystem.class).deployCard(cardChosen);
                        engine.getSystem(CardSystem.class).updateSelected(cardChosen);
                        engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
                        engine.getSystem(PlayerSystem.class).payForCard(players.get(0), engine.getSystem(CardSystem.class).getCost(cardChosen));
                    }
                    else { //player does not have enough mana for card
                        engine.getSystem(CardSystem.class).updateSelected(cardChosen);
                                engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
                    }


                } else { // New card chosen
                    engine.getSystem(CardSystem.class).updateSelected(cardChosen);
                    engine.getSystem(CardSystem.class).updateSelected(prevClickedCard);
                    engine.getSystem(BoardSystem.class).cardChosen(boardEntity, cardChosen);
                }
            } else {
                engine.getSystem(BoardSystem.class).cardChosen(boardEntity, cardChosen);
                engine.getSystem(CardSystem.class).updateSelected(cardChosen);
                // engine.getSystem(CardSystem.class).updateSelected(cardChosen);
                // chosenCard(cardChosen);
            }
        }
        else {
            Entity prevClickedCard = engine.getSystem(BoardSystem.class).getPreviouslyClickedCard(boardEntity);
            engine.getSystem(CardSystem.class).updateSelected(prevClickedCard);
            engine.getSystem(BoardSystem.class).cardChosen(boardEntity, null);
        }

    }

    //makes the card glow, has to click one more time to confirm.
    public void chosenCard(Entity cardChosen) {
        engine.getSystem(BoardSystem.class).cardChosen(boardEntity, cardChosen);
        engine.getSystem(CardSystem.class).updateSelected(cardChosen);
    }
}
