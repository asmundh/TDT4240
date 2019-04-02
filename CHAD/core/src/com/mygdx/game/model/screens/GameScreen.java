package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.CardGame;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.systems.BoardSystem;
import com.mygdx.game.model.systems.CardSystem;
import com.mygdx.game.model.systems.PlayerSystem;
import com.mygdx.game.view.BoardView;
import com.mygdx.game.view.CardView;
import com.mygdx.game.World;

import java.util.List;

public class GameScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private World world;
    private Engine engine;
    private BoardView bv;

    List<Entity> players;
    Entity boardEntity;
    ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);

  
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

        //Testing under
        engine.getSystem(CardSystem.class).setHealth(pm.get(players.get(0)).hand.get(0), 1000);



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
//        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

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

    @Override
    public void handleInput() {
//        if(Gdx.input.isTouched()){
//            //game.setScreen(new MenuScreen(game, engine));
//            //engine.getSystem(CardSystem.class).takeDamage(pm.get(players.get(0)).hand.get(0), 1);
//            engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(0), 0);
//        }

        //testing under
        if(Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            engine.getSystem(PlayerSystem.class).AddCardToTable(players.get(0), 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)  && !Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            engine.getSystem(PlayerSystem.class).addCardToHand(players.get(0), engine.getSystem(PlayerSystem.class).removeCardOnTable(players.get(0), 0));
        }


    }
}
