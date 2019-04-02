package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.World;
import com.mygdx.game.model.components.PlayerComponent;

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
            addCardToDeck(player, world.createCard(1));
        }
    }

    public void addCardToDeck(Entity player, Entity card){
        pm.get(player).deck.add(card);
    }

    // Take last card from deck, and add to hand list
    public void pickFromDeck(Entity entity) {
        pm.get(entity).hand.add(pm.get(entity).deck.remove(pm.get(entity).deck.size() - 1));
    }

    // From hand to table
    public void AddCardToTable(Entity entity, int index) {
        pm.get(entity).cardsOnTable.add(pm.get(entity).hand.remove(index));
    }

    public void addRectangleToCard(Entity entity, int index) {
        
    }

    // Returns given card in table
    public Entity getCardOnTable(Entity entity , int index) {
        return pm.get(entity).cardsOnTable.get(index);
    }

    public void removeCardOnTable(Entity entity , int index) {
        pm.get(entity).cardsOnTable.remove(index);
    }

    public String getPlayerId(Entity entity) {
        return pm.get(entity).id;
    }

    public void SetPlayerId(Entity entity, String id) {
        pm.get(entity).id = id;
    }

    public int getPlayerPowerPoints(Entity entity) {
        return pm.get(entity).powerPoints;
    }

    public void setPlayerPowerPoints(Entity entity, int points) {
        pm.get(entity).powerPoints = points;
    }

    public void takeDamage(Entity entity, int damage) {
        pm.get(entity).health = pm.get(entity).health - damage;
    }

    public int getHealth(Entity entity) {
        return pm.get(entity).health;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComp = pm.get(entity);
    }
}
