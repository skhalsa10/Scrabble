package me.snizzle.scrabble;

import java.util.HashMap;

/**
 * this is a small class that keeps track of a score. Very Basic may not even need to be a class
 *
 * @author Siri Khalsa
 * @version 1
 */
public class ScrabbleScore {

    private int score;

    public ScrabbleScore(){
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    /**
     * if given a rules object and a board  and current move we can even calculate the score
     */
    public int calcScore(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove,
                         ScrabbleRules rules, ScrabbleBoard board){
        return rules.calcScore(currentMove, board);
    }

    public void calcAndAddScore(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove,
                                ScrabbleRules rules, ScrabbleBoard board){
        score += rules.calcScore(currentMove, board);
    }
}
