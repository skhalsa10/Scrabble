package me.snizzle.scrabble;

import java.util.ArrayList;

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
     * fills the tray back up to 7
     */
    public void fillTray(){
        while(tray.size() < 7){
            drawFromTileBag();
        }
    }

    /**
     * overloaded version used for testing purposes loads a tile tray from a file.
     * @param fileName filename that contains a tray representation.
     */
    public ScrabbleTileTray(String fileName){
        //TODO still need to write
    }
}
