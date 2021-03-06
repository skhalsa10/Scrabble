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

        this.letter = Character.toUpperCase(letter);
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
        ScrabbleTile c = new ScrabbleTile(this.readTile(),this.getPoints());
        return c;
    }


    /**
     * this is a better definition of equal for this object
     * @param o object to compare to.
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        //null check
        if(o == null) {
            return false;
        }

        //reference check
        if (o == this) {
            return true;
        }

        //instance check
        if(!(o instanceof ScrabbleTile)){
            return false;
        }

        ScrabbleTile tile = (ScrabbleTile)o;

        //valueCheck
        return (this.letter == tile.readTile() && this.points == tile.getPoints());
    }

    /**
     * hopefully this hashcode is good enough.
     * @return my hashcode
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        return prime*(result+letter+points);
    }


}
