package me.snizzle.scrabble;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class defines the scrabble Player. I made this abstract because the computer is pretty different
 *  for the testing as we are giving it certain trays and boards to test against
 */
public abstract class ScrabblePlayer {
    protected ScrabbleTileTray tileTray;
    protected ScrabbleTileBag tileBag;
    protected ScrabbleBoard board;
    protected ScrabbleRules rules;

    protected HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove;


    public ScrabblePlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules){
        this.rules = rules;
        this.board = board;
        this.tileBag = tileBag;
        this.tileTray = new ScrabbleTileTray(tileBag);
    }
    public ScrabblePlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayFileName, ScrabbleRules rules){
        this.rules = rules;
        this.board = board;
        this.tileBag = tileBag;
        this.tileTray = new ScrabbleTileTray(trayFileName);

    }

    //TODO i might want to add a parameter here that takes some parameter representing the human player
    //TODO move imported from the GUI
    public abstract void takeTurn();

    public void cacheMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        this.currentMove = move;
    }

    /**
     * this function checks to see if the cached move is valid on the board
     * and if tiles are in tile tray
     * @return
     */
    public boolean checkCachedMoveValid(){
        //TODO check if move is valid on board
        //TODO check to see if hand contains tiles
        if(!tileTray.contains(new ArrayList<>(currentMove.values()))){
            return false;
        }

        if(!board.validMove(currentMove, rules)){
            return false;
        }
        return true;
    }

    /**
     * this will assume the move is valid remove the tiles and play them on the board.
     * @return true on success
     */
    public boolean approveMove(){
        if(tileTray.remove(new ArrayList<>(currentMove.values())))
        return board.placeTiles(currentMove);

    }
}
