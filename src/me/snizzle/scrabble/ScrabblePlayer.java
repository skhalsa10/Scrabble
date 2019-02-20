package me.snizzle.scrabble;

/**
 * this class defines the scrabble Player. I made this abstract because the computer is pretty different
 *  for the testing as we are giving it certain trays and boards to test against
 */
public abstract class ScrabblePlayer {
    protected ScrabbleTileTray tileTray;
    protected ScrabbleTileBag tileBag;
    protected ScrabbleBoard board;


    public ScrabblePlayer(ScrabbleBoard board, ScrabbleTileBag tileBag){
        this.board = board;
        this.tileBag = tileBag;
        this.tileTray = new ScrabbleTileTray(tileBag);
    }
    public ScrabblePlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayFileName){
        this.board = board;
        this.tileBag = tileBag;
        this.tileTray = new ScrabbleTileTray(trayFileName);

    }

    //TODO i might want to add a parameter here that takes some parameter representing the human player
    //TODO move imported from the GUI
    public abstract void takeTurn();
}
