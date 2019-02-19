package me.snizzle.scrabble;

public class ScrabbleCompPlayer extends ScrabblePlayer {

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag) {
        super(board, tileBag);
    }

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayFileName){
        super(board, tileBag, trayFileName);
    }

    @Override
    public void takeTurn() {

    }
}
