package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.components.PositionComponent;
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
        TextureComponent tc = new TextureComponent();

        // TODO fjerne denne, gjør den bedre : Testdata
        tc.texture  = Assets.getTexture(Assets.background);
        bc.showHand = true;

        boardEntity.add(bc);
        boardEntity.add(tc);

        engine.addEntity(boardEntity);

        return boardEntity;
    }

    public Entity createCard(int id) {
        Entity cardEntity = new Entity();

        CardPowerComponent cardPower = new CardPowerComponent();
        CardStatsComponent cardStats = new CardStatsComponent();
        TextureComponent tc = new TextureComponent();

        switch (id){
            case 1:
                // CardPower
                cardPower.powerName = "TestEffekt";
                cardPower.powerEffectText = "Description";
                cardPower.powerSize = 2;
                cardPower.powerType = 3;

                //CardStats
                cardStats.attackPower = 2;
                cardStats.health = 3;
                cardStats.cost = 4;
                cardStats.cardPower = cardPower;

                //Texture
                tc.texture = Assets.getTexture(Assets.playBtn);
        }

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
