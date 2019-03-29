package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CardGame;

import java.util.ArrayList;
import java.util.List;


public class BoardView {


    private CardGame game;
    private Texture deck;
    private Texture background;
    private Texture enemyRect;
    private  Texture handRect;
    private ShapeRenderer shapeRenderer;
    private String pathToHealtchIcon = "textures/healthIcon.png";
    private Texture healthIconTexture;
    private BitmapFont font;
    private boolean showHand;




    private List<CardView> cards = new ArrayList<>();
    private List<CardView> cardsInHand = new ArrayList<>();

    private Vector2[] boardPositions = {
            new Vector2((Gdx.graphics.getWidth() / 2) - (Gdx.graphics.getWidth() / 4), (Gdx.graphics.getHeight() / 2) - (Gdx.graphics.getHeight() / 4)),
            new Vector2((Gdx.graphics.getWidth() / 2) - 0.5f*(Gdx.graphics.getWidth() / 4), (Gdx.graphics.getHeight() / 2) - (Gdx.graphics.getHeight() / 4)),
            new Vector2((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (Gdx.graphics.getHeight() / 4)),
            new Vector2((Gdx.graphics.getWidth() / 2) + 0.5f*(Gdx.graphics.getWidth() / 4), (Gdx.graphics.getHeight() / 2) - (Gdx.graphics.getHeight() / 4)),
            new Vector2((Gdx.graphics.getWidth() / 2) - (Gdx.graphics.getWidth() / 4), Gdx.graphics.getHeight() / 2),
            new Vector2((Gdx.graphics.getWidth() / 2) - 0.5f*(Gdx.graphics.getWidth() / 4), Gdx.graphics.getHeight() / 2),
            new Vector2((Gdx.graphics.getWidth() / 2), Gdx.graphics.getHeight() / 2),
            new Vector2((Gdx.graphics.getWidth() / 2) + 0.5f*(Gdx.graphics.getWidth() / 4), (Gdx.graphics.getHeight() / 2)),
    };

    private Vector2[] handPositions = {
            new Vector2(Gdx.graphics.getWidth() / 6 + 50, 100),
            new Vector2(Gdx.graphics.getWidth() / 6 + 300, 100),
            new Vector2(Gdx.graphics.getWidth() / 6 + 550, 100),
            new Vector2(Gdx.graphics.getWidth() / 6 + 800, 100),
            new Vector2(Gdx.graphics.getWidth() / 6 + 1050, 100),
    };






    public BoardView(CardGame game) {
        this.game = game;
        this.deck = new Texture(Gdx.files.internal("textures/cardBackside.png"));
        this.background = new Texture(Gdx.files.internal("textures/background.png"));
        this.enemyRect = new Texture(Gdx.files.internal("textures/enemy.png"));
        this.handRect = new Texture(Gdx.files.internal("textures/handRect.png"));
        this.healthIconTexture = new Texture(Gdx.files.internal(pathToHealtchIcon));
        this.font = new BitmapFont();
        this.showHand = true;


        shapeRenderer = new ShapeRenderer();


    }

    public void addCardToBoard(CardView card) {
        this.cards.add(card);
    }

    public void removeCardOnBoard(CardView card) {
        this.cards.remove(card);
    }

    public void addCardToHand(CardView card) {
        this.cardsInHand.add(card);
    }

    public void removeCardInHand(CardView card) {
        this.cardsInHand.remove(card);
    }

    public void setShowHand(boolean bool) {
        this.showHand = bool;
    }

    public boolean getShowHand() {
        return this.showHand;
    }

    public void draw(SpriteBatch batch) {

        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();

        //Drawing of deck
        batch.begin();
        batch.draw(deck,10,  Gdx.graphics.getHeight() / 2 - deck.getHeight() / 2);
        batch.end();

        //Drawing of enemy
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - enemyRect.getWidth() / 2, Gdx.graphics.getHeight() - enemyRect.getHeight(), enemyRect.getWidth(), enemyRect.getHeight());
        shapeRenderer.end();

        batch.begin();
        batch.draw(enemyRect, Gdx.graphics.getWidth() / 2 - enemyRect.getWidth() / 2, Gdx.graphics.getHeight() - enemyRect.getHeight());
        batch.draw(healthIconTexture, Gdx.graphics.getWidth() / 2 - healthIconTexture.getWidth(), Gdx.graphics.getHeight() - enemyRect.getHeight() + 10);
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
        String healthString = String.valueOf(10); //hvordan få enemy health??
        font.draw(batch, healthString, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - enemyRect.getHeight() + font.getLineHeight());
        batch.end();





        //Drawing of cards on board
        for (int i = 0; i < this.cards.size(); i++) {
            float x = this.boardPositions[i].x;
            float y = this.boardPositions[i].y;
            this.cards.get(i).draw(batch, x, y);

        }


        if (showHand) {

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.LIGHT_GRAY);
            shapeRenderer.rect(Gdx.graphics.getWidth() / 6, 50, Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 3);
            shapeRenderer.end();
            batch.begin();
            batch.draw(handRect, Gdx.graphics.getWidth() / 6, 50);
            batch.end();

            for (int i = 0; i < this.cardsInHand.size(); i++) {
                float xHand = this.handPositions[i].x;
                float yHand = this.handPositions[i].y;
                this.cardsInHand.get(i).draw(batch, xHand, yHand);
            }


        }



    }















}
