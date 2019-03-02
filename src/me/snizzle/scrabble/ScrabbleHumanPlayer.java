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

        //assumes the move is cached to play.
        if(!checkCachedMoveValid()){
            return false;
        }

        score.addToScore(rules.calcScore(currentMove,board));

        if(!approveMove()){
            return false;
        }
        System.out.println("passed Approve Move");

        tileTray.fillTray();
        return true;
    }
}
