package me.snizzle.scrabble;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
    private GridPane boardGridPane;
    private GridPane trayGridPane;
    private Scene scene;
    private VBox root;
    private Label title;



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

        //create the boardGridPane
        boardGridPane = new GridPane();
        initBoardGridPane();

        //create the title
        title = new Label("SCRABBLE");
        title.setAlignment(Pos.CENTER);
        title.setId("title");
        title.setMinHeight(35);

        //create usertraypane convas and graphics context
        trayGridPane = new GridPane();
        initTray();


        //initialize the scene and root node attach stylesheets and add everything togethor
        root = new VBox();
        scene = new Scene(root, calculateMinWidth(),calculateMinHeight() );
        scene.getStylesheets().add("me/snizzle/scrabble/Scrabble.css");
        gameStage.setScene(scene);
        gameStage.setTitle("SCRABBLE");

        root.getChildren().add(title);
        //root.getChildren().add(bPane);
        root.getChildren().add(boardGridPane);
        root.getChildren().add(trayGridPane);
        root.setAlignment(Pos.CENTER);
        gameStage.setMinHeight(calculateMinHeight());
        gameStage.setMinWidth(calculateMinWidth());




    }

    private void initTray() {
        trayGridPane.setMinHeight(TILESIZE);
        trayGridPane.setMinWidth(TILESIZE*userTray.length);
        trayGridPane.setHgap(3);
        trayGridPane.setAlignment(Pos.CENTER);
        for(int i = 0; i < userTray.length;i++){
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            Rectangle rect = new Rectangle(0,0,TILESIZE, TILESIZE);
            rect.setFill(Color.web("#2B2B2B"));
            stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("Tray index " + trayGridPane.getColumnIndex((Node)event.getSource()));
                    ((Text)((StackPane)event.getSource()).getChildren().get(((StackPane)event.getSource()).getChildren().size()-2)).setId("tile-letter-text-selected");
                }
            });
            stackPane.getChildren().add(rect);
            trayGridPane.add(stackPane,i,0);
        }

    }

    /**
     * this will initialize the board to the default with no tiles in it
     */
    private void initBoardGridPane() {
        boardGridPane.setMinWidth(TILESIZE*15+50);
        boardGridPane.setMinHeight(TILESIZE*15 + 50);
        boardGridPane.setId("board-pane");
        boardGridPane.setHgap(1);
        boardGridPane.setVgap(1);
        boardGridPane.setAlignment(Pos.CENTER);
        boardGridPane.setGridLinesVisible(true);
        StackPane stackPane;
        for(int r = 0; r < board.getBoardSize(); r++) {
            for (int c = 0; c < board.getBoardSize(); c++) {
                stackPane = buildBoardCell(board.getBoardValueAt(r,c));
                stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("row is " + boardGridPane.getRowIndex((Node) event.getSource()) );
                        System.out.println("Column= is " + boardGridPane.getColumnIndex((Node) event.getSource()) );
                    }
                });
                if(r == 7 && c == 7){
                    Text text = (Text) stackPane.getChildren().get(stackPane.getChildren().size()-1);
                    text.setText("*");
                }
                boardGridPane.add(stackPane,c,r);
            }
        }
    }

    /**
     * this build the stackPane of a cell in the grid representing the board
     * @param v the value of the board cell to determine the multiplier of the board location
     * @return
     */
    private StackPane buildBoardCell(int v){
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setMaxWidth(TILESIZE);
        stackPane.setMinWidth(TILESIZE);
        stackPane.setMinHeight(TILESIZE);
        stackPane.setMaxHeight(TILESIZE);
        Text text = new Text();
        text.setId("text");
        Rectangle rect = new Rectangle(0,0, TILESIZE, TILESIZE);
        rect.setStroke(Color.DARKBLUE);
        if(v - 'w' == 3){
            rect.setFill(Color.RED);
            text.setText("Wx\n3");
        }
        else if(v - 'w' == 2){
            rect.setFill(Color.HOTPINK);
            text.setText("Wx\n2");
        }
        else if(v ==3){
            rect.setFill(Color.BLUEVIOLET);
            text.setText("Lx\n3");
        }
        else if(v == 2){
            rect.setFill(Color.BLUE);
            text.setText("Lx\n2");
        }
        else{
            rect.setFill(Color.rgb(40, 255, 237));
        }
        stackPane.getChildren().add(rect);
        stackPane.getChildren().add(text);

        return stackPane;
    }

    private double calculateMinWidth() {
        return boardGridPane.getMinWidth();
    }

    /**
     * returns the minimum heigh needed to display all gui
     * @return
     */
    private double calculateMinHeight(){
        return boardGridPane.getMinHeight()+trayGridPane.getMinHeight() + title.getMinHeight()+100;
    }

    /**
     * this will build the gui and render it
     */
    //@Override
    public void render() {
        renderBoardState();
        renderTrayState();
    }

    private void renderTrayState() {
        if(userTray != null && userTray.length ==7){
            for (int i = 0; i <userTray.length ; i++) {
                renderTileText((StackPane) trayGridPane.getChildren().get(i), userTray[i]);
            }
        }
    }

    private boolean renderTileText(StackPane stackPane, ScrabbleTile tile) {
        Object o = stackPane.getChildren().get(stackPane.getChildren().size()-1);
        if (!(o instanceof Rectangle)){
            return false;
        }

        Rectangle r = (Rectangle) o;
        //this will tell if it is a tile rectangle base on the color
        if(!r.getFill().equals(Color.web("#2B2B2B"))){
            return false;
        }
        Text letter = renderTileLetter(tile.readTile());
        Text points = renderTilePoints(tile.getPoints());
        stackPane.getChildren().add(letter);
        stackPane.setAlignment(letter,Pos.CENTER);
        stackPane.getChildren().add(points);
        stackPane.setAlignment(points, Pos.BOTTOM_RIGHT);
        return true;
    }

    private Rectangle renderTileBase(){

        Rectangle rect = new Rectangle(0,0,TILESIZE,TILESIZE);
        rect.setFill(Color.web("#2B2B2B"));
        return rect;

    }

    private Text renderTileLetter(char letter){

        Text tileText = new Text(Character.toString(letter).toUpperCase());
        tileText.setId("tile-letter-text");


        return tileText;
    }

    private Text renderTilePoints(int points){
        Text tilepoints = new Text(Integer.toString(points));
        tilepoints.setId("tile-points-text");
        return tilepoints;
    }
    //I should try to to this on a grid pane rather than drawing to canvas. I CAN USE A STACK
    //pane inside the gridpane and make it look better with text and rectangles and stuff
    private void renderBoardState() {

        //TODO all this will do is add any new tiles to the board
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
        //userTray = es.viewUserTray();
        userTray = new ScrabbleTile[]{new ScrabbleTile('g',1),
                new ScrabbleTile('w',2),
                new ScrabbleTile('r',3),
                new ScrabbleTile('a',4),
                new ScrabbleTile('z',5),
                new ScrabbleTile('c',6),
                new ScrabbleTile('l',7),};

    }
}
