package me.snizzle.scrabble;

/**
 * //TODO I may want to convert this to an interface and implement versions that are static that
 * //TODO all it does is look up the value of a char based on a map of the type of scrabble?
 * This class defines a scrabble tile. it will be an original scrabble tile that contains a character
 * of the alphabet and a point value associated with the tile
 * @author Siri Khalsa
 * @version 1
 */
public class ScrabbleTile implements Cloneable{
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

    @Override
    protected ScrabbleTile clone(){
        ScrabbleTile c = null;
        try {
            c = (ScrabbleTile)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return c;
    }
}
