package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.systems.BoardSystem;
import com.mygdx.game.model.systems.CardSystem;
import com.mygdx.game.model.systems.PlayerSystem;
import com.mygdx.game.view.BoardView;
import com.mygdx.game.view.CardView;
import com.mygdx.game.World;

import org.omg.CORBA.Current;

import java.util.List;

public class GameScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private World world;
    private Engine engine;
    private BoardView bv;
    private List<Entity> players;
    private Entity boardEntity;

  
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


        engine.addSystem(new PlayerSystem());
        engine.addSystem(new CardSystem());
        engine.addSystem(new BoardSystem());
        engine.getSystem(BoardSystem.class).addPlayer(boardEntity, players);

        for (int i = 0; i < 5; i++) {
            engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(0));
            //engine.getSystem(PlayerSystem.class).pickFromDeck(players.get(1));
        }

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
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 pos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            System.out.println("Pos");
            System.out.println(pos);
            int index = 0;

            if (engine.getSystem(BoardSystem.class).getShowHand(boardEntity)) {
                List<Rectangle> boardPos = bv.getBoardPosition();
                for (Rectangle rec : boardPos) {
                    System.out.println("Rectangle");
                    System.out.println(rec);
                    if (rec.contains(pos)) {

                        System.out.println(rec);
                        index = boardPos.indexOf(rec);
                        break;
                    }
                    //this.handleInputHand(pos);
                }
                Entity cardChosen = engine.getSystem(PlayerSystem.class).getCardFromHand(players.get(0), index);
                Entity prevClickedCard = engine.getSystem(BoardSystem.class).getClickedCard(boardEntity);

                if (prevClickedCard != null && engine.getSystem(BoardSystem.class).getClickedCard(boardEntity) == cardChosen) {
                    engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(0), index);
                }

                else {
                    engine.getSystem(CardSystem.class).updateSelected(cardChosen);
                    chosenCard(cardChosen);
                }


                /*else{
                this.handleInputTable(pos);
            }*/
            }
        }
    }


    public void handleInputTable(Vector2 pos) {
        int index = 0;
        System.out.println(pos);
        List<Rectangle> boardPos = bv.getBoardPosition();
        for (Rectangle rec : boardPos) {
            if (rec.contains(pos)) {
                System.out.println(rec);
                index = boardPos.indexOf(rec);
                break;
            }


        }
    }

    public void handleInputHand(Vector2 pos) {
        List<Rectangle> handPos = bv.getHandPosition();
        int index = 0;

        for (Rectangle rec : handPos) {
            if (rec.contains(pos)) {
                index = handPos.indexOf(rec);
                break;
            }
        }

        Entity cardChosen = engine.getSystem(PlayerSystem.class).getCardFromHand(players.get(0), index);
        Entity prevClickedCard = engine.getSystem(BoardSystem.class).getClickedCard(boardEntity);

        if (prevClickedCard != null && engine.getSystem(BoardSystem.class).getClickedCard(boardEntity) == cardChosen) {
            engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(0), index);
        }

        else {
            engine.getSystem(CardSystem.class).updateSelected(cardChosen);
            chosenCard(cardChosen);
        }

    }

    //makes the card glow, has to click one more time to confirm.
    public void chosenCard(Entity entity) {
        engine.getSystem(BoardSystem.class).cardChosen(boardEntity, entity);
        engine.getSystem(CardSystem.class).updateSelected(entity);

    }
}
