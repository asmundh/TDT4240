package com.mygdx.game.view;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CardGame;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.PlayerComponent;

import java.util.ArrayList;
import java.util.List;


public class BoardView {


    private CardGame game;
    private Texture deck;
    private Texture background;
    private Texture enemyRect;
    private Texture handRect;
    private ShapeRenderer shapeRenderer;
    private String pathToHealtchIcon = "textures/healthIcon.png";
    private Texture healthIconTexture;
    private BitmapFont font;
    private boolean showHand;
    private ComponentMapper<BoardComponent> bm;
    private ComponentMapper<PlayerComponent> pm;
    private Entity boardEntity;
    private int enemyHealth;
    private Entity player;
    private Entity enemyPlayer;



    private List<Entity> friendlyCardsOnBoardEntity;
    private List<Entity> enemyCardsOnBoardEntity;
    private List<Entity> cardsInHandEntity;

    private List<CardView> friendlyCardsOnBoard;
    private List<CardView> enemyCardsOnBoard;
    private List<CardView> cardsInHand;


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

    public BoardView(Entity boardEntity) {
        this.boardEntity = boardEntity;
        this.deck = new Texture(Gdx.files.local("textures/cardBackside.png"));
        this.background = new Texture(Gdx.files.local("textures/background.png"));
        this.enemyRect = new Texture(Gdx.files.local("textures/enemy.png"));
        this.handRect = new Texture(Gdx.files.local("textures/handRect.png"));
        this.healthIconTexture = new Texture(Gdx.files.local(pathToHealtchIcon));
        this.font = new BitmapFont();


        shapeRenderer = new ShapeRenderer();

        bm = ComponentMapper.getFor(BoardComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);

        player = bm.get(boardEntity).playerOne;
        enemyPlayer = bm.get(boardEntity).playerTwo;

        showHand = bm.get(boardEntity).showHand;
        enemyHealth = pm.get(enemyPlayer).health;
        friendlyCardsOnBoardEntity = pm.get(player).cardsOnTable;
        enemyCardsOnBoardEntity = pm.get(enemyPlayer).cardsOnTable;
        cardsInHandEntity = pm.get(player).hand;

        friendlyCardsOnBoard = new ArrayList<CardView>();
        enemyCardsOnBoard = new ArrayList<CardView>();
        cardsInHand = new ArrayList<CardView>();

    }




    public void draw(SpriteBatch batch) {

        player = bm.get(boardEntity).playerOne;
        enemyPlayer = bm.get(boardEntity).playerTwo;
        showHand = bm.get(boardEntity).showHand;
        enemyHealth = pm.get(enemyPlayer).health;
        friendlyCardsOnBoardEntity = pm.get(player).cardsOnTable;
        enemyCardsOnBoardEntity = pm.get(enemyPlayer).cardsOnTable;
        cardsInHandEntity = pm.get(player).hand;

        cardsInHand.clear();
        enemyCardsOnBoard.clear();
        friendlyCardsOnBoard.clear();


        for (int i = 0; i < friendlyCardsOnBoardEntity.size(); i++) {
            friendlyCardsOnBoard.add(new CardView(friendlyCardsOnBoardEntity.get(i)));
        }
        for (int i = 0; i < enemyCardsOnBoardEntity.size(); i++) {
            enemyCardsOnBoard.add(new CardView(enemyCardsOnBoardEntity.get(i)));
        }
        for (int i = 0; i < cardsInHandEntity.size(); i++) {
            cardsInHand.add(new CardView(cardsInHandEntity.get(i)));
        }


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

        String healthString = String.valueOf(enemyHealth);

        font.draw(batch, healthString, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - enemyRect.getHeight() + font.getLineHeight());
        batch.end();





        //Drawing of friendly cards on board
        for (int i = 0; i < this.friendlyCardsOnBoard.size(); i++) {
            float x = this.boardPositions[i].x;
            float y = this.boardPositions[i].y;
            this.friendlyCardsOnBoard.get(i).draw(batch, x, y);
        }
        //Drawing of enemy cards on board
        for (int i = 0; i < this.enemyCardsOnBoard.size(); i++) {
            float x = this.boardPositions[i + 4].x;
            float y = this.boardPositions[i + 4].y;
            this.enemyCardsOnBoard.get(i).draw(batch, x, y);
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
