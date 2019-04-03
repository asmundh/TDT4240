package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class BoardComponent implements Component {
    public Entity playerOne;
    public Entity playerTwo;
    public boolean showHand;
    public boolean turn;
    public Entity lastCardClicked; // Siste kortet som ble trykket på. For å holde styr på om du trykker på ditt kort
    // for første gang, eller om du skal angripe en motstander, eller om du skal trykke på et av dine andre kort

}

