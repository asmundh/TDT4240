package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class CardView {

    private ShapeRenderer shapeRenderer;
    private Texture Maintexture;
    private Texture attackIconTexture;
    private Texture healthIconTexture;
    private Texture greenRect;
    private Texture blackRect;
    private BitmapFont font;

    // Paths to static textures for all cards.
    private String pathToAttackIcon = "textures/attackIcon.png";
    private String pathToHealtchIcon = "textures/healthIcon.png";
    private String pathToGreenRect = "textures/greenRect.png";
    private String pathToBlackRect = "textures/blackRect.png";


    //Fixed size for cards
    private int cardWidth = 200;
    private int cardHeight = 250;
    private int fontOffset = 35;

    private int attackPower;
    private int health;
    private boolean activated;



    public CardView(String pathToImageFile, int attackPower, int health) {
        shapeRenderer = new ShapeRenderer();
        Maintexture = new Texture(Gdx.files.internal(pathToImageFile));
        attackIconTexture = new Texture(Gdx.files.internal(pathToAttackIcon));
        healthIconTexture = new Texture(Gdx.files.internal(pathToHealtchIcon));
        greenRect = new Texture(Gdx.files.internal(pathToGreenRect));
        blackRect = new Texture(Gdx.files.internal(pathToBlackRect));
        font = new BitmapFont();
        this.activated = false;
        this.attackPower = attackPower;
        this.health = health;
    }

    public void draw(SpriteBatch batch, float xCoord, float yCoord) {

        String attackPowerString = String.valueOf(attackPower);
        String healthString = String.valueOf(health);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(xCoord, yCoord, cardWidth, cardHeight);
        shapeRenderer.end();

        batch.begin();

        if (activated) {
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


    public void decreaseHealth(int amount) {
        this.health -= amount;
    }

    public void increaseHealth(int amount) {
        this.health += amount;
    }

    public void decreaseAttack(int amount) {
        this.attackPower -= amount;
    }

    public void increaseAttack(int amount) {
        this.attackPower += amount;
    }


}
