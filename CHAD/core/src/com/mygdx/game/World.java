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
        CardPowerComponent powerCard = new CardPowerComponent();
        CardStatsComponent statsCard = new CardStatsComponent();

        boardEntity.add(bc);
        boardEntity.add(tc);
        boardEntity.add(powerCard);
        boardEntity.add(statsCard);


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
