package com.mygdx.game.controller;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardStatsComponent;

/*
This is the system that updates the components in the card entity.
It is also the bridge to get data from the components to the views.
 */

public class CardSystem extends IteratingSystem {

    private static final Family family = Family.all(CardStatsComponent.class, CardPowerComponent.class).get();
    private ComponentMapper<CardStatsComponent> csm;
    private ComponentMapper<CardPowerComponent> cpm;


    public CardSystem() {
        super(family);
        cpm = ComponentMapper.getFor(CardPowerComponent.class);
        csm = ComponentMapper.getFor(CardStatsComponent.class);
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

    public void takeDamage(Entity entity, int damage) {
        System.out.println("took damage");
        if (getHealth(entity) - damage <= 0) {
            setHealth(entity, 0);
        }
        else {
            setHealth(entity, getHealth(entity) - damage);
        }
    }

    public void deployCard(Entity entity) {
        csm.get(entity).sleeping = true;
    }

    public int getAttackPower(Entity attackingEntity) {
        return csm.get(attackingEntity).attackPower;
    }

    public void dealDamage(Entity attackingEntity, Entity entityBeingAttacked) {
        int damageToDeal = this.getAttackPower(attackingEntity);
        takeDamage(entityBeingAttacked, damageToDeal);
        csm.get(attackingEntity).sleeping = true;
    }

    public void retaliate(Entity attackingEntity, Entity entityBeingAttacked) {
        int damageToDeal = this.getAttackPower(attackingEntity);
        takeDamage(entityBeingAttacked, damageToDeal);
    }

    public boolean isSleeping(Entity cardEntity) {
        return csm.get(cardEntity).sleeping;
    }

    public int getId(Entity cardEntity) {
        return csm.get(cardEntity).id;
    }


    public void setSleeping(Entity cardEntity, boolean value) {
        csm.get(cardEntity).sleeping = value;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    public void updateSelected(Entity entity) {
        if (entity != null) {
            csm.get(entity).selected = !csm.get(entity).selected;

        }
    }
}
