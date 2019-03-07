package me.snizzle.scrabble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ScrabbleCompPlayerNoBlank extends ScrabbleCompPlayer {

    public ScrabbleCompPlayerNoBlank(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules) {
        super(board, tileBag, rules);
    }


    public ScrabbleCompPlayerNoBlank(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayAsString, ScrabbleRules rules) {
        super(board, tileBag, trayAsString, rules);
    }

    @Override
    void findBestMove() {

        //i will get a list of points representing tiles on the board
        ArrayList<ScrabbleBoardPoint> playedPoints = board.getListPlayedTiles();



        //HORIZONTAL CALCULATIONNS
        //I will take this list and filter them to only include the points I should use to check for horizontal moves
        HashSet<ScrabbleBoardPoint> hPointsToCheck = filterHPoints(playedPoints);

        for (ScrabbleBoardPoint p : hPointsToCheck) {
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
