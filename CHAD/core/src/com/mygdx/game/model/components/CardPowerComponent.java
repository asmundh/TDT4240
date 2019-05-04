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

}
