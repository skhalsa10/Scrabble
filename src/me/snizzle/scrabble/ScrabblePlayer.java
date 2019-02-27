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

    //this is used as a cached move that is being held for testing.
    protected HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove;


    /**
     * this creates a default player with a default tray.
     * @param board
     * @param tileBag
     * @param rules
     */
    public ScrabblePlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules){
        this.rules = rules;
        this.board = board;
        this.tileBag = tileBag;
        this.tileTray = new ScrabbleTileTray(tileBag);
    }

    /**
     * should only be used for testing the computer player for assignment. it is pointless otherwise.
     * it needs to be able to parse a file to set up the state.
     * @param board
     * @param tileBag
     * @param trayFileName
     * @param rules
     */
    public ScrabblePlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayFileName, ScrabbleRules rules){
        this.rules = rules;
        this.board = board;
        this.tileBag = tileBag;
        this.tileTray = new ScrabbleTileTray(trayFileName);

    }

    //TODO i might want to add a parameter here that takes some parameter representing the human player
    //TODO move imported from the GUI
    public abstract boolean takeTurn();

    public void cacheMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> move) {
        this.currentMove = move;
    }

    /**
     * this function checks to see if the cached move is valid on the board
     * and if tiles are in tile tray
     * @return
     */
    public boolean checkCachedMoveValid(){
        if(!tileTray.contains(new ArrayList<>(currentMove.values()))){
            return false;
        }
        if (!rules.isMoveValid(currentMove, board)){
            return false;
        }

        /*if(!board.validMove(currentMove, rules)){
            return false;
        }*/
        return true;
    }

    /**
     * this will assume the move is valid remove the tiles from the tray
     * and plays them on the board. It will also delete the reset currentMove to null.
     * @return true on success
     */
    public boolean approveMove(){
        //TODO should I checkCachedMovevalid here?
        if(!tileTray.remove(new ArrayList<>(currentMove.values()))){
            return false;
        }
        if(!board.placeTiles(currentMove)){
            return false;
        }

        currentMove = null;
        return true;

    }

    /**
     *
     * @return the tray of this player as a ScrabbleTile[]
     */
    public ScrabbleTile[] tileTrayToArray(){
        return tileTray.toArray();
    }


}
