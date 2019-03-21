package com.mygdx.game.model.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.game.model.screens.utils.Assets;

public class MenuScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private SpriteBatch sb;
    private Stage stage;
    private Texture background;

    public MenuScreen(CardGame game){ // Constructor initializes background and runs create()
        super();
        this.game = game;
        sb = game.batch;
        background = Assets.getTexture(Assets.menuBG);

        create(); // Run create on one-time operations
    }

    public void create() {
        stage = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(stage); // Set inputs to be handled by the stage

        // Initialize a  button using texture from Assets, first is up-texture, second is down. Set the size, make is transformable and set the origin to the middle
        final Button playBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.playBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.playBtn))));
        playBtn.setTransform(true);
        playBtn.setSize(300, 108);
        playBtn.setOrigin(playBtn.getWidth()/2, playBtn.getHeight()/2);

        // Initialize a  button using texture from Assets. Set the size, make is transformable and set the origin to the middle
        final Button setBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.settingBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.settingBtn))));
        setBtn.setTransform(true);
        setBtn.setSize(300, 108);
        setBtn.setOrigin(setBtn.getWidth()/2, setBtn.getHeight()/2);

        playBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                playBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });
        setBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Clicked motherfucker! " + x);
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                setBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        Table menuTable = new Table(); // Table containing the buttons on the screen
        menuTable.add(playBtn).pad(10);
        menuTable.getCell(playBtn).height(108).width(300);
        menuTable.row();
        menuTable.add(setBtn);
        menuTable.getCell(setBtn).height(108).width(300);
        menuTable.setFillParent(true);
        menuTable.moveBy(0,-200);

        stage.addActor(menuTable); // Add the table containing the buttons to the stage
    }

    @Override
    public void update(float dt) { // Only thing we're checking for is if user presses button
        handleInput();
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void draw() { // Draws sprite batch
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin(); // Draw elements to Sprite Batch
        sb.draw(background, 0,0, CardGame.WIDTH, CardGame.HEIGHT);
        sb.end();

        stage.draw(); // Draw elements to Stage
    }

    @Override
    public void handleInput() { // Not used
        if (Gdx.input.isTouched()) {

        }
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
}
