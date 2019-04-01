package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;

/*
 * Component of the base card. Has a
 * Power, a given health and a given cost.
 * Each card also has a special power, here a CardPowerComponent.
 */

public class CardStatsComponent implements Component {
    public int attackPower;
    public int health;
    public int cost;
    public CardPowerComponent cardPower;
}
