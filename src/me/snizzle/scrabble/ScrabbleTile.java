package me.snizzle.scrabble;

public class ScrabbleTile {
    private final char letter;
    private final int points;

    public ScrabbleTile(char letter, int value){
        this.letter = letter;
        this.points = value;
    }

    public char readTile(){
        return letter;
    }

    public int getPoints(){
        return points;
    }
}
