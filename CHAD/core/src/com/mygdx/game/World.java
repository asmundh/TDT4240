package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardPowerTypeComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.components.PositionComponent;

public class World  {

    //This class is to be used in Gamescreen when entities are needed.

    private Engine engine;

    public World(Engine engine){
        this.engine = engine;

    }

    public Entity createBoard() {
        Entity boardEntity = new Entity();

        Component bc = new BoardComponent();
        Component pos = new PositionComponent();

        boardEntity.add(bc);
        boardEntity.add(pos);


        engine.addEntity(boardEntity);

        return boardEntity;
    }

    public Entity createCard() {
        Entity cardEntity = new Entity();

        Component cardPower = new CardPowerComponent();
        Component cardtype = new CardPowerTypeComponent();
        Component cardStats = new CardStatsComponent();
        Component pos = new PositionComponent();

        cardEntity.add(cardPower);
        cardEntity.add(cardtype);
        cardEntity.add(cardStats);
        cardEntity.add(pos);

        engine.addEntity(cardEntity);

        return cardEntity;
    }

    public Entity createPlayer() {
        Entity playerEntity = new Entity();

        Component playerComp = new PlayerComponent();

        playerEntity.add(playerComp);

        engine.addEntity(playerEntity);


        return playerEntity;
    }


}
