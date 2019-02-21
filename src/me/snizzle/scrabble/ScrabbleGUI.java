package me.snizzle.scrabble;

import javafx.stage.Stage;
import me.snizzle.game.LogicExportState;
import me.snizzle.game.LogicImportState;
import me.snizzle.game.Renderable;

/**
 * this will be the gui for the scrabble game. because it is a Scrabble Gui it implements the ScrabbleGameLogic
 * Exporter and importer. this permanently links these togethor as they will agree on a defined protocal to communicate
 * state back and fourth. it renders any state that has changed in the logic since the last render.
 *
 * It also collects new state from interaction with gui. the Logic will Import this state back to where it belongs
 */
public class ScrabbleGUI implements ScrabbleGameLogic.Importer, ScrabbleGameLogic.Exporter, Renderable{
    //declare needed objects. as is true with all GUI stuff this will fill up with code
    private Stage gameStage;
    private boolean userMovedFailed;

    /**
     * this constructs a new GUI
     * @param gameStage this takes a stage that will appear as a second window for the scrabble game
     */
    public ScrabbleGUI(Stage gameStage){
        this.gameStage = gameStage;
    }

    //@Override
    public void render() {
        //TODO
    }

    /**
     * the importer will be asking for data probably well before the data is ready to go. the animation timer
     * is pretty fast at cycling through the handle function. so this simple beauty allows the application to
     * continue doing stuff and then come back and ask later for data until it becomes time to fetch that data.
     * @return true or false depending on if all data has been collected.
     */
    @Override
    public boolean timeToFetchData() {
        //TODO
        return false;
    }

    @Override
    public LogicImportState fetch() {
        //TODO

        return null;
    }

    /**
     * this function will import the current logic state using the logicstateexporter
     * @param exportState state from the logic.
     */
    @Override
    public void exportState(LogicExportState exportState) {
        ScrabbleExportState es = (ScrabbleExportState) exportState;
        userMovedFailed = es.isUserMoveFailed();

    }
}
