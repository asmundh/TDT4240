package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.model.components.TextureComponent;
import com.mygdx.game.model.components.TransformComponent;

import java.util.Comparator;

    public class RenderingSystem extends SortedIteratingSystem {

        private ImmutableArray<Entity> entities;

        private SpriteBatch batch;
        private OrthographicCamera camera;

        private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
        private ComponentMapper<TextureComponent> vm = ComponentMapper.getFor(TextureComponent.class);

        public RenderingSystem () {
            super();
            batch = new SpriteBatch();
        }

        @Override
        public void addedToEngine (Engine engine) {
            entities = engine.getEntitiesFor(Family.all(TransformComponent.class, TextureComponent.class).get());
        }

        @Override
        public void removedFromEngine (Engine engine) {

        }

        @Override
        public void update (float deltaTime) {
            TransformComponent transCom;
            TextureComponent tex;

            camera.update();

            batch.begin();

            for (int i = 0; i < entities.size(); ++i) {
                Entity e = entities.get(i);

                transCom = pm.get(e);
                tex = vm.get(e);

                batch.draw(tex.region, transCom.position.x, transCom.position.y);
            }

            batch.end();
        }

        @Override
        protected void processEntity(Entity entity, float deltaTime) {

        }
    }
