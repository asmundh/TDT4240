package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.screens.utils.Assets;

public class TutorialScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private SpriteBatch sb;
    private Stage stage;
    private Texture background;
    private Engine engine;

    private Button stepForwardBtn;
    private Button stepBackBtn;

    private TextArea textBox1;
    private TextArea textBox2;
    private TextArea textBox3;

    private int tutCounter; // Keeps track of which tutorial box to display

    public TutorialScreen(CardGame game, Engine engine){ // Constructor initializes background and runs create()
        super();
        this.game = game;
        sb = game.batch;
        background = Assets.getTexture(Assets.boardBackground);
        this.engine = engine;
        tutCounter = 0;

        create(); // Run create on one-time operations
    }

    public void create() {
        stage = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(stage); // Set inputs to be handled by the stage

        // Initialize a  button using texture from Assets, make is transformable and set the origin to the middle
        // TODO: Implement correct asset for button
        final Button backBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.backBtn))));
        backBtn.setTransform(true);
        backBtn.setSize(241, 87);
        backBtn.setOrigin(backBtn.getWidth()/2, backBtn.getHeight()/2);

        // Create back and forward buttons for the tutorial
        // TODO: Implement correct asset for button
        stepForwardBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.backBtn))));
        stepForwardBtn.setTransform(true);
        stepForwardBtn.setSize(200, 200);
        stepForwardBtn.setOrigin(stepForwardBtn.getWidth()/2, stepForwardBtn.getHeight()/2);

        // TODO: Implement correct asset for button
        stepBackBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.backBtn))));
        stepBackBtn.setTransform(true);
        stepBackBtn.setSize(200, 200);
        stepBackBtn.setOrigin(stepBackBtn.getWidth()/2, stepBackBtn.getHeight()/2);

        // Input functionality for buttons
        backBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, engine));
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
        stepBackBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                System.out.println(tutCounter);
                tutCounter -= 2;
                showTutorialStep();
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stepBackBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                stepBackBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });
        stepForwardBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                System.out.println(tutCounter);
                showTutorialStep();
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stepForwardBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                stepForwardBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        backBtn.setPosition(50, game.getHeight() - stepBackBtn.getHeight() - 50);
        stepBackBtn.setPosition(game.getWidth()-450, 50);
        stepForwardBtn.setPosition(game.getWidth()- 200, 50);

        createTutorialBoxes();

        stage.addActor(backBtn);
        stage.addActor(stepBackBtn);
        stage.addActor(stepForwardBtn);

        showTutorialStep();
    }

    private void createTutorialBoxes(){
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin.getFont("font").getData().setScale(2.0f,2.0f);

        textBox1 = new TextArea("VElkommen til dette spill? Hva gjør man. Følg med!", skin);
        textBox1.setSize(400f, 200f);
        textBox1.setPosition(100f, 100f);
        textBox1.setDisabled(true); // Disables input into box
        textBox1.setVisible(false);

        textBox2 = new TextArea("Kortet her er dine korts :O", skin);
        textBox2.setSize(400f, 200f);
        textBox2.setPosition(500f, 400f);
        textBox2.setDisabled(true);
        textBox2.setVisible(false);

        textBox3 = new TextArea("Collect first. THen hoist cardet and go for the destroy!", skin);
        textBox3.setSize(400f, 200f);
        textBox3.setPosition(500f, 400f);
        textBox3.setDisabled(true);
        textBox3.setVisible(false);

        stage.addActor(textBox1);
        stage.addActor(textBox2);
        stage.addActor(textBox3);
    }

    private void showTutorialStep(){ // Method for handeling which part of the tutorial to display
        switch (tutCounter){
            case -1:
                tutCounter = 0;
                break;
            case 0:
                textBox1.setVisible(true);
                textBox2.setVisible(false);
                stepBackBtn.setVisible(false);
                break;
            case 1: // In general each case needs to hide previous and next box, and show current
                stepBackBtn.setVisible(true);
                textBox1.setVisible(false);
                textBox2.setVisible(true);
                textBox3.setVisible(false);
                break;
            case 2:
                textBox2.setVisible(false);
                textBox3.setVisible(true);
                //textBox4.setVisible(false);
                break;
            default:
                break;
        }
        tutCounter++;
    }

    @Override
    public void update(float dt) {
        handleInput();
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin(); // Draw elements to Sprite Batch
        sb.draw(background, 0,0, CardGame.WIDTH, CardGame.HEIGHT);
        sb.end();

        stage.draw(); // Draw elements to Stage
    }

    @Override
    public void handleInput() { // Not used
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
