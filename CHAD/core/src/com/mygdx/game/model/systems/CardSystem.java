package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardStatsComponent;


public class CardSystem extends IteratingSystem {

    private static final Family family = Family.all(CardStatsComponent.class, CardPowerComponent.class).get();
    private ComponentMapper<CardStatsComponent> csm;
    private ComponentMapper<CardPowerComponent> cpm;

    public CardSystem() {
        super(family);
        cpm = ComponentMapper.getFor(CardPowerComponent.class);
        csm = ComponentMapper.getFor(CardStatsComponent.class);

        /*
        TODO: Add functionality that updates components as the cards move. Position etc.
         */
    }

    public String getPowerEffectText(Entity entity) {
        return cpm.get(entity).powerEffectText;
    }

    public void setPowerEffectText(Entity entity, String powerEffectText) {
        cpm.get(entity).powerEffectText = powerEffectText;
    }

    public int getPowerSize(Entity entity) {
        return cpm.get(entity).powerSize;
    }

    public void setPowerSize(Entity entity, int powerSize) {
        cpm.get(entity).powerSize = powerSize;
    }

    public int getPowerType(Entity entity) {
        return cpm.get(entity).powerType;
    }

    public void setPowerType(Entity entity, int powerType) {
        cpm.get(entity).powerType = powerType;
    }

    public void setPowerName(Entity entity, String name) {
        cpm.get(entity).powerName = name;
    }

    public String getPowerName(Entity entity) {
        return cpm.get(entity).powerName;
    }

    public int getHealth(Entity entity) {
        return csm.get(entity).health;
    }

    public void setHealth(Entity entity ,int health) {
        csm.get(entity).health = health;
    }

    public int getCost(Entity entity) {
        return csm.get(entity).cost;
    }

    public void setCost(Entity entity, int cost) {
        csm.get(entity).cost = cost;
    }

    public void takeDamage(Entity entity, int damage) {
        if (getHealth(entity) - damage <= 0) {
            setHealth(entity, 0);
        }
        else {
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
            case (CardPowerComponent.SELF_HEALTH_INCREASE):
                int healthIncrease = getPowerSize(entity);
                int currentHealth = getHealth(entity);
                setHealth(entity, currentHealth + healthIncrease);
            default:
                return;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
