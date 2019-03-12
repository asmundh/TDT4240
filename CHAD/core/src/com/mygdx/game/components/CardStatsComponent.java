package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class CardStatsComponent implements Component {
    public float power;
    public float health;
    public float cost;
    public CardPowerComponent cardPower;
}
