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
    private ArrayList<ScrabbleBoardPoint> playedBlanks;
    HashMap<ScrabbleBoardPoint, ScrabbleTile> cachedMove;


    /**
     * constructs the default board
     */
    public ScrabbleBoard(ScrabbleRules rules){
        this("resources/scrabble_board.txt", rules);
    }

    /**
     * this will construct a board based on the configs in the file
     * @param boardConfig the file to the board config
     */
    public ScrabbleBoard(String boardConfig, ScrabbleRules rules){
        this.rules = rules;
        playedBlanks = new ArrayList<>();
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(boardConfig));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String line = fileReader.readLine();
            boardSize = Integer.parseInt(line);


            boardValues = new int[boardSize][boardSize];
            boardTiles = new ScrabbleTile[boardSize][boardSize];


            configureBoard(fileReader);
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this is similar to the constructor that takes a string name representing a file location. this one takes a fileReader
     * and it assumes that the next line read in is the number representing the board size. it will also not worry about closing the file
     * @param fileReader this
     * @param rules
     */
    public ScrabbleBoard(BufferedReader fileReader, ScrabbleRules rules){
        this.rules = rules;
        playedBlanks = new ArrayList<>();

        try {
            String line = fileReader.readLine();
            boardSize = Integer.parseInt(line);


            boardValues = new int[boardSize][boardSize];
            boardTiles = new ScrabbleTile[boardSize][boardSize];


            configureBoard(fileReader);
        }catch (IOException e) {
            e.printStackTrace();
        }

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
            for (int i = 0; i < boardSize; i++) {
                line = fileReader.readLine();

                //if(parsed.length != boardSize){throw new IOException();}

                for(c = 0; c< boardSize;c++) {

                    char word = line.charAt(c*3);
                    char letter = line.charAt((c*3)+1);
                    if(word == ' '){
                        boardTiles[r][c] = new ScrabbleTile(letter, rules.standardCharPoints(letter));
                    }
                    else if (word != '.') {
                        boardValues[r][c] = Integer.parseInt(String.valueOf(word)) + 'w';
                    } else if (letter != '.') {
                        boardValues[r][c] = Integer.parseInt(String.valueOf(letter));
                    } else {
                        boardValues[r][c] = 1;
                    }
                }

                r++;
            }

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
     * creates a list of all played tile mapped to the points they are on
     * @return
     */
    public HashMap<ScrabbleBoardPoint, ScrabbleTile> export() {
        //generate a list of all played points mapped to tiles
        HashMap<ScrabbleBoardPoint, ScrabbleTile> temp = new HashMap<>();
        for (ScrabbleBoardPoint p :getListPlayedTiles()) {
            temp.put(new ScrabbleBoardPoint(p.getRow(),p.getCol()),readTileAt(p.getRow(),p.getCol()));
        }

        // HashMap<ScrabbleBoardPoint, ScrabbleTile> temp = cachedMove; TODO I might not really need this cachedmove setting
        cachedMove = null;
        return temp;

    }

    /**
     *
     * @return the size of the board. the board should be square so this is the height and width
     */
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


    /**
     * will check the value at  boardValues[row][col] if it is divisible by the value of 'w'(i think this is 119)
     * then it is a word multiplier. I add the value of 'w' when I read in the board to represent it being a word multiplier.
     *
     * @param row
     * @param col
     * @return true if it is a word multiplier location at row and col and false otherwise
     */
    public boolean valueIsWordMultAt(int row, int col) {
        if(boardValues[row][col] / 'w' != 0){
            return true;
        }
        return false;
    }

    /**
     * prints a representation of the current board out
     */
    public void printBoard(){
        for (int r = 0; r < boardSize; r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < boardSize; c++) {
                if(boardTiles[r][c] != null){
                    line.append(" ");
                    line.append(boardTiles[r][c].readTile());
                    line.append(" ");
                }else{
                    int val = boardValues[r][c];
                    if(val == 1){
                        line.append(".. ");
                    }
                    else if(val/'w'==0){
                        line.append(".");
                        line.append(val);
                        line.append(" ");
                    }
                    else{
                        line.append(val%'w');
                        line.append(".");
                        line.append(" ");
                    }
                }
                c++;
            }
            System.out.println(line.toString());
            r++;
        }
    }
}
