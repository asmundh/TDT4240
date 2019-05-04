package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.PlayerComponent;
import com.mygdx.game.model.components.RectangleComponent;
import com.mygdx.game.model.components.TextureComponent;
import com.mygdx.game.model.screens.utils.Assets;
import com.mygdx.game.model.systems.PlayerSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        System.out.println("World.createCard(): - I was fired from somewhere. Trying to create a card!");
        Entity cardEntity = new Entity();

        CardPowerComponent cardPower = new CardPowerComponent();
        CardStatsComponent cardStats = new CardStatsComponent();
        TextureComponent tc = new TextureComponent();
        RectangleComponent rc = new RectangleComponent();

        switch (id){
            case 0:
                // CardPower
                cardPower.powerName = "TestEffekt";
                cardPower.powerEffectText = "Description";
                cardPower.powerSize = 2;
                cardPower.powerType = 3;

                //CardStats
                cardStats.attackPower = 1;
                cardStats.health = 2;
                cardStats.cost = 1;
                cardStats.id = 0;

                //Texture
                tc.texture = Assets.getTexture(Assets.goblin);

                break;

            case 1:
                cardPower.powerName = "Test2Effekt";
                cardPower.powerEffectText = "Description2";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 2;
                cardStats.health = 3;
                cardStats.cost = 2;
                cardStats.id = 1;

                tc.texture = Assets.getTexture(Assets.ogre);

                break;

            case 2:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 3;
                cardStats.health = 2;
                cardStats.cost = 2;
                cardStats.id = 2;

                tc.texture = Assets.getTexture(Assets.orc);

                break;

            case 3:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 4;
                cardStats.health = 6;
                cardStats.cost = 4;
                cardStats.id = 3;

                tc.texture = Assets.getTexture(Assets.iceGolem);

                break;

            case 4:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 5;
                cardStats.health = 8;
                cardStats.cost = 5;
                cardStats.id = 4;

                tc.texture = Assets.getTexture(Assets.mossGolem);

                break;

            case 5:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 6;
                cardStats.health = 10;
                cardStats.cost = 8;
                cardStats.id = 5;

                tc.texture = Assets.getTexture(Assets.magmaGolem);

                break;
        }

        cardEntity.add(cardPower);
        cardEntity.add(cardStats);
        cardEntity.add(tc);
        cardEntity.add(rc);

        engine.addEntity(cardEntity);

        return cardEntity;

    }

    public Entity createBoardCard(int id, int health, int attack) {
        System.out.println("World.createBoardCard(): I was fired from somewhere... trying to create a card for a board");
        Entity cardEntity = new Entity();

        CardPowerComponent cardPower = new CardPowerComponent();
        CardStatsComponent cardStats = new CardStatsComponent();
        TextureComponent tc = new TextureComponent();
        RectangleComponent rc = new RectangleComponent();

        switch (id){
            case 0:
                // CardPower
                cardPower.powerName = "TestEffekt";
                cardPower.powerEffectText = "Description";
                cardPower.powerSize = 2;
                cardPower.powerType = 3;

                //CardStats
                cardStats.attackPower = attack;
                cardStats.health = health;
                cardStats.cost = 1;
                cardStats.id = 0;

                //Texture
                tc.texture = Assets.getTexture(Assets.goblin);

                break;

            case 1:
                cardPower.powerName = "Test2Effekt";
                cardPower.powerEffectText = "Description2";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = attack;
                cardStats.health = health;
                cardStats.cost = 2;
                cardStats.id = 1;

                tc.texture = Assets.getTexture(Assets.ogre);

                break;

            case 2:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = attack;
                cardStats.health = health;
                cardStats.cost = 2;
                cardStats.id = 2;

                tc.texture = Assets.getTexture(Assets.orc);

                break;

            case 3:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = attack;
                cardStats.health = health;
                cardStats.cost = 4;
                cardStats.id = 3;

                tc.texture = Assets.getTexture(Assets.iceGolem);

                break;

            case 4:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = attack;
                cardStats.health = health;
                cardStats.cost = 5;
                cardStats.id = 4;

                tc.texture = Assets.getTexture(Assets.mossGolem);

                break;

            case 5:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = attack;
                cardStats.health = health;
                cardStats.cost = 8;
                cardStats.id = 5;

                tc.texture = Assets.getTexture(Assets.magmaGolem);

                break;
        }

        cardEntity.add(cardPower);
        cardEntity.add(cardStats);
        cardEntity.add(tc);
        cardEntity.add(rc);

        engine.addEntity(cardEntity);

        return cardEntity;

    }

    public Entity createRandomCard() {
        Entity cardEntity = new Entity();

        CardPowerComponent cardPower = new CardPowerComponent();
        CardStatsComponent cardStats = new CardStatsComponent();
        TextureComponent tc = new TextureComponent();
        RectangleComponent rc = new RectangleComponent();

        Random r = new Random();
        int id = r.nextInt(6);


        switch (id){
            case 0:
                // CardPower
                cardPower.powerName = "TestEffekt";
                cardPower.powerEffectText = "Description";
                cardPower.powerSize = 2;
                cardPower.powerType = 3;

                //CardStats
                cardStats.attackPower = 1;
                cardStats.health = 2;
                cardStats.cost = 1;
                cardStats.id = 0;

                //Texture
                tc.texture = Assets.getTexture(Assets.goblin);

                break;

            case 1:
                cardPower.powerName = "Test2Effekt";
                cardPower.powerEffectText = "Description2";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 2;
                cardStats.health = 3;
                cardStats.cost = 2;
                cardStats.id = 1;

                tc.texture = Assets.getTexture(Assets.ogre);

                break;

            case 2:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 3;
                cardStats.health = 2;
                cardStats.cost = 2;
                cardStats.id = 2;

                tc.texture = Assets.getTexture(Assets.orc);

                break;

            case 3:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 4;
                cardStats.health = 6;
                cardStats.cost = 4;
                cardStats.id = 3;

                tc.texture = Assets.getTexture(Assets.iceGolem);

                break;

            case 4:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 5;
                cardStats.health = 8;
                cardStats.cost = 5;
                cardStats.id = 4;

                tc.texture = Assets.getTexture(Assets.mossGolem);

                break;

            case 5:
                cardPower.powerName = "Test3Effekt";
                cardPower.powerEffectText = "Description3";
                cardPower.powerSize = 3;
                cardPower.powerType = 1;

                cardStats.attackPower = 6;
                cardStats.health = 10;
                cardStats.cost = 8;
                cardStats.id = 5;

                tc.texture = Assets.getTexture(Assets.magmaGolem);

                break;
        }

        cardEntity.add(cardPower);
        cardEntity.add(cardStats);
        cardEntity.add(tc);
        cardEntity.add(rc);

        engine.addEntity(cardEntity);

        return cardEntity;

    }

    public List<Entity> createPlayers() {
        List<Entity> entities = new ArrayList<Entity>();
        Entity playerEntity = new Entity();

        PlayerComponent playerComp = new PlayerComponent();

        playerEntity.add(playerComp);


        engine.addEntity(playerEntity);
        engine.addSystem(new PlayerSystem());

        engine.getSystem(PlayerSystem.class).setUpDeck(this, playerEntity, 30);

        Entity playerEnemy = new Entity();

        PlayerComponent playerCompEnemy = new PlayerComponent();
        playerEnemy.add(playerCompEnemy);

        engine.addEntity(playerEnemy);

        engine.getSystem(PlayerSystem.class).setUpDeck(this, playerEnemy, 30);

        entities.add(playerEntity);
        entities.add(playerEnemy);

        return entities;
    }


}
