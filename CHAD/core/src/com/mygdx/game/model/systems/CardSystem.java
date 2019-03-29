package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.model.components.CardStatsComponent;

public class CardSystem {

    public CardSystem() {

        /*
        TODO: Add functionality that updates components as the cards move. Position etc.
         */
    }

    //@Override
    public void processEntity(Entity entity) {
        //TODO Get entity,make actions, like move card, attack initated etc.

        //See Bobsystem in Ashley - superjumper for reference :
        // https://github.com/dsaltares/ashley-superjumper/blob/master/core/src/com/siondream/superjumper/systems/BobSystem.java


    }

    private void dealDamage(Entity entity, int damage) {
        int healthBeforeAttack = entity.getComponent(CardStatsComponent.health);
        if ((healthBeforeAttack - damage) < 0) {

    }
}
