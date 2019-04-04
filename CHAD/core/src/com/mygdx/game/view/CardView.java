package com.mygdx.game.view;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.TextureComponent;
import com.mygdx.game.model.screens.utils.Assets;


public class CardView {

    private ShapeRenderer shapeRenderer;
    private Texture Maintexture;
    private Texture attackIconTexture;
    private Texture healthIconTexture;
    private Texture greenRect;
    private Texture blackRect;
    private BitmapFont font;
    private Entity cardEntity;

    // Paths to static textures for all cards.
    private String pathToAttackIcon = Assets.pathToAttackIcon;
    private String pathToHealtchIcon = Assets.pathToHealtchIcon;
    private String pathToGreenRect = Assets.pathToGreenRect;
    private String pathToBlackRect = Assets.pathToBlackRect;



    //Fixed size for cards
    public static final int cardWidth = 200;
    public static final int cardHeight = 250;
    private int fontOffset = 35;

    private int attackPower;
    private int health;
    private boolean selected;
    private boolean sleeping;



/*
    public CardView(String pathToImageFile, int attackPower, int health, Entity cardEntity) {
        shapeRenderer = new ShapeRenderer();
        Maintexture = new Texture(Gdx.files.local(pathToImageFile));
        attackIconTexture = new Texture(Gdx.files.local(pathToAttackIcon));
        healthIconTexture = new Texture(Gdx.files.local(pathToHealtchIcon));
        greenRect = new Texture(Gdx.files.local(pathToGreenRect));
        blackRect = new Texture(Gdx.files.local(pathToBlackRect));
        font = new BitmapFont();
        //this.activated = false;
        this.attackPower = attackPower;
        this.health = health;
        this.cardEntity = cardEntity;
    }
*/


    private ComponentMapper<TextureComponent> tm;
    private ComponentMapper<CardStatsComponent> cm;


    public CardView() {

        tm = ComponentMapper.getFor(TextureComponent.class);
        cm = ComponentMapper.getFor(CardStatsComponent.class);



        shapeRenderer = new ShapeRenderer();
        attackIconTexture = Assets.getTexture(pathToAttackIcon);
        healthIconTexture = Assets.getTexture(pathToHealtchIcon);
        greenRect = Assets.getTexture(pathToGreenRect);
        blackRect = Assets.getTexture(pathToBlackRect);
        font = new BitmapFont();

    }



    public void draw(SpriteBatch batch, float xCoord, float yCoord, Entity cardEntity) {

        selected = cm.get(cardEntity).selected;
        String attackPowerString = String.valueOf(cm.get(cardEntity).attackPower);
        String healthString = String.valueOf(cm.get(cardEntity).health);
        sleeping = cm.get(cardEntity).sleeping;
        Maintexture = tm.get(cardEntity).texture;


        if (sleeping) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BROWN);
            shapeRenderer.rect(xCoord, yCoord, cardWidth, cardHeight);
            shapeRenderer.end();
            batch.begin();

            if (selected) {
                batch.draw(greenRect, xCoord, yCoord);
            }
            else {
                batch.draw(blackRect, xCoord, yCoord);
            }

            batch.draw(Maintexture, xCoord, yCoord);

            batch.draw(attackIconTexture, xCoord, yCoord + cardHeight - attackIconTexture.getHeight() * 1.2f);
            batch.draw(healthIconTexture, xCoord + cardWidth - 1.8f * healthIconTexture.getWidth(), yCoord + cardHeight - healthIconTexture.getHeight());

            font.setColor(Color.BLACK);
            font.getData().setScale(2);

            font.draw(batch, attackPowerString, xCoord + fontOffset, yCoord + cardHeight * 0.9f);
            font.draw(batch, healthString, xCoord + cardWidth - fontOffset, yCoord + cardHeight * 0.9f);
            batch.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(255, 255, 255, 0.5f));
            shapeRenderer.rect(xCoord, yCoord, cardWidth, cardHeight);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            batch.begin();
            font.draw(batch, "Sleeping", xCoord + cardWidth/2 - fontOffset, yCoord + cardHeight/2);
            batch.end();

        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BROWN);
            shapeRenderer.rect(xCoord, yCoord, cardWidth, cardHeight);
            shapeRenderer.end();
            batch.begin();

            if (selected) {
                batch.draw(greenRect, xCoord, yCoord);
            }
            else {
                batch.draw(blackRect, xCoord, yCoord);
            }

            batch.draw(Maintexture, xCoord, yCoord);

            batch.draw(attackIconTexture, xCoord, yCoord + cardHeight - attackIconTexture.getHeight() * 1.2f);
            batch.draw(healthIconTexture, xCoord + cardWidth - 1.8f * healthIconTexture.getWidth(), yCoord + cardHeight - healthIconTexture.getHeight());

            font.setColor(Color.BLACK);
            font.getData().setScale(2);

            font.draw(batch, attackPowerString, xCoord + fontOffset, yCoord + cardHeight * 0.9f);
            font.draw(batch, healthString, xCoord + cardWidth - fontOffset, yCoord + cardHeight * 0.9f);
            batch.end();
        }







    }

}
