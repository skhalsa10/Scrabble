package me.snizzle.scrabble;

/**
 * This class defines a scrabble tile. it will be an original scrabble tile that contains a character
 * of the alphabet and a point value associated with the tile
 * @author Siri Khalsa
 * @version 1
 */
public class ScrabbleTile {
    private final char letter;
    private final int points;

    /**
     * this constructs a new Tile
     * @param letter the letter of the Tile
     * @param value the point value associated with the letter
     */
    public ScrabbleTile(char letter, int value){
        this.letter = letter;
        this.points = value;
    }

    /**
     * @return the letter
     */
    public char readTile(){
        return letter;
    }

    /**
     *
     * @return returns the point value
     */
    public int getPoints(){
        return points;
    }
}
