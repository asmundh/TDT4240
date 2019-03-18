package com.mygdx.game.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class EngineSetup {

    public Engine engine = new Engine();

    public Entity player = new Entity();

    public EngineSetup() {
        Component playerComp = new PlayerComponent();
        player.add(playerComp);

        engine.addEntity(player);

    }
}
