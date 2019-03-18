package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComponentInputMap;

public class GenericBoardComponent {

    public Texture boardTexture;
    public Player playerOne;
    public Player playerTwo;
    public List<Card> handOne = new ArrayList();
    public List<Card> handTwo = new ArrayList();
    public Card cardChosenOne;
    public Card cardChosenTwo;
    public List<Card> deckOne = new ArrayList();
    public List<Card> deckTwo = new ArrayList();
}
