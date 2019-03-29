package com.mygdx.game.model.components;

/*
 * This component describes the power of a card.
 * A power has a name and a text describing it.
 * It also has a size, e.g. +4,
 * and a type, e.g. damage.
 */

import com.badlogic.ashley.core.Component;

public class CardPowerComponent implements Component {
    public String powerName;
    public String powerEffectText;
    public int powerSize;
    public int powerType;

    public static final int SELF_DAMAGE_INCREASE = 0;
    public static final int SELF_HEALTH_INCREASE = 1;
    public static final int COST_REDUCTION = 2;
    public static final int TEAM_DAMAGE_INCREASE = 3;
    public static final int TEAM_HEALTH_INCREASE = 4;

}
