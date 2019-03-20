package com.mygdx.game.model.components;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import com.badlogic.ashley.core.Component;

public class BoardComponent implements Component {

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

