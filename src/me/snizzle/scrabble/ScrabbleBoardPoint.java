package me.snizzle.scrabble;

/**
 * this is a simple point designed to keeptrack of a location on the board for a tile
 */
public class ScrabbleBoardPoint {

    private int row;
    private int col;

    public ScrabbleBoardPoint(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

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
        if(!(o instanceof ScrabbleBoardPoint)){
            return false;
        }

        ScrabbleBoardPoint point = (ScrabbleBoardPoint)o;

        //valueCheck
        return (this.row == point.getRow() && this.col == point.getCol());
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        return prime*(result+row+col);
    }
}
