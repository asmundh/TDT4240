package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
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
import com.mygdx.game.view.CardView;
import com.mygdx.game.World;


import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.Card;

public class GameScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private World world;
    private Engine engine;
    private BoardView bv;
    private List<Entity> players;
    private Entity boardEntity;
    private Stage playing;

  
    protected GameScreen(CardGame game, Engine engine) {
        this.game = game;

        this.engine = engine;
        this.world = new World(engine);

        create();
    }

    @Override
    public void create() {
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



        bv = new BoardView(boardEntity);
    }

    @Override
    public void update(float dt) {

        handleInput();
        engine.update(dt);


    }

    @Override
    public void draw() {


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bv.draw(game.batch);

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
