package com.mygdx.game.model.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.model.components.BoardComponent;
import com.mygdx.game.model.components.CardPowerComponent;
import com.mygdx.game.model.components.CardStatsComponent;
import com.mygdx.game.model.components.TextureComponent;

import com.badlogic.ashley.core.Entity;

public class BoardSystem extends IteratingSystem {

    private static final Family family = Family.all(BoardComponent.class, TextureComponent.class,
                                                    CardPowerComponent.class, CardStatsComponent.class).get();
    private Engine engine;
    private ComponentMapper<BoardComponent> bm;
    private ComponentMapper<TextureComponent> tm;
    private ComponentMapper<CardPowerComponent> cpm;
    private ComponentMapper<CardStatsComponent> csm;



    public BoardSystem() {

        super(family);

        bm = ComponentMapper.getFor(BoardComponent.class);
        tm = ComponentMapper.getFor(TextureComponent.class);
        cpm = ComponentMapper.getFor(CardPowerComponent.class);
        csm = ComponentMapper.getFor(CardStatsComponent.class);
    }

    public void addPlayer(Entity board, Entity player, int index) {
        if (!family.matches(entity)) return;
        BoardComponent bc = bm.get(board);
        switch (index) {
            case 1:
                bc.playerOne = player;
                break;
            case 2:
                bc.playerTwo = player;
                break;
            default:
                System.err.println("IndexError: Player index must be 1 or 2");
        }
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        BoardComponent boardComp = bm.get(entity);
        CardPowerComponent cardPow = cpm.get(entity);
        CardStatsComponent cardStat = csm.get(entity);

        // lister over hva som ligger på bordet og hva som ligger på hånda.
        //4 første er dine egne, 4 siste er de andre sine kort
        // 5 plasser på korta i hendene.
        // trenger liv fra motspiller
        // oppdatere ditt eget liv
        // Knapper
               // Vis hånd
                 //       Skjul hånd
        //end turn
        // sette playere
        // sjekke når korta på brettet har null liv, fjernes fra lista.
        // tegne et rektangel rundt kortet, så man vet hvilket som blir valgt. 


    }

    public void drawCard(Entity entity) {
        /*
        TODO: add functionality for drawing a new card.
         */
    }

    public void chooseCard(Entity entity) {
        /*
        TODO: add functionality for choosing a card to attack with
         */
    }



}
