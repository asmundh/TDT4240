package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.TextureComponent;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.view.BoardView;

import java.util.List;

public class BoardSystem extends IteratingSystem {

    private static final Family family = Family.all(BoardComponent.class, TextureComponent.class).get();
    private ComponentMapper<BoardComponent> bm;
    private ComponentMapper<TextureComponent> tm;



    public BoardSystem() {

        super(family);

        bm = ComponentMapper.getFor(BoardComponent.class);
        tm = ComponentMapper.getFor(TextureComponent.class);
    }

    public void addPlayer(Entity board, List<Entity> players) {
        if (!family.matches(board)){
            return;
        }
        BoardComponent bc = bm.get(board);

        bc.playerOne = players.get(0);
        bc.playerTwo = players.get(1);
    }

    public boolean getShowHand(Entity boardEntity) {
        return bm.get(boardEntity).showHand;
    }

    public void changeShowHand(Entity boardEntity) {
        bm.get(boardEntity).showHand = !bm.get(boardEntity).showHand;
    }


    @Override
    protected void processEntity(Entity entity, float dt) {
        BoardComponent boardComp = bm.get(entity);

    }

    public void turnSwitcher(Entity entity) {
        bm.get(entity).turn = !bm.get(entity).turn;
    }


    public void cardChosen(Entity board, Entity lastCardClicked) {
        bm.get(board).lastCardClicked = lastCardClicked;
    }

    public Entity getPreviouslyClickedCard(Entity board) {
        return bm.get(board).lastCardClicked;

    }


}
