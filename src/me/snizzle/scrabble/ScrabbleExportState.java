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
    //TODO hint data?
    //TODO score data

    public ScrabbleExportState(){
        userMoveFailed = false;
        playedTiles = new HashMap<>();
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
     *
     * @param userTrayState this is the current user state of the tray that will be exported to the renderer
     */
    public void exportUserTray(ScrabbleTile[] userTrayState){
        this.userTrayState = userTrayState;
    }

    /**
     *
     * @return the state of the user tray at the moment in time.
     */
    public ScrabbleTile[] viewUserTray() {
        return userTrayState;
    }
}
