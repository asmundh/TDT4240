package com.mygdx.game.model.components;
import com.badlogic.ashley.core.Component;
/*
 * Stores what type of stat the power has an effect on.
 */

public class CardPowerTypeComponent implements Component {
    public static final int DAMAGE_INCREASE = 0;
    public static final int HEALTH_INCREASE = 1;
    public static final int COST_REDUCTION = 2;
}
