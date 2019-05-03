package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.List;

public class PlayerComponent implements Component {
    public String id;
    public String name;
    public List<Entity> hand = new ArrayList<Entity>();
    public List<Entity> cardsOnTable = new ArrayList<Entity>();
    public int powerPoints;
    public List<Entity> deck = new ArrayList<Entity>();
    public int health = 30;
    public int manaPoints = 0;
    public boolean isYourTurn = true;
    public int yourTurnNumber = 0;
}
