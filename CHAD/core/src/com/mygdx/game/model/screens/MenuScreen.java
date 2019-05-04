package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Engine engine;
    private Music bgMusic;
    private Sound btnClick;
    private String userName;
    private BitmapFont font;
    private boolean isSignedIn = false;
    private boolean foundMatch = false; // flag used to display loading/looking for match
    private boolean flagLookingForMatch = false;
    private String lookingForMatch = "Looking for match";


    public MenuScreen(CardGame game, Engine engine){ // Constructor initializes background and runs create()
        super();
        this.game = game;
        sb = game.batch;
        background = Assets.getTexture(Assets.menuBG);
        this.engine = engine;

        this.flagLookingForMatch = false;
        this.foundMatch = false;

        this.bgMusic = Assets.getMusic(Assets.backgroundMusic);
        this.bgMusic.setVolume(0.3f);
        this.bgMusic.play();

        this.btnClick = Assets.getSound(Assets.btnClick);

        this.font = new BitmapFont();


        create(); // Run create on one-time operations
    }

    public void create() {
        System.out.println("Entered menuscreen");
        stage = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(stage); // Set inputs to be handled by the stage

        // Initialize a  button using texture from Assets, first is up-texture, second is down. Set the size, make is transformable and set the origin to the middle
        final Button playBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.playBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.playBtn))));
        playBtn.setTransform(true);
        playBtn.setSize(483, 174);
        playBtn.setOrigin(playBtn.getWidth()/2, playBtn.getHeight()/2);

        // Initialize a  button using texture from Assets. Set the size, make is transformable and set the origin to the middle
        final Button setBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.settingBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.settingBtn))));
        setBtn.setTransform(true);
        setBtn.setSize(619, 174);
        setBtn.setOrigin(setBtn.getWidth()/2, setBtn.getHeight()/2);

        // Initialize a sign-in button for google play services, only visible if not signed in
        final Button signInBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.signInButton))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.signInButton))));
        signInBtn.setTransform(true);
        signInBtn.setSize(619, 174);
        signInBtn.setOrigin(signInBtn.getWidth()/2, signInBtn.getHeight()/2);

        playBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            // Once button is clicked, the game should start looking for a match and display lookingForMatch
            public void clicked(InputEvent event, float x, float y) {
                game.androidInterface.createNewMatch();
                // hide playbutton
                playBtn.setVisible(false);
                // show looking for match
                flagLookingForMatch = true;
                System.out.println("Now looking for game...");
                btnClick.play();
                bgMusic.stop();
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
                game.androidInterface.changeView();
                game.setScreen(new SettingsScreen(game, engine));
                btnClick.play();
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

        signInBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                btnClick.play();
                game.androidInterface.manualSignIn();
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                signInBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                signInBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        Table menuTable = new Table(); // Table containing the buttons on the screen
        menuTable.add(playBtn).pad(10);
        menuTable.getCell(playBtn).height(174).width(483);
        menuTable.row();
        menuTable.add(setBtn);
        menuTable.getCell(setBtn).height(174).width(619);
        menuTable.setFillParent(true);
        menuTable.moveBy(0,-240);
        menuTable.row();
        menuTable.add(signInBtn).pad(10);
        menuTable.getCell(signInBtn).height(178).width(600);
        menuTable.setFillParent(true);

        stage.addActor(menuTable); // Add the table containing the buttons to the stage

        // This part fetches the displayname of the signed in user if possible
        this.isSignedIn = this.game.androidInterface.getIsSignedIn();
        if(this.isSignedIn){
            this.userName = this.game.androidInterface.getDisplayName();
            game.androidInterface.dismissAllMatches();
        }



    }

    @Override
    public void update(float dt) { // Only thing we're checking for is if user presses button

        // check if we have found an opponent
        if(game.androidInterface.getFoundOpponent()){
            System.out.println("Found opponent!");
            // this returned true, that means we have found a match
            foundMatch = true;
        }

        // if a match is found, then the game changes to GameScreen and change lookingforflag
        if(foundMatch) {
            flagLookingForMatch = false;
            foundMatch = false;
            System.out.println("MenuScreen - update(): setting a new GameScreen - because of foundMatch");
            game.setScreen(new GameScreen(game, engine));
        }

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

        sb.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        font.draw(sb, "Signed in as: " + userName, 10, Gdx.graphics.getHeight()*0.95f);
        sb.end();

        if(flagLookingForMatch){
            sb.begin();
            font.setColor(Color.WHITE);
            font.getData().setScale(4);
            font.draw(sb, lookingForMatch, Gdx.graphics.getWidth()/2-200, Gdx.graphics.getHeight()/2);
            sb.end();
        }

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
