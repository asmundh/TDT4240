package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.List;

public class DeckComponent implements Component {
    public List<Entity> deck = new ArrayList();
}
