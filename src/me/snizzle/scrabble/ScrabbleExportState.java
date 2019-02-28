package me.snizzle.scrabble;

import me.snizzle.game.LogicExportState;

import java.util.HashMap;

//TODO will need a boolean if move failed to give feedback

/**
 * this class has a purpose that transfers state from the logic to the Renderer. I have abstracted this behvaior because
 * the Dominos game required two seperate Renderers a JFX and a Text. this will also set these applications to implement new renderers
 * maybe Java comes out with a new one in a year or if I decide to export to a sepperate graphics engine.
 *
 * This class acts as a contract of the state that is needed to render
 */
public class ScrabbleExportState implements LogicExportState {
    private boolean userMoveFailed;
    private HashMap<ScrabbleBoardPoint, ScrabbleTile> playedTiles;
    private ScrabbleTile[] userTrayState;
    private int playerScore;
    private int compScore;
    //TODO hint data?


    public ScrabbleExportState(){
        userMoveFailed = false;
        playedTiles = new HashMap<>();
        playerScore = 0;
        compScore = 0;
    }

    //TODO if this does not reset anything els I should remove this.
    public void reset(){
        userMoveFailed = false;
    }

    public void setUserMoveFailed(boolean userMoveFailed) {
        this.userMoveFailed = userMoveFailed;
    }

    /**
     * this will return if the user move failed
     * @return
     */
    public boolean isUserMoveFailed() {
        //TODO should I change this to false here? after it gets checked?
        return userMoveFailed;
    }

    /**
     * This will make a copy of the user tray before it stores it the original tray will not be touch
     * @param userTrayState this is the current user state of the tray that will be exported to the renderer.
     */
    public void exportUserTray(ScrabbleTile[] userTrayState){
        ScrabbleTile[] temp = new ScrabbleTile[userTrayState.length];
        for (int i = 0; i < userTrayState.length; i++) {
            temp[i] = userTrayState[i].clone();
        }
        this.userTrayState = temp;
    }

    /**
     *
     * @return the state of the user tray at the moment in time.
     */
    public ScrabbleTile[] viewUserTray() {
        return userTrayState;
    }

    /**
     * sets the new tiles to add to export to the GUI. it will make a copy of them though so the state does not get corrupted
     * @param newBoardTiles new tiles to be played
     */
    public  void setPlayedTiles(HashMap<ScrabbleBoardPoint, ScrabbleTile> newBoardTiles){
        this.playedTiles = null;
        if(newBoardTiles == null){return;}
        HashMap<ScrabbleBoardPoint, ScrabbleTile> temp = new HashMap<>();
        for (ScrabbleBoardPoint p: newBoardTiles.keySet()) {
            temp.put(p,newBoardTiles.get(p).clone());
        }

        this.playedTiles = temp;
    }

    /**
     *
     * @return the latest tiles to be added to the gui
     */
    public HashMap<ScrabbleBoardPoint, ScrabbleTile> getPlayedTiles() {
        return playedTiles;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getCompScore() {
        return compScore;
    }

    public void setCompScore(int compScore) {
        this.compScore = compScore;
    }
}
