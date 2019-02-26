package me.snizzle.scrabble;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a scrabble board. depending on the config file. and the actions needed to place and remove tiles
 *
 * @author Siri Khalsa
 * @version 1
 */
public class ScrabbleBoard {
    private int[][] boardValues;
    private ScrabbleTile[][] boardTiles;
    private int boardSize;
    private ScrabbleRules rules;
    HashMap<ScrabbleBoardPoint, ScrabbleTile> cachedMove;

    /**
     * constructs the default board
     */
    public ScrabbleBoard(ScrabbleRules rules){
        this("resources/scrabble_board.txt", rules);
    }

    /**
     * this will construct a board based on the configs in the file
     * @param boardConfig
     */
    public ScrabbleBoard(String boardConfig, ScrabbleRules rules){
        this.rules = rules;
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(boardConfig));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String line = fileReader.readLine();
            boardSize = Integer.parseInt(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boardValues = new int[boardSize][boardSize];
        boardTiles = new ScrabbleTile[boardSize][boardSize];


        configureBoard(fileReader);
    }

    /**
     * this will initialize the board values
     * @param fileReader
     */
    private void configureBoard(BufferedReader fileReader) {
        //initialize the tiles to null
        for(int r = 0; r < boardSize; r++){
            for(int c = 0; c< boardSize; c++){
                boardTiles[r][c] = null;
            }
        }

        String line;
        int r = 0;
        int c;

        //initialize the board values
        try {
            while ((line = fileReader.readLine()) != null) {
                String[] parsed = line.split(" ");

                if(parsed.length != boardSize){throw new IOException();}

                for(c = 0; c< parsed.length;c++){
                   char word  = parsed[c].charAt(0);
                   char letter = parsed[c].charAt(1);
                   if(word != '.'){
                       boardValues[r][c] = Integer.parseInt(String.valueOf(word))+'w';
                   }else if (letter != '.'){
                       boardValues[r][c] = Integer.parseInt(String.valueOf(letter));
                   }else{
                       boardValues[r][c] = 0;
                   }
                }

                r++;
            }

            fileReader.close();

        }catch(IOException e){
            System.out.println("boardSize does not match length of split.");
            e.printStackTrace();
        }



    }

    /**
     * This function will add tiles to the given point. if a tile is already at the point
     * the function will return false
     *
     * @param moves a map of points to the tile that should be placed there
     * @return true if it successfully added the tile (can only add a tile to an empty spot)
     */
    public boolean placeTiles(HashMap<ScrabbleBoardPoint, ScrabbleTile> moves){
        if(moves == null){
            return true;
        }

        cachedMove = moves;

        for (ScrabbleBoardPoint p: moves.keySet()) {
            if(boardTiles[p.getRow()][p.getCol()] != null){
                return false;
            }
        }

        //add tiles now that we confirm there exist no tiles
        for (Map.Entry<ScrabbleBoardPoint, ScrabbleTile> entry : moves.entrySet()) {
            ScrabbleBoardPoint p = entry.getKey();
            ScrabbleTile t = entry.getValue();
            boardTiles[p.getRow()][p.getCol()] = t.clone();
        }
        return true;
    }

    /**
     * This function will remove all tiles  from the list of points. if there is no tile at the point it will do nothing
     * @param points an array list of points to remove tiles from
     */
    public void removeTiles(ArrayList<ScrabbleBoardPoint> points){
        for (ScrabbleBoardPoint point: points ) {
            if(boardTiles[point.getRow()][point.getCol()] != null){
                boardTiles[point.getRow()][point.getCol()] = null;
            }
        }
    }

    /**
     * given a hashmap of moves  this function will check if the move is valid on the board
     * @param moves this is a hashmap of points mapped to tiles
     * @param rules the rules to check the move against
     * @return true if valid else false
     */
    public boolean validMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> moves, ScrabbleRules rules){
        //TODO need to first check if any tiles are mapped to a board point that already has a tile
        //TODO should I move this out of the board?
        return true;
    }

    public HashMap<ScrabbleBoardPoint, ScrabbleTile> export() {

        HashMap<ScrabbleBoardPoint, ScrabbleTile> temp = cachedMove;
        cachedMove = null;
        return temp;

    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getBoardValueAt(int r, int c){
        return boardValues[r][c];
    }

    public ScrabbleTile readTileAt(int r, int c){
        return boardTiles[r][c];
    }

    /**
     * this will return a list of all points on the tileboard that contain a tile.
     * @return an array list containing scrabbleBoardPOints representing played Tiles
     */
    public ArrayList<ScrabbleBoardPoint> getListPlayedTiles() {
        ArrayList<ScrabbleBoardPoint> temp = new ArrayList<>();
        for(int r = 0;r<boardSize;r++){
            for(int c = 0; c< boardSize;c++){
                if(boardTiles[r][c] != null){
                    temp.add(new ScrabbleBoardPoint(r,c));
                }
            }
        }
        return temp;
    }
}
