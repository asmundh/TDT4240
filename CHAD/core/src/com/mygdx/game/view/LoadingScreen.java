package com.mygdx.game.view;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.Assets;

public class LoadingScreen extends ScreenAdapter implements ScreenInterface, Screen {

    private CardGame game;
    private Engine engine;
    private ShapeRenderer shapeRenderer;
    private float progress;
    private Texture bgTex;

    public LoadingScreen(CardGame game, Engine engine) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        bgTex = new Texture("splash.png");
        this.engine = engine;
    }

    @Override
    public void show() {
        this.progress = 0f;
        Assets.load();
    }

    @Override
    public void create() {}

    @Override
    public void update(float dt) {
        progress = MathUtils.lerp(progress, Assets.getProgress(), .1f);
        if (Assets.update() && progress >= Assets.getProgress() - 0.001f) {
            game.setScreen(new MenuScreen(game, engine));
        }
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.32f, 0.78f, 0.95f, 1.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(bgTex, 0, 0);
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, game.getHeight() - game.getHeight(),
                progress * (game.getWidth() - 0 * 2), 10);
        shapeRenderer.end();
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void dispose() {
        game.batch.dispose();
        shapeRenderer.dispose();
    }
}
