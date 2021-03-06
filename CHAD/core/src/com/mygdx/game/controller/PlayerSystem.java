package com.mygdx.game.controller;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.model.World;
import com.mygdx.game.model.components.PlayerComponent;

import java.util.List;

/*
This is the system that updates the components in the player entity.
It is also the bridge to get data from the components to the views.
 */

public class PlayerSystem extends IteratingSystem {
    private static final Family family = Family.all(PlayerComponent.class).get();
    private ComponentMapper<PlayerComponent> pm;

    public PlayerSystem() {
        super(family);
        pm = ComponentMapper.getFor(PlayerComponent.class);
    }

    public Entity getCardFromHand(Entity player, int index) {
        return pm.get(player).hand.get(index);
    }

    // Used to initiilize the deck
    public void setUpDeck(World world, Entity player, int numOfCards){
        for(int i = 0; i < numOfCards; i++){
            addCardToDeck(player, world.createRandomCard());
        }

        for (int i = 0; i < numOfCards; i++) {
            Entity card = player.getComponent(PlayerComponent.class).deck.get(i);
        }
    }

    public List<Entity> getCardsOnTable(Entity playerEntity) {
        return pm.get(playerEntity).cardsOnTable;
    }
    public List<Entity> getCardsOnHand(Entity playerEntity) {
        return pm.get(playerEntity).hand;
    }

    public void addCardToDeck(Entity player, Entity card){
        pm.get(player).deck.add(card);
    }

    public void setHealth(Entity playerEntity, int health) {
        if (pm.get(playerEntity).health < 0) {
            pm.get(playerEntity).health = 10 * 1000;
        }
        else {
            pm.get(playerEntity).health = health;
        }
    }

    // Take last card from deck, and add to hand list
    public void pickFromDeck(Entity entity) {
        if (pm.get(entity).hand.size() < 5) {
            Entity card = pm.get(entity).deck.remove(pm.get(entity).deck.size() - 1);
            pm.get(entity).hand.add(card);
        }
    }

    // From hand to table
    public void AddCardToTable(Entity playerEntity, int index) {
        if (tableHasRoom(playerEntity)) {
            pm.get(playerEntity).cardsOnTable.add(pm.get(playerEntity).hand.remove(index));
        } else {
            return;
        }
    }

    public boolean tableHasRoom(Entity playerEntity) {
        return (pm.get(playerEntity).cardsOnTable.size() < 4);
    }

    public void clearBoard(Entity playerEntity) {
        pm.get(playerEntity).cardsOnTable.clear();
    }

    public void payForCard(Entity playerEntity, int cost) {
        pm.get(playerEntity).manaPoints -= cost;
    }

    public int getManaPoints(Entity playerEntity) {
        return pm.get(playerEntity).manaPoints;
    }

    public void setManaPoints(Entity playerEntity, int manaPoints) {
        if (manaPoints > 10) {
            pm.get(playerEntity).manaPoints = 10;
        }
        else {
            pm.get(playerEntity).manaPoints = manaPoints;
        }
    }

    // Returns given card in table
    public Entity getCardOnTable(Entity playerEntity , int index) {
        return pm.get(playerEntity).cardsOnTable.get(index);
    }

    public Entity removeCardOnTable(Entity playerEntity , int index) {
        return pm.get(playerEntity).cardsOnTable.remove(index);
    }

    public void addCardToTable(Entity playerEntity, Entity cardEntity) {
        System.out.println("PlayerSystem - addCardToTable(): trying to add a card to table");
        if (pm.get(playerEntity).cardsOnTable.size() < 5) {

            pm.get(playerEntity).cardsOnTable.add(cardEntity);
            System.out.println("PlayerSystem - addCardToTable(): added a card to table");
        }
    }

    public boolean getIsYourTurn(Entity playerEntity) {
        return pm.get(playerEntity).isYourTurn;
    }

    public void setIsYourTurn(Entity playerEntity, boolean bool) {
        pm.get(playerEntity).isYourTurn = bool;
    }

    public void takeDamage(Entity playerEntity, int damage) {
        if (pm.get(playerEntity).health - damage < 0) {
            pm.get(playerEntity).health = 0;
        }
        else {
            pm.get(playerEntity).health -= damage;
        }
    }

    public int getHealth(Entity entity) {
        return pm.get(entity).health;
    }

    public String getPlayerName(Entity entity) {
        return pm.get(entity).name;
    }

    public void setPlayerName(Entity playerEntity, String name) {
        pm.get(playerEntity).name = name;
    }

    public void increaseYourTurnNumber(Entity playerEntity) {
        pm.get(playerEntity).yourTurnNumber++;
    }

    public int getYourTurnNumber(Entity playerEntity) {
        return pm.get(playerEntity).yourTurnNumber;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComp = pm.get(entity);
    }
}
