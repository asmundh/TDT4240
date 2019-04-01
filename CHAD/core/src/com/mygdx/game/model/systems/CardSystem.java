package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardStatsComponent;


public class CardSystem {

    private ComponentMapper<CardPowerComponent> cpc;
    private ComponentMapper<CardStatsComponent> csc;

    /*private Family family = Family.all(CardStatsComponent.class, CardPowerComponent.class).get();
    private ComponentMapper<CardStatsComponent, CardPowerComponent> cm; */

    public CardSystem() {
        //super(family);
        cpc = ComponentMapper.getFor(CardPowerComponent.class);
        csc = ComponentMapper.getFor(CardStatsComponent.class);

        /*
        TODO: Add functionality that updates components as the cards move. Position etc.
         */
    }

    public String getPowerEffectText(Entity entity) {
        return cpc.get(entity).powerEffectText;
    }

    public void setPowerEffectText(Entity entity, String powerEffectText) {
        cpc.get(entity).powerEffectText = powerEffectText;
    }

    public int getPowerSize(Entity entity) {
        return cpc.get(entity).powerSize;
    }

    public void setPowerSize(Entity entity, int powerSize) {
        cpc.get(entity).powerSize = powerSize;
    }

    public int getPowerType(Entity entity) {
        return cpc.get(entity).powerType;
    }

    public void setPowerType(Entity entity, int powerType) {
        cpc.get(entity).powerType = powerType;
    }

    public void setPowerName(Entity entity, String name) {
        cpc.get(entity).powerName = name;
    }

    public String getPowerName(Entity entity) {
        return cpc.get(entity).powerName;
    }

    public void setPower(Entity entity, int damageIncrease) {
        int prev = csc.get(entity).attackPower;
        csc.get(entity).attackPower = prev + damageIncrease;
    }


    public int getHealth(Entity entity) {
        return csc.get(entity).health;
    }

    public void setHealth(Entity entity ,int health) {
        csc.get(entity).health = health;
    }

    public int getCost(Entity entity) {
        return csc.get(entity).cost;
    }

    public void setCost(Entity entity, int cost) {
        csc.get(entity).cost = cost;
    }

    public void takeDamage(Entity entity, int damage) {
        if (getHealth(entity) - damage <= 0) {
            setHealth(entity, 0);
        } else if (getHealth(entity) - damage > 0) {
            setHealth(entity, getHealth(entity) - damage);
        }
    }

    public void dealDamage(Entity entity, int damage) {
        takeDamage(entity, damage);
    }


    public void activatePower(Entity entity) {
        switch(getPowerType(entity)) {
            case (CardPowerComponent.SELF_DAMAGE_INCREASE):
                int damageIncrease = getPowerSize(entity);
                setPower(entity, damageIncrease);
            case (CardPowerComponent.SELF_HEALTH_INCREASE):
                int healthIncrease = getPowerSize(entity);
                int currentHealth = getHealth(entity);
                setHealth(entity, currentHealth + healthIncrease);
            default:
                return;
        }
    }

    // TODO: Method for take damage
    // TODO: Dealing damage
    // TODO: Activating power (Depending on power type (bost, helth, ... (Ref card power comp)))
    // TODO: Dead card ? 

    /*@Override
    public void processEntity(Entity entity) {
        //TODO Get entity,make actions, like move card, attack initated etc.

        //See Bobsystem in Ashley - superjumper for reference :
        // https://github.com/dsaltares/ashley-superjumper/blob/master/core/src/com/siondream/superjumper/systems/BobSystem.java


    }*/

    /*private void dealDamage(Entity entity, int damage) {
        int healthBeforeAttack = entity.getComponent(CardStatsComponent.health);
        if ((healthBeforeAttack - damage) < 0) {

        }
    }*/
}
