package com.mygdx.game.model;

import com.badlogic.ashley.core.Entity;

import java.util.List;


public class GameStateObject {

    public int playerHealth;
    public int enemyHealth;
    public List<List> playerBoard;
    public List<List> enemyBoard;

    @Override
    public String toString() {

        String gamestate = "";
        gamestate = gamestate + playerHealth + "#" + enemyHealth + "#";

        for (int i = 0; i < playerBoard.size(); i++) {
            gamestate = gamestate + playerBoard.get(i).get(0) + "i";
            gamestate = gamestate + playerBoard.get(i).get(1) + "h";
            gamestate = gamestate + playerBoard.get(i).get(2) + "a";

            gamestate = gamestate + "c";
        }
        gamestate = gamestate + "#";

        for (int i = 0; i < enemyBoard.size(); i++) {
            gamestate = gamestate + enemyBoard.get(i).get(0) + "i";
            gamestate = gamestate + enemyBoard.get(i).get(1) + "h";
            gamestate = gamestate + enemyBoard.get(i).get(2) + "a";

            gamestate = gamestate + "c";
        }
        gamestate = gamestate + "#";
        return gamestate;
    }
}


