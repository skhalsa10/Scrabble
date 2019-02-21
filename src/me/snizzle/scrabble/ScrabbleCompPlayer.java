package me.snizzle.scrabble;

public class ScrabbleCompPlayer extends ScrabblePlayer {

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules) {
        super(board, tileBag, rules);
    }

    public ScrabbleCompPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayFileName, ScrabbleRules rules){
        super(board, tileBag, trayFileName, rules);
    }

    @Override
    public void takeTurn() {
        //TODO need to right algorithm for taking a turn.
    }
}
