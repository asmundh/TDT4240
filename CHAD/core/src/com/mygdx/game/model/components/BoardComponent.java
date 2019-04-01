package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class BoardComponent implements Component {
    public Entity playerOne;
    public Entity playerTwo;
    public boolean showHand;

}

