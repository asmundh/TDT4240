package com.mygdx.game.model.components;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.Card;
import javax.swing.ComponentInputMap;


public class BoardComponent {

    public Texture boardTexture;
    public PlayerComponent playerOne;
    public PlayerComponent playerTwo;
    public List<Card> handOne = new ArrayList();
    public List<Card> handTwo = new ArrayList();
    public Card cardChosenOne;
    public Card cardChosenTwo;
    public List<Card> deckOne = new ArrayList();
    public List<Card> deckTwo = new ArrayList();
}

