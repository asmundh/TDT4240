package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

import javax.swing.ComponentInputMap;

public class GenericBoardComponent {

    public Texture boardTexture;
    public Player playerOne;
    public Player playerTwo;
    public List<Card> deckOne;
    public List<Card> deckTwo;
    public Card cardChosenOne;
    public Card cardChosenTwo;

}
