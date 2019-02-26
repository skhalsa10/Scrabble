package me.snizzle.scrabble;

public class ScrabbleHumanPlayer extends ScrabblePlayer{

    public ScrabbleHumanPlayer(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules) {
        super(board, tileBag, rules);

    }

    /**
     * this method needs a cached move to work and assumes it is cached
     * @return true if cached move is valid the move is played successfully.
     * will return false otherwise or if currentMove is null
     */
    @Override
    public boolean takeTurn() {
        if(currentMove == null){return false;}
        System.out.println("taking turn");
        //assumes the move is cached to play.
        if(!checkCachedMoveValid()){
            return false;
        }
        System.out.println("move valid");
        if(!approveMove()){
            return false;
        }
        System.out.println("move approved");
        tileTray.fillTray();
        return true;
    }
}
