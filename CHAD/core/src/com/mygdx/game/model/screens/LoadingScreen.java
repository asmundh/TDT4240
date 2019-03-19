package com.mygdx.game.model.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.screens.utils.Assets;

public class LoadingScreen extends ScreenAdapter implements ScreenInterface, Screen {

    private CardGame game;
    private ShapeRenderer shapeRenderer;
    private float progress;
    private BitmapFont font = new BitmapFont();
    private Texture bgTex;

    public LoadingScreen(CardGame game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        bgTex = new Texture("splash.jpg");
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
            //game.setScreen(new SplashScreen(game));
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.32f, 0.78f, 0.95f, 1.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(bgTex, 0, 0);
        //font.draw(game.batch, "CHAD", game.getWidth() / 2, game.getHeight() / 2);
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.WHITE);
//        shapeRenderer.rect(0, 0, game.getWidth(), 11);

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
