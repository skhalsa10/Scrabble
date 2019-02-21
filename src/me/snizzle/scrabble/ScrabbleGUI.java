package me.snizzle.scrabble;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
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
    private final int TILESIZE = 50;
    private Stage gameStage;
    private Canvas boardCanvas;
    private GraphicsContext bGC;
    private Canvas userTrayCanvas;
    private GraphicsContext utGC;
    private Scene scene;
    private VBox root;




    //declare state needed to render but not FX objects
    private boolean userMovedFailed;
    private ScrabbleBoard board;
    private ScrabbleTile[] userTray;


    /**
     * this constructs a new GUI
     * @param gameStage this takes a stage that will appear as a second window for the scrabble game
     */
    public ScrabbleGUI(Stage gameStage){
        //TODO I really dont like the fact  of building the board with rules. I think I would rather
        //TODO pass a board clone to a rules object and let the rules tust for validity this just more like rules behavior to me. what if the rules change that change how validity is determined.
        board = new ScrabbleBoard(new ScrabbleRules());
        //this will be initialized like this for now just to get GUI rendering
        userTray = new ScrabbleTile[7];

        //set up jfx stuff
        this.gameStage = gameStage;
        boardCanvas = new Canvas(TILESIZE*board.getBoardSize(), TILESIZE*board.getBoardSize());
        bGC = boardCanvas.getGraphicsContext2D();
        userTrayCanvas = new Canvas(TILESIZE*7, TILESIZE);
        root = new VBox();
        //TODO make a mehtod that calculates the width and Height)
        scene = new Scene(root, boardCanvas.getWidth(), boardCanvas.getHeight()+userTrayCanvas.getHeight());







    }

    /**
     * this will build the gui and render it
     */
    //@Override
    public void render() {
        renderBoardState(bGC);
    }

    private void renderBoardState(GraphicsContext gc) {
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
        userTray = es.viewUserTray();

    }
}
