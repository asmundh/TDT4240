package com.mygdx.game.model.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.screens.utils.Assets;

public class ConfirmationScreen extends ScreenAdapter implements ScreenInterface {

    private String confirmationText;
    private CardGame game;
    private Engine engine;
    private SpriteBatch sb;
    private Stage confirmationStage;
    private BitmapFont font;
    private Sound btnClick;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private GlyphLayout glyphLayout;


    public ConfirmationScreen(CardGame game, Engine engine, String confirmationText) {
        super();
        this.game = game;
        this.engine = engine;
        this.confirmationText = confirmationText;
        this.sb = game.batch;
        this.btnClick = Assets.getSound(Assets.btnClick);

        create();
    }

    @Override
    public void create() {
        confirmationStage = new Stage(new ScreenViewport()); // Create stage used by buttons
        Gdx.input.setInputProcessor(confirmationStage); // Set inputs to be handled by the stage

        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto/Roboto-Bold.ttf"));
        this.parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        font = generator.generateFont(parameter);
        generator.dispose();
        font.setColor(Color.WHITE);

        // Initialize a  button using texture from Assets, first is up-texture, second is down. Set the size, make is transformable and set the origin to the middle
        final Button yesBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.yesBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.yesBtn))));
        yesBtn.setTransform(true);
        yesBtn.setSize(yesBtn.getWidth(), yesBtn.getHeight());
        yesBtn.setOrigin(yesBtn.getWidth()/2, yesBtn.getHeight()/2);

        // Initialize a  button using texture from Assets. Set the size, make is transformable and set the origin to the middle
        final Button noBtn = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.noBtn))), new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.noBtn))));
        noBtn.setTransform(true);
        noBtn.setSize(noBtn.getWidth(), noBtn.getHeight());
        noBtn.setOrigin(noBtn.getWidth()/2, noBtn.getHeight()/2);

        yesBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                this.game.androidInterface.endMatch();
                game.setScreen(new MenuScreen(game, engine));
                btnClick.play();
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                yesBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                yesBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        noBtn.addListener(new ClickListener() {
            @Override // Fires when the user lets go of the button
            public void clicked(InputEvent event, float x, float y) {
                btnClick.play();
                // TODO: Find way to change back to current game screen.
                game.setScreen(new GameScreen(game, engine));
            }

            @Override // Fires when the button is pressed down
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                noBtn.addAction(Actions.scaleTo(0.95f, 0.95f, 0.1f));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override // Fires when the button is released
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                noBtn.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        Table menuTable = new Table(); // Table containing the buttons on the screen
        menuTable.add(yesBtn).pad(10);
        menuTable.getCell(yesBtn).height(yesBtn.getHeight()).width(yesBtn.getWidth());
        menuTable.add(noBtn);
        menuTable.getCell(noBtn).height(noBtn.getHeight()).width(noBtn.getWidth());
        menuTable.setFillParent(true);
        menuTable.moveBy(0,-50);

        confirmationStage.addActor(menuTable); // Add the table containing the buttons to the stage

    }

    @Override
    public void update(float dt) {
        confirmationStage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.35f, 0.34f, 0.32f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, confirmationText, Color.WHITE, Gdx.graphics.getWidth(), Align.center, true);

        sb.begin(); // Draw elements to Sprite Batch
        font.draw(sb, glyphLayout, 0, Gdx.graphics.getHeight()/2 + 300);
        sb.end();

        confirmationStage.draw(); // Draw elements to Stage
    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
