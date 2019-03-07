package me.snizzle.scrabble;

import me.snizzle.scrabble.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


public class ScrabbleCompPlayer20  extends ScrabbleCompPlayer {
    Random random = new Random();

    public ScrabbleCompPlayer20(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules) {
        super(board, tileBag, rules);
    }

    public ScrabbleCompPlayer20(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayAsString, ScrabbleRules rules) {
        super(board, tileBag, trayAsString, rules);
    }


    @Override
    void findBestMove() {

        int temp = random.nextInt(10);

        //i will get a list of points representing tiles on the board
        ArrayList<ScrabbleBoardPoint> playedPoints = board.getListPlayedTiles();



        //HORIZONTAL CALCULATIONNS
        //I will take this list and filter them to only include the points I should use to check for horizontal moves
        HashSet<ScrabbleBoardPoint> hPointsToCheck = filterHPoints(playedPoints);

        for (ScrabbleBoardPoint p : hPointsToCheck) {
            if(temp%2 == 0 || bestMoveScore >=18){
                break;
            }
            ArrayList<ScrabbleTile> tiles = tileTray.getCopy();
            if (tiles.contains(new ScrabbleTile(' ', 0))) {

                ArrayList<ScrabbleTile> tiles2 = copyTilesWithNoT(tiles, new ScrabbleTile(' ', 0));
                tiles2.add(new ScrabbleTile('e', 0));
                getMaxHMove(p, getHWord(p, new HashMap<>()), new HashMap<>(), tiles2);

            }
            else {

                getMaxHMove(p, getHWord(p, new HashMap<>()), new HashMap<>(), tiles);
            }
        }

        //VERTICAL CALCULATIONS
        HashSet<ScrabbleBoardPoint> vPointsToCheck = filterVPoints(playedPoints);

        for (ScrabbleBoardPoint p : vPointsToCheck) {
            if(bestMoveScore >=18){
                break;
            }
            ArrayList<ScrabbleTile> tiles = tileTray.getCopy();
            if (tiles.contains(new ScrabbleTile(' ', 0))) {

                ArrayList<ScrabbleTile> tiles2 = copyTilesWithNoT(tiles, new ScrabbleTile(' ', 0));
                tiles2.add(new ScrabbleTile('e', 0));
                getMaxVMove(p, getVWord(p, new HashMap<>()), new HashMap<>(), tiles2);

            }
            else {
                getMaxVMove(p, getVWord(p, new HashMap<>()), new HashMap<>(), tiles);
            }

        }


    }
}
