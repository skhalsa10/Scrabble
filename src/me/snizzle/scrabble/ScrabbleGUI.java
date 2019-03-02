package me.snizzle.scrabble;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.snizzle.game.LogicExportState;
import me.snizzle.game.LogicImportState;
import me.snizzle.game.Renderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;


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
    private StackPane[][] boardGridStackPaneRef;
    private GridPane trayGridPane;
    private StackPane[] trayGridStackRef;
    private Scene scene;
    private VBox root;
    private Label title;
    private Button reset;
    private Button playMove;
    private Pane buttonSpacer1;
    private Pane buttonSpacer2;
    private HBox buttonContainer;
    private VBox playerScoreBox;
    private Label playerScoreNum;
    private Label playerScore;
    private VBox compScoreBox;
    private Label compScoreNum;
    private Label compScore;


    //declare state needed to render but not FX objects
    private boolean userMovedFailed;
    private ScrabbleBoard board;
    private ScrabbleTile[] userTray;
    private boolean importerReady;
    private boolean newTurn;
    private HashMap<ScrabbleBoardPoint,ScrabbleTile> cachedMove;
    private ScrabbleTile cachedSelection;
    private ScrabbleImportState importState;
    private boolean selectedIsBlank;
    private ArrayList<ScrabbleBoardPoint> blankPoints;


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
        cachedMove = new HashMap<>();
        importerReady = false;
        newTurn = true;
        importState = new ScrabbleImportState();
        selectedIsBlank = false;
        blankPoints = new ArrayList<>();

        //set up jfx stuff
        this.gameStage = gameStage;

        //create the boardGridPane
        boardGridPane = new GridPane();
        boardGridStackPaneRef = new StackPane[board.getBoardSize()][board.getBoardSize()];
        initBoardGridPane();

        //create the title
        title = new Label("SCRABBLE");
        title.setAlignment(Pos.CENTER);
        title.setId("title");
        title.setMinHeight(35);

        //create usertraypane convas and graphics context
        trayGridPane = new GridPane();
        trayGridStackRef = new StackPane[7];
        initTray();

        //initialize Buttons
        reset = new Button("Reset Move");
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetCachedMove();
            }
        });
        reset.getStyleClass().add("reset-button");
        playMove = new Button("Play Move");
        playMove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("play pressed");
                playMoveHandler();
            }
        });
        playMove.getStyleClass().add("play-button");
        //init scores
        //player First
        playerScoreBox = new VBox();
        playerScoreNum = new Label("0");
        playerScoreNum.setId("score-num");
        playerScore = new Label("Player Score");
        playerScore.setId("score");
        playerScoreBox.getChildren().addAll(playerScoreNum,playerScore);
        playerScoreBox.setAlignment(Pos.BOTTOM_CENTER);
        playerScoreBox.setPadding(new Insets(2,7,2,2));
        //now the computer score
        compScoreBox = new VBox();
        compScoreNum = new Label("0");
        compScoreNum.setId("score-num");
        compScore = new Label("Comp Score");
        compScore.setId("score");
        compScoreBox.getChildren().addAll(compScoreNum,compScore);
        compScoreBox.setAlignment(Pos.BOTTOM_CENTER);
        compScoreBox.setPadding(new Insets(2, 2,2,7));

        //build spacers
        buttonSpacer1 = new Pane();
        buttonSpacer2 = new Pane();
        buttonContainer = new HBox();
        buttonContainer.getChildren().addAll(reset, buttonSpacer1,playerScoreBox,
                                                compScoreBox,buttonSpacer2,playMove);
        buttonContainer.setHgrow(buttonSpacer1,Priority.ALWAYS);
        buttonContainer.setHgrow(buttonSpacer2,Priority.ALWAYS);





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
        root.getChildren().add(buttonContainer);
        root.setAlignment(Pos.CENTER);

        gameStage.setMinHeight(calculateMinHeight());
        gameStage.setMinWidth(calculateMinWidth());




    }

    private void playMoveHandler() {
        System.out.println("we wilhandle the play button one day");
        importerReady  = true;
    }

    /**
     * this will undo the current cached move. delete  rendered tile on the board and put it back into the tray
     */
    private void resetCachedMove() {

        //if the cachedselection still exists
        if(selectedIsBlank){
            cachedSelection = new ScrabbleTile(' ', 0);
            selectedIsBlank = false;
        }
        //lets first set the the blank points in the cached move back to having blank tiles
        for (ScrabbleBoardPoint p: blankPoints  ) {
            //cachedMove.remove(p);
            if(cachedMove.containsKey(p)){
                cachedMove.put(p,new ScrabbleTile(' ',0));
            }else{
                System.out.println("error in resetCachedMove()");
            }

        }
        blankPoints.clear();


        Iterator cachedIterator = cachedMove.keySet().iterator();

        for (int i = 0; i < userTray.length; i++) {
            ScrabbleTile tile = userTray[i];
            if (tile == null){
                if(cachedSelection != null){
                    userTray[i] = cachedSelection;
                    cachedSelection = null;
                    continue;
                }
                if(!cachedIterator.hasNext()){
                    System.out.println("error cachedMove size does not match missing tiles in user tray");
                }
                ScrabbleBoardPoint p = (ScrabbleBoardPoint)cachedIterator.next();
                unRenderBoardTile(boardGridStackPaneRef[p.getRow()][p.getCol()]);
                userTray[i] = cachedMove.get(p);
                cachedIterator.remove();

            }

        }

        renderTrayState();
    }

    private boolean unRenderBoardTile(StackPane sp) {

        if(sp.getChildren().size() < 5){
            return false;
        }

        sp.getChildren().remove(sp.getChildren().size() -1);
        sp.getChildren().remove(sp.getChildren().size() -1);
        sp.getChildren().remove(sp.getChildren().size() -1);

        return true;
    }

    /**
     * set up the tray for the first time
     */
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
                    selectTile(trayGridPane.getColumnIndex((Node)event.getSource()));

                }
            });
            stackPane.getChildren().add(rect);
            trayGridPane.add(stackPane,i,0);
            trayGridStackRef[i] = stackPane;
        }

    }

    /**
     * select the tile to place on the board
     * @param index
     */
    private void selectTile(int index) {
        if(cachedSelection == null) {
            trayGridStackRef[index].getChildren().get(trayGridStackRef[index].getChildren().size()-2).setId("tile-letter-text-selected");
            trayGridStackRef[index].getChildren().get(trayGridStackRef[index].getChildren().size()-1).setId("tile-points-text-selected");
            //cachedSelection = userTray[index];
            //lets try handling the blank here
            if(userTray[index].readTile() == ' '){
                selectedIsBlank = true;
                char c = getBlankChar();
                userTray[index] = null;
                userTray[index] = new ScrabbleTile(c,0);
            }
            cachedSelection = userTray[index];
            userTray[index] = null;

        }

    }

    /**
     * lets hope this works  this will hopeful make a pop up dialogbox that
     * collects the char from the user to be used on the blank tile
     * @return letter for  blank tile
     */
    private char getBlankChar() {

        TextInputDialog dialog = new TextInputDialog("c");

        //set up styling
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("Scrabble.css").toExternalForm());
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).getStyleClass().add("play-button");
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).getStyleClass().add("reset-button");


        dialog.setTitle("Blank for Letter");
        dialog.setHeaderText("Please enter a LOWERCASE Letter to \nbe used on the Blank tile: ");
        dialog.setContentText("Letter: ");
        Button ok = (Button)dialog.getDialogPane().lookupButton(ButtonType.OK);
        TextField textInput = dialog.getEditor();
        //textInput.onInputMethodTextChangedProperty()
        textInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateOK(ok, newValue);
            }

            private void updateOK(Button ok, String text) {
                if(text.length() ==1 && text.charAt(0) >= 97 && text.charAt(0) <= 122){
                    ok.setDisable(false);
                }
                else{
                    ok.setDisable(true);
                }
            }
        });
        Optional<String> input = dialog.showAndWait();
        System.out.println(textInput.getText());
        //input.ifPresent(letter -> {c = letter.charAt(0);});
        return textInput.getText().charAt(0);


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

                        selectBoardPoint(boardGridPane.getRowIndex((Node) event.getSource()),
                                boardGridPane.getColumnIndex((Node) event.getSource()));
                    }
                });
                if(r == 7 && c == 7){
                    Text text = (Text) stackPane.getChildren().get(stackPane.getChildren().size()-1);
                    text.setText("*");
                }
                boardGridPane.add(stackPane,c,r);
                boardGridStackPaneRef[r][c] = stackPane;
            }
        }
    }

    /**
     * the board point to place the tile on a tile must be selected for this point to be registered
     * @param row
     * @param col
     */
    private void selectBoardPoint(int row, int col) {
        //there is already a tile on this point. refuse it
        if(board.readTileAt(row,col) != null){return;}
        //there is already a tile in the cached move dedicated for this spot
        if(cachedMove.containsKey(new ScrabbleBoardPoint(row,col))){
            return;
        }
        if(cachedSelection != null){
            cachedMove.put(new ScrabbleBoardPoint(row,col), cachedSelection);
            cachedSelection = null;
            if(selectedIsBlank) {
                selectedIsBlank = false;
                blankPoints.add(new ScrabbleBoardPoint(row, col));
            }
        }
        //System.out.println(cachedMove.size());
        renderTrayState();
        renderCachedMove();
    }

    private void renderCachedMove() {
        for (ScrabbleBoardPoint p: cachedMove.keySet()) {
            StackPane sp = boardGridStackPaneRef[p.getRow()][p.getCol()];
            renderTileText(sp,cachedMove.get(p),true);
            sp.getChildren().get(sp.getChildren().size()-2).setId("tile-letter-text-selected");
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

    /**
     * returns the minimum width needed to display all objects
     * @return the minimum width needed to display all objects
     */
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
        if(newTurn) {
            renderBoardState();
            renderTrayState();
            newTurn = false;
        }
    }

    /**
     * this will render the tray state
     */
    private void renderTrayState() {
        if(userTray != null && userTray.length ==7){
            for (int i = 0; i <userTray.length ; i++) {
                renderTileText((StackPane) trayGridPane.getChildren().get(i), userTray[i], false);
            }
        }
    }

    /**
     * this will render the tile depending on where it is  this can get complicated
     * @param stackPane the pane that will hold the new tile render. dpending on the pane passed in it can have a set of
     *                  current legal states to work with
     * @param tile the tile to be rendered
     * @param isBoardTile will change how it approaches the render of the tile.
     * @return true if successful or false otherwise.
     */
    private boolean renderTileText(StackPane stackPane, ScrabbleTile tile, boolean isBoardTile) {
        //lets take of the board tile case
        if(isBoardTile){
            //first will check the size of stackpane's children it can only be 3 Stackpane->Rectangle->Text
            if(stackPane.getChildren().size() != 2 ){
                return false;
            }
            //TODO I think the size is all I need to test because nothing else will be three.
            //render the tile base
            stackPane.getChildren().add(renderTileBase());
            //now in a state to add text

        }
        //now do the rare case of blank tile trays which we get first time after initialization
        else if(!isBoardTile && stackPane.getChildren().size() ==1) {
            Object o = stackPane.getChildren().get(stackPane.getChildren().size() - 1);
            if (!(o instanceof Rectangle)) {
                return false;
            }

            Rectangle r = (Rectangle) o;
            //this will tell if it is a tile rectangle base on the color
            if (!r.getFill().equals(Color.web("#2B2B2B"))) {
                return false;
            }

            //already in a state to add and allign text.
        }
        else if(!isBoardTile && stackPane.getChildren().size() ==3){
            //this is the case of a tile in the tray already rendered with text we delete the the text and add the new text
            stackPane.getChildren().remove(stackPane.getChildren().size() -1);
            stackPane.getChildren().remove(stackPane.getChildren().size() -1);
            //now ready for more text
        }
        else{
            return false;
        }

        //add and allign letter and points text
        if(tile == null){return true;}
        Text letter = renderTileLetter(tile.readTile());
        Text points = renderTilePoints(tile.getPoints());
        stackPane.getChildren().add(letter);
        stackPane.setAlignment(letter,Pos.CENTER);
        stackPane.getChildren().add(points);
        stackPane.setAlignment(points, Pos.BOTTOM_RIGHT);
        return true;
    }

    /**
     * this will return a rectangle base to set behind the tile text
     * @return rectangle base of tile
     */
    private Rectangle renderTileBase(){

        Rectangle rect = new Rectangle(0,0,TILESIZE,TILESIZE);
        rect.setFill(Color.web("#2B2B2B"));
        return rect;

    }

    /**
     * this will return the Text of the letter
     * @param letter to add to Text object
     * @return Text containing only the letter
     */
    private Text renderTileLetter(char letter){

        Text tileText = new Text(Character.toString(letter).toUpperCase());
        tileText.setId("tile-letter-text");


        return tileText;
    }

    /**
     * this will return a Text object containing the points as the text
     * @param points to represent as a Text object
     * @return Text object representing the points.
     */
    private Text renderTilePoints(int points){
        Text tilepoints = new Text(Integer.toString(points));
        tilepoints.setId("tile-points-text");
        return tilepoints;
    }


    private void renderBoardState() {
        //lets reset the  board to match the actual state in case it has changed during the turn.
        for(int r = 0;r<board.getBoardSize(); r++){
            for (int c = 0; c< board.getBoardSize();c++){

                //if there is no tile at this spot then canfirm it has no tile there
                if(board.readTileAt(r, c) == null){
                    resetBoardCellStackPane(boardGridStackPaneRef[r][c]);
                }else{
                    //else add the tile
                    renderTileText(boardGridStackPaneRef[r][c], board.readTileAt(r, c),true);
                    setTextColor(r,c);
                    //TODO should I change the tiles to display to change the color
                }
            }
        }

    }

    /**
     * sets the text color of the tile on the board at r and c
     * @param r
     * @param c
     */
    private void setTextColor(int r, int c) {
        //get the text nodes and change the id based on  underlying board value
        if (board.getBoardValueAt(r, c) - 'w' == 3) {
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 2).setId("tile-letter-text-3w");
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 1).setId("tile-points-text-3w");
        }
        else if(board.getBoardValueAt(r,c)- 'w' == 2){
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 2).setId("tile-letter-text-2w");
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 1).setId("tile-points-text-2w");
        }
        else if(board.getBoardValueAt(r,c) == 3){
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 2).setId("tile-letter-text-3l");
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 1).setId("tile-points-text-3l");
        }
        else if(board.getBoardValueAt(r,c) == 2){
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 2).setId("tile-letter-text-2l");
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 1).setId("tile-points-text-2l");
        }
        else{
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 2).setId("tile-letter-text");
            boardGridStackPaneRef[r][c].getChildren().get(boardGridStackPaneRef[r][c].getChildren().size() - 1).setId("tile-points-text");
        }
    }

    /**
     * resets the stackpane of the board to the default state...if needed.
     * @param sp
     * @return
     */
    private boolean resetBoardCellStackPane(StackPane sp) {
        if(sp.getChildren().size() < 2){
            //already reset
            return false;
        }
        //delete last child until there are only 2 left
        while(sp.getChildren().size() > 2){
            sp.getChildren().remove(sp.getChildren().size()-1);
        }
        return true;
    }


    /**
     * the importer will be asking for data probably well before the data is ready to go. the animation timer
     * is pretty fast at cycling through the handle function. so this simple beauty allows the application to
     * continue doing stuff and then come back and ask later for data until it becomes time to fetch that data.
     * @return true or false depending on if all data has been collected.
     */
    @Override
    public boolean timeToFetchData() {
        //System.out.println("timetofetchdata called and returned: " +importerReady);
        return importerReady;
    }

    @Override
    public LogicImportState fetch() {
        importState.setMove(cachedMove);
        cachedMove = new HashMap<>();
        importState.setBlankPoints(blankPoints);
        blankPoints = new ArrayList<>();
        importerReady = false;
        newTurn = true;
        return importState;
    }

    /**
     * this function will import the current logic state using the logicstateexporter
     * @param exportState state from the logic.
     */
    @Override
    public void exportState(LogicExportState exportState) {
        ScrabbleExportState es = (ScrabbleExportState) exportState;
        if(newTurn){
            userMovedFailed = es.isUserMoveFailed();
            userTray = es.viewUserTray();
            board.placeTiles(es.getPlayedTiles());
            playerScoreNum.setText(Integer.toString(es.getPlayerScore()));
            compScoreNum.setText(Integer.toString(es.getCompScore()));
        }
    }
}
