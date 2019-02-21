package me.snizzle.scrabble;

import java.util.ArrayList;
import java.util.Collection;

/**
 * this class defines a scrabble tile tray used by a player to store their hand of tiles.
 * I have overloaded the constructor to make a special tray from a file needed for testing the computer player
 *
 * @author siri Khalsa
 * @version 1
 */
public class ScrabbleTileTray {
    //probably only need an Array or an Array list here.
    private ArrayList<ScrabbleTile> tray;
    private ScrabbleTileBag tileBag;

    /**
     * constructs  a default Tile Tray with 7 random tiles
     */
    public ScrabbleTileTray(ScrabbleTileBag tileBag){
        tray = new ArrayList<>();
        this.tileBag = tileBag;
        initTray();
    }

    //helper used to draw 7 tiles
    private void initTray() {
        for(int i = 0; i<7; i++){
            drawFromTileBag();
        }
    }

    /**
     * picks up one tile will make private unless it is determined to be needed otherwise.
     */
    private void drawFromTileBag(){
        tray.add(tileBag.draw());
    }

    /**
     * fills the tray back up to 7 or as many tiles are left in the tile bag
     */
    public void fillTray(){
        while(!tileBag.isEmpty() && tray.size() < 7 ){
            drawFromTileBag();
        }
    }

    /**
     * overloaded version used for testing purposes loads a tile tray from a file.
     * @param trayString string of characters that contains a tray representation.
     */
    public ScrabbleTileTray(String trayString){
        //this is a test version of the tile tray needed for the computer test
        tileBag = null;
        tray = new ArrayList<>();
        if(trayString.length() != 7){
            System.out.println("Error parsing string");
            return;
        }
        //rules needed to get that point value of the char
        ScrabbleRules rules = new ScrabbleRules();
        //add the 7 character string to the tray. only used for testing
        for(int i = 0; i < 7; i++){
            tray.add(new ScrabbleTile(trayString.charAt(i),rules.standardCharPoints(trayString.charAt(i))));
        }
    }

    /**
     * this will return true if the tile tray contains  every value
     * @param values an Array list of tiles
     * @return true if every tile is contained in the TileTray
     */
    public boolean contains(ArrayList<ScrabbleTile> values) {
        for (ScrabbleTile v: values ) {
            if(!tray.contains(v)){
                return false;
            }
        }
        return true;
    }

    /**
     * This method removes the array list of tiles
     * @param scrabbleTiles list of tiles to remove
     * @return will return true if all tiles can be removed and false otherwise
     */
    public boolean remove(ArrayList<ScrabbleTile> scrabbleTiles) {
        for (ScrabbleTile v: scrabbleTiles){
            if(!tray.remove(v)){
                return false;
            }
        }

        return true;
    }

    /**
     * THis method will remove tiles from the tray and then fill the tray with new tiles
     * @param scrabbleTiles
     * @return true on success else false
     */
    public boolean removeAndFill(ArrayList<ScrabbleTile> scrabbleTiles){
        if(!remove(scrabbleTiles)){
            return false;
        }
        fillTray();
        return true;
    }
}
