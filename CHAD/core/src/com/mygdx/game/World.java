package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.components.PositionComponent;
import com.mygdx.game.model.components.TextureComponent;

public class World  {

    //This class is to be used in Gamescreen when entities are needed.

    private Engine engine;

    public World(Engine engine){
        this.engine = engine;

    }

    public Entity createBoard() {
        Entity boardEntity = new Entity();

        BoardComponent bc = new BoardComponent();
        TextureComponent tc = new TextureComponent();
        PositionComponent pos1 = new PositionComponent();
        PositionComponent pos2 = new PositionComponent();
        PositionComponent pos3 = new PositionComponent();
        PositionComponent pos4 = new PositionComponent();
        PositionComponent pos5 = new PositionComponent();
        PositionComponent pos6 = new PositionComponent();
        PositionComponent pos7 = new PositionComponent();
        PositionComponent pos8 = new PositionComponent();
        PositionComponent posDeck = new PositionComponent();
        PositionComponent posHand = new PositionComponent();

        boardEntity.add(bc);
        boardEntity.add(tc);
        boardEntity.add(pos1);
        boardEntity.add(pos2);
        boardEntity.add(pos3);
        boardEntity.add(pos4);
        boardEntity.add(pos5);
        boardEntity.add(pos6);
        boardEntity.add(pos7);
        boardEntity.add(pos8);
        boardEntity.add(posDeck);
        boardEntity.add(posHand);


        engine.addEntity(boardEntity);

        return boardEntity;
    }

    public Entity createCard() {
        Entity cardEntity = new Entity();

        CardPowerComponent cardPower = new CardPowerComponent();
        CardStatsComponent cardStats = new CardStatsComponent();
        TextureComponent tc = new TextureComponent();

        cardEntity.add(cardPower);
        cardEntity.add(cardStats);
        cardEntity.add(tc);

        engine.addEntity(cardEntity);

        return cardEntity;
    }

    public Entity createPlayer() {
        Entity playerEntity = new Entity();

        PlayerComponent playerComp = new PlayerComponent();

        playerEntity.add(playerComp);

        engine.addEntity(playerEntity);


        return playerEntity;
    }


}
