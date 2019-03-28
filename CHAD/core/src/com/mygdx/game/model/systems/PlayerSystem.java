package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.model.components.PlayerComponent;

public class PlayerSystem extends IteratingSystem {
    private ComponentMapper<PlayerComponent> pl = ComponentMapper.getFor(PlayerComponent.class);
    private PlayerComponent player = new PlayerComponent();

    public PlayerSystem(Family family) {
        super(family.all(PlayerComponent.class).get());
    }

    // Take last card from deck, and add to hand list
    public void pickFromDeck() {
        player.hand.add(player.deck.remove(player.deck.size() - 1));
    }

    // From hand to table
    public void AddCardToTable(int index) {
        player.cardsOnTable.add(player.hand.remove(index));
    }

    // Returns given card in table
    public Entity getCardOnTable(int index) {
        return player.cardsOnTable.get(index);
    }

    public void removeCardOnTable(int index) {
        player.cardsOnTable.remove(index);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent player = pl.get(entity);
    }
}
