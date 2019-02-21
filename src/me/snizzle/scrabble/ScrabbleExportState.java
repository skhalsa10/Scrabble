package me.snizzle.scrabble;

import me.snizzle.game.LogicExportState;

import java.util.HashMap;

//TODO will need a boolean if move failed to give feedback
public class ScrabbleExportState implements LogicExportState {
    private boolean userMoveFailed;
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> playedTiles;
    //TODO hint data?
    //TODO score data

    public ScrabbleExportState(){
        userMoveFailed = false;
        playedTiles = new HashMap<>();
    }

    public void reset(){
        userMoveFailed = false;
    }

    public void setUserMoveFailed() {
        userMoveFailed = true;
    }

    public boolean isUserMoveFailed() {
        return userMoveFailed;
    }
}
