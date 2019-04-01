package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import java.util.List;

public class PlayerComponent implements Component {
    public String id;
    public String name;
    public List<Entity> hand ;
    public List<Entity> cardsOnTable;
    public int powerPoints;
    public List<Entity> deck;
    public int health;
}
