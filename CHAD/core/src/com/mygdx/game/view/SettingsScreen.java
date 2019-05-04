package com.mygdx.game.view;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.Assets;
import com.mygdx.game.model.screens.utils.MusicStateManager;

/*
This is the screen that is displayed when the player clicks on Settings.
 */

public class SettingsScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private Engine engine;
    private SpriteBatch sb;

    private Stage settings;
    private Sound btnClick;
    private MusicStateManager musicStateManager;
    private MenuScreen menuScreen;

    public SettingsScreen(CardGame game, Engine engine) {
        super();
        this.game = game;
        sb = game.batch;
        create();
        this.engine = engine;

        this.btnClick = Assets.getSound(Assets.btnClick);
    }

    @Override
    public void create() {
        settings = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(settings); // Set inputs to be handled by the stage

        // Initialize a  button using texture from Assets, first is up-texture, second is down. Set the size, make is transformable and set the origin to the middle
        final Button backBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.backBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.backBtn))));
        backBtn.setTransform(true);
        backBtn.setSize(backBtn.getWidth(), backBtn.getHeight());
        backBtn.setOrigin(backBtn.getWidth()/2, backBtn.getHeight()/2);

        final Button muteBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.mute))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.mute))),
                new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.play))));
        muteBtn.setTransform(true);
        muteBtn.setChecked(!game.musicStateManager.getMusicState());
        muteBtn.setSize(muteBtn.getWidth(), muteBtn.getHeight());
        muteBtn.setOrigin(muteBtn.getWidth()/2, muteBtn.getHeight()/2);

        // Initialize a  button using texture from Assets. Set the size, make is transformable and set the origin to the middle
        final Button exitBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.exit_gameBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.exit_gameBtn))));
        exitBtn.setTransform(true);
        exitBtn.setSize(exitBtn.getWidth(), exitBtn.getHeight());
        exitBtn.setOrigin(exitBtn.getWidth()/2, exitBtn.getHeight()/2);

        backBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, engine));
                btnClick.play();
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                backBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                backBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        muteBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                game.musicStateManager.changeState();
                btnClick.play();
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                muteBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                muteBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                btnClick.play();
                Gdx.app.exit();
                System.exit(0);
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                exitBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        Table menuTable = new Table(); // Table containing the buttons on the screen
        menuTable.add(backBtn).pad(10);
        menuTable.getCell(backBtn).height(backBtn.getHeight()).width(backBtn.getWidth());
        menuTable.row();
        menuTable.add(muteBtn).pad(10);
        menuTable.getCell(muteBtn).height(muteBtn.getHeight()).width(muteBtn.getWidth());
        menuTable.row();
        menuTable.add(exitBtn).pad(10);
        menuTable.getCell(exitBtn).height(exitBtn.getHeight()).width(exitBtn.getWidth());
        menuTable.setFillParent(true);
        menuTable.moveBy(0,0);

        settings.addActor(menuTable); // Add the table containing the buttons to the stage
    }

    @Override
    public void update(float dt) {
        handleInput();
        settings.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.35f, 0.34f, 0.32f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin(); // Draw elements to Sprite Batch
        sb.end();

        settings.draw(); // Draw elements to Stage
    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void handleInput() {

    }
}
