package com.mygdx.game.view;

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
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.Assets;

public class TutorialScreen extends ScreenAdapter implements ScreenInterface {

    private CardGame game;
    private SpriteBatch sb;
    private Stage stage;
    private Texture background;
    private Engine engine;

    private Button stepForwardBtn;
    private Button stepBackBtn;

    private Sound btnSound;

    private TextArea textBox1;
    private TextArea textBox2;
    private TextArea textBox3;
    private TextArea textBox4;
    private TextArea textBox5;
    private TextArea textBox6;
    private TextArea textBox7;
    private TextArea textBox8;
    private TextArea textBox9;
    private TextArea textBox10;
    private TextArea textBox11;
    private TextArea textBox12;
    private TextArea textBox13;

    private int tutCounter; // Keeps track of which tutorial box to display

    public TutorialScreen(CardGame game, Engine engine){ // Constructor initializes background and runs create()
        super();
        this.game = game;
        sb = game.batch;
        background = Assets.getTexture(Assets.boardBackground);
        this.engine = engine;
        tutCounter = 0;
        this.btnSound = Assets.getSound(Assets.btnClick);

        create(); // Run create on one-time operations
    }

    public void create() {
        stage = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(stage); // Set inputs to be handled by the stage

        // Initialize a  button using texture from Assets, make is transformable and set the origin to the middle
        final Button backBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.backBtn))));
        backBtn.setTransform(true);
        backBtn.setSize(241, 87);
        backBtn.setOrigin(backBtn.getWidth()/2, backBtn.getHeight()/2);

        // Create back and forward buttons for the tutorial
        stepForwardBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.stepForwardBtn))));
        stepForwardBtn.setTransform(true);
        stepForwardBtn.setSize(200, 200);
        stepForwardBtn.setOrigin(stepForwardBtn.getWidth()/2, stepForwardBtn.getHeight()/2);

        stepBackBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.stepBackBtn))));
        stepBackBtn.setTransform(true);
        stepBackBtn.setSize(200, 200);
        stepBackBtn.setOrigin(stepBackBtn.getWidth()/2, stepBackBtn.getHeight()/2);

        // Input functionality for buttons
        backBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                btnSound.play();
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

                btnSound.play();
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

                btnSound.play();
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

        backBtn.setPosition(40, game.getHeight() - backBtn.getHeight() - 20);
        stepBackBtn.setPosition(game.getWidth()-500, 50);
        stepForwardBtn.setPosition(game.getWidth()- 250, 50);

        createTutorialBoxes();

        stage.addActor(backBtn);
        stage.addActor(stepBackBtn);
        stage.addActor(stepForwardBtn);

        showTutorialStep();
    }

    private void createTutorialBoxes(){
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin.getFont("font").getData().setScale(2.0f,2.0f);

        textBox1 = new TextArea("Welcome to CHAD. This tutorial will take you through the basic and get you going in no time! Use the button to navigate through the tutorial.", skin);
        textBox1.setSize(600f, 300f);
        textBox1.setPosition(game.getWidth()/2 - textBox1.getWidth()/2, game.getHeight()/2 - textBox1.getHeight()/2 + 100);
        textBox1.setDisabled(true); // Disables input into box
        textBox1.setVisible(false);

        textBox2 = new TextArea("The highlighted area is your hand. These are the cards you can hoist to the table to later attack with.", skin);
        textBox2.setSize(400f, 200f);
        textBox2.setPosition(500f, 400f);
        textBox2.setDisabled(true);
        textBox2.setVisible(false);

        textBox3 = new TextArea("This area shows you your health and Mana points. When your health reaches 0 you lose. Mana points are used to send cards to the table and you accumulate some each round.", skin);
        textBox3.setSize(400f, 330f);
        textBox3.setPosition(1050f, 700f);
        textBox3.setDisabled(true);
        textBox3.setVisible(false);

        textBox4 = new TextArea("To attack with a card, it needs to be placed on the table. How much this costs is indicated by the number in the bottom left corner.", skin);
        textBox4.setSize(400f, 250f);
        textBox4.setPosition(600f, 400f);
        textBox4.setDisabled(true);
        textBox4.setVisible(false);

        textBox5 = new TextArea("To send the card to the table, first select it by tapping it. This will highlight the card. Then tap it again to confirm. ", skin);
        textBox5.setSize(400f, 200f);
        textBox5.setPosition(600f, 400f);
        textBox5.setDisabled(true);
        textBox5.setVisible(false);

        textBox6 = new TextArea("The card is now placed on the table. To show the tabel, tap the \"Hide hand\" button.", skin);
        textBox6.setSize(400f, 200f);
        textBox6.setPosition(700f, 400f);
        textBox6.setDisabled(true);
        textBox6.setVisible(false);

        textBox7 = new TextArea("As you can see, the card is now in a sleeping state. This means that the card is unavailable this round, but will wake up and be ready to fight in the next!", skin);
        textBox7.setSize(400f, 250f);
        textBox7.setPosition(700f, 400f);
        textBox7.setDisabled(true);
        textBox7.setVisible(false);

        textBox8 = new TextArea("Your enemy and your own cards are displayed here. In addition to the mana cost, each card has an attack value in the top left corner and a health value in the top right." +
                "When attacking an opponents card or the enemy itself, its health will decrease with the attack score of your card.", skin);
        textBox8.setSize(400f, 480f);
        textBox8.setPosition(1050f, 300f);
        textBox8.setDisabled(true);
        textBox8.setVisible(false);

        textBox9 = new TextArea("You win if you reduce your opponents health to 0. " +
                "This means you have to prioritize if you want to attack a card or the player directly." +
                "To attack, select a card and then tap the card you want to attack or the enemy.", skin);
        textBox9.setSize(400f, 480f);
        textBox9.setPosition(1050, 300f);
        textBox9.setDisabled(true);
        textBox9.setVisible(false);

        textBox10 = new TextArea("Each round, you'll get one new card that you can hoist", skin);
        textBox10.setSize(400f, 200f);
        textBox10.setPosition(600f, 150f);
        textBox10.setDisabled(true);
        textBox10.setVisible(false);

        textBox11 = new TextArea("When you have finished your turn, press the \"End turn\" and wait for your opponent to finish.", skin);
        textBox11.setSize(400f, 200f);
        textBox11.setPosition(1100f, 500f);
        textBox11.setDisabled(true);
        textBox11.setVisible(false);

        textBox12 = new TextArea("When your opponent has finished, you'll receive a \"A match was updated\" notification. When this happens, press the \"Load Turn\" button!", skin);
        textBox12.setSize(400f, 200f);
        textBox12.setPosition(game.getWidth()/2 - textBox12.getWidth()/2, 400f);
        textBox12.setDisabled(true);
        textBox12.setVisible(false);

        textBox13 = new TextArea("Now you know the basics of how to play CHAD," +
                " the game all about collecting cards, hoisting them and going for the attack! CHAD can be a pretty serious game, " +
                "but remember: The most important ting is to GLHF!", skin);
        textBox13.setSize(600f, 300f);
        textBox13.setPosition(game.getWidth()/2 - textBox1.getWidth()/2, game.getHeight()/2 - textBox1.getHeight()/2 + 100);
        textBox13.setDisabled(true);
        textBox13.setVisible(false);

        stage.addActor(textBox1);
        stage.addActor(textBox2);
        stage.addActor(textBox3);
        stage.addActor(textBox4);
        stage.addActor(textBox5);
        stage.addActor(textBox6);
        stage.addActor(textBox7);
        stage.addActor(textBox8);
        stage.addActor(textBox9);
        stage.addActor(textBox10);
        stage.addActor(textBox11);
        stage.addActor(textBox12);
        stage.addActor(textBox13);
    }

    private void showTutorialStep(){ // Method for handeling which part of the tutorial to display
        switch (tutCounter){
            case -1:
                tutCounter = 0;
                break;
            case 0:
                background = Assets.getTexture(Assets.gameStart);
                textBox1.setVisible(true);
                textBox2.setVisible(false);
                stepBackBtn.setVisible(false);
                break;
            case 1: // In general each case needs to hide previous and next box, and show current
                background = Assets.getTexture(Assets.gameHand);
                stepBackBtn.setVisible(true);
                textBox1.setVisible(false);
                textBox2.setVisible(true);
                textBox3.setVisible(false);
                break;
            case 2:
                background = Assets.getTexture(Assets.gamePoints);
                textBox2.setVisible(false);
                textBox3.setVisible(true);
                textBox4.setVisible(false);
                break;
            case 3:
                background = Assets.getTexture(Assets.gameCard);
                textBox3.setVisible(false);
                textBox4.setVisible(true);
                textBox5.setVisible(false);
                break;
            case 4:
                background = Assets.getTexture(Assets.gameCardSelect);
                textBox4.setVisible(false);
                textBox5.setVisible(true);
                textBox6.setVisible(false);
                break;
            case 5:
                background = Assets.getTexture(Assets.gameSentToBoard);
                textBox5.setVisible(false);
                textBox6.setVisible(true);
                textBox7.setVisible(false);
                break;
            case 6:
                background = Assets.getTexture(Assets.gameCardSleep);
                textBox6.setVisible(false);
                textBox7.setVisible(true);
                textBox8.setVisible(false);
                break;
            case 7:
                background = Assets.getTexture(Assets.gameEnemyYou);
                textBox7.setVisible(false);
                textBox8.setVisible(true);
                textBox9.setVisible(false);
                break;
            case 8:
                background = Assets.getTexture(Assets.gameEnemyYou);
                textBox8.setVisible(false);
                textBox9.setVisible(true);
                textBox10.setVisible(false);
                break;
            case 9:
                background = Assets.getTexture(Assets.gameNewCard);
                textBox9.setVisible(false);
                textBox10.setVisible(true);
                textBox11.setVisible(false);
                break;
            case 10:
                background = Assets.getTexture(Assets.gameEndTurn);
                textBox10.setVisible(false);
                textBox11.setVisible(true);
                textBox12.setVisible(false);
                break;
            case 11:
                background = Assets.getTexture(Assets.gameLoadTurn);
                stepForwardBtn.setVisible(true);
                textBox11.setVisible(false);
                textBox12.setVisible(true);
                textBox13.setVisible(false);
                break;
            case 12:
                background = Assets.getTexture(Assets.gameEnd);
                stepForwardBtn.setVisible(false);
                textBox12.setVisible(false);
                textBox13.setVisible(true);
                break;
            default:
                tutCounter--;
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