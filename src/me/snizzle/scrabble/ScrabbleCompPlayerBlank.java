package me.snizzle.scrabble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ScrabbleCompPlayerBlank extends ScrabbleCompPlayer {
    public ScrabbleCompPlayerBlank(ScrabbleBoard board, ScrabbleTileBag tileBag, ScrabbleRules rules) {
        super(board, tileBag, rules);
    }

    public ScrabbleCompPlayerBlank(ScrabbleBoard board, ScrabbleTileBag tileBag, String trayAsString, ScrabbleRules rules) {
        super(board, tileBag, trayAsString, rules);
    }

    @Override
    void findBestMove() {
        {
            //i will get a list of points representing tiles on the board
            ArrayList<ScrabbleBoardPoint> playedPoints = board.getListPlayedTiles();
            //testedWords = new Trie();


            //HORIZONTAL CALCULATIONNS
            //I will take this list and filter them to only include the points I should use to check for horizontal moves
            HashSet<ScrabbleBoardPoint> hPointsToCheck = filterHPoints(playedPoints);

            for (ScrabbleBoardPoint p:hPointsToCheck ) {
                ArrayList<ScrabbleTile> tiles = tileTray.getCopy();
                if(tiles.contains(new ScrabbleTile(' ',0))) {
                    for(char c = 'a';c<='z';c++) {
                        ArrayList<ScrabbleTile> tiles2 = copyTilesWithNoT(tiles, new ScrabbleTile(' ', 0));
                        tiles2.add(new ScrabbleTile(c, 0));
                        getMaxHMove(p, getHWord(p, new HashMap<>()), new HashMap<>(), tiles2);
                        //tiles.remove(new ScrabbleTile(c,0));
                        //tiles.add(new ScrabbleTile((char)(c+1),0));

                    }
                    continue;
                }

                getMaxHMove(p,getHWord(p,new HashMap<>()),new HashMap<>(),tiles);
            }

            //VERTICAL CALCULATIONS
            HashSet<ScrabbleBoardPoint> vPointsToCheck = filterVPoints(playedPoints);
            //HashSet<ScrabbleBoardPoint> vPointsToCheck = new HashSet<>();
            //vPointsToCheck.add(new ScrabbleBoardPoint(7,7));
            for (ScrabbleBoardPoint p:vPointsToCheck ) {
                ArrayList<ScrabbleTile> tiles = tileTray.getCopy();
                if(tiles.contains(new ScrabbleTile(' ',0))) {
                    for(char c = 'a';c<='z';c++) {
                        ArrayList<ScrabbleTile> tiles2 = copyTilesWithNoT(tiles, new ScrabbleTile(' ', 0));
                        tiles2.add(new ScrabbleTile(c, 0));
                        getMaxVMove(p, getVWord(p, new HashMap<>()), new HashMap<>(), tiles2);
                        //tiles.remove(new ScrabbleTile(c,0));
                        //tiles.add(new ScrabbleTile((char)(c+1),0));

                    }
                    continue;
                }

                getMaxVMove(p,getVWord(p,new HashMap<>()),new HashMap<>(),tiles);

            }

        }
    }
}
