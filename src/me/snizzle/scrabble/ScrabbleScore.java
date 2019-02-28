package me.snizzle.scrabble;

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
     * if given a rules object and a board we can even calculate the score
     */
    public void calcScore( ScrabbleRules rules, ScrabbleBoard board){
        //TODO call the calculate score in rules
    }
}
