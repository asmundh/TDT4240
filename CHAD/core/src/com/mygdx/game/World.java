package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardPowerTypeComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.components.TransformComponent;
import com.mygdx.game.model.components.TextureComponent;
import com.mygdx.game.model.screens.utils.Assets;

public class World  {

    //This class is to be used in Gamescreen when entities are needed.

    private Engine engine;

    public World(Engine engine){
        this.engine = engine;

    }

    public Entity createBoard() {
        Entity boardEntity = new Entity();

        BoardComponent bc = new BoardComponent();
        TransformComponent pos = new TransformComponent();
        TextureComponent tex = new TextureComponent();


        boardEntity.add(bc);
        boardEntity.add(pos);
        boardEntity.add(tex);


        engine.addEntity(boardEntity);

        return boardEntity;
    }

    public Entity createCard() {
        Entity cardEntity = new Entity();

        Component cardPower = new CardPowerComponent();
        Component cardtype = new CardPowerTypeComponent();
        Component cardStats = new CardStatsComponent();
        Component pos = new TransformComponent();

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

    public Entity createTest() {
        Entity testEntity = new Entity();

        TransformComponent transComp  = new TransformComponent();
        TextureComponent texComp = new TextureComponent();

        transComp.position.x = 50;
        transComp.position.y = 50;
        transComp.position.z = 0;
        texComp.region = Assets.getTexture(Assets.playBtn);

        testEntity.add(transComp);
        testEntity.add(texComp);

        engine.addEntity(testEntity);

        return testEntity;
    }
}
