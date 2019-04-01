package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.model.components.PlayerComponent;

public class PlayerSystem extends IteratingSystem {
    private static final Family family = Family.all(PlayerComponent.class).get();
    private ComponentMapper<PlayerComponent> pc;

    public PlayerSystem() {
        super(family);

        pc = ComponentMapper.getFor(PlayerComponent.class);
    }

    // Take last card from deck, and add to hand list
    public void pickFromDeck(Entity entity) {
        pc.get(entity).hand.add(pc.get(entity).deck.remove(pc.get(entity).deck.size() - 1));
    }

    // From hand to table
    public void AddCardToTable(Entity entity, int index) {
        pc.get(entity).cardsOnTable.add(pc.get(entity).hand.remove(index));
    }

    // Returns given card in table
    public Entity getCardOnTable(Entity entity , int index) {
        return pc.get(entity).cardsOnTable.get(index);
    }

    public void removeCardOnTable(Entity entity , int index) {
        pc.get(entity).cardsOnTable.remove(index);
    }

    public String getPlayerId(Entity entity) {
        return pc.get(entity).id;
    }

    public void SetPlayerId(Entity entity, String id) {
        pc.get(entity).id = id;
    }

    public int getPlayerPowerPoints(Entity entity) {
        return pc.get(entity).powerPoints;
    }

    public void setPlayerPowerPoints(Entity entity, int points) {
        pc.get(entity).powerPoints = points;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComp = pc.get(entity);
    }
}
