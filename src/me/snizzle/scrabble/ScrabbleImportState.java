package me.snizzle.scrabble;

import me.snizzle.game.LogicImportState;

import java.util.HashMap;

/**
 * this class represents the state that is returned by the gui.
 * it will be an array list that follows the following protocol:
 *
 * Hashmap<scrabbleBoardpoint, tiles> move
 */
public class ScrabbleImportState implements LogicImportState {
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> move;

    public ScrabbleImportState(){
        move = new HashMap<>();
    }

    public void setMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        this.move = move;
    }

    public HashMap<ScrabbleBoardPoint, ScrabbleTile> getMove() {
        return move;
    }
}
