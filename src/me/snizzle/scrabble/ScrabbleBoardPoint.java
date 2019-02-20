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
}
