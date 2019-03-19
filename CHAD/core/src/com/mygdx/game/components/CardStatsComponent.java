package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

/*
 * Component of the base card. Has a
 * Power, a given health and a given cost.
 * Each card also has a special power, here a CardPowerComponent.
 */

public class CardStatsComponent implements Component {
    public float power;
    public float health;
    public float cost;
    public CardPowerComponent cardPower;
}
