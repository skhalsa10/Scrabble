package me.snizzle.scrabble;

import me.snizzle.game.GameLogic;

import java.util.HashMap;

/**
 * this is the main logic of the game it keeps track of the board
 * the players the rules and tilebag and lets them play togethor
 * @author Siri Khalsa
 * @version 1
 */
public class ScrabbleGameLogic implements GameLogic {
    //declare importer
    private Importer importer;
    //declare objects used to make up the game.
    private ScrabblePlayer player;
    private ScrabblePlayer comp;
    private ScrabbleBoard board;
    private ScrabbleTileBag tileBag;
    //private ScrabbleWords words;
    private ScrabbleRules rules;
    private ScrabbleExportState exportS;

    //other boolean logic needed
    private boolean isGameOver;
    private boolean isPlayerTurn;

    public ScrabbleGameLogic(Importer importer) {
        this.importer = importer;
        this.exportS = new ScrabbleExportState();
        this.tileBag = new ScrabbleTileBag();
        //TODO should I really have the words here? probably needs to be tucked into the rules
        //this.words = new ScrabbleWords("resources/enable.txt");
        this.rules = new ScrabbleRules();
        this.board = new ScrabbleBoard(rules);
        this.player = new ScrabbleHumanPlayer(board, tileBag, rules);
        this.comp = new ScrabbleCompPlayer(board, tileBag, rules);

        this.isGameOver = false;
        this.isPlayerTurn = true;
    }


    /**
     * repopulate the Export state with new data and send it to the Exporter.
     *
     * @param exporter
     */
    public void export(Exporter exporter) {
        //I will update the exportS with the current tray and the new tiles played since last render
        exportS.exportUserTray(player.tileTrayToArray());
        exportS.setPlayedTiles(board.export());
        exportS.setCompScore(comp.getScore());
        exportS.setPlayerScore(player.getScore());
        exporter.exportState(exportS);
    }

    /**
     * this method will sted through the logic. it does that with one of three ways. it will process the user
     * move if ready. if not the entire step is ignored and will be processed again later. this could happen a million times
     * while it waits for the user to finish the move.
     * <p>
     * the other thing it will do is process the computers turn.
     */
    @Override
    public void step() {
        if (isPlayerTurn) {
            //System.out.println(importer.timeToFetchData());
            //we need to fetch data  for the user turn if ready. if not skip step and come back.
            if (importer.timeToFetchData()) {
                //System.out.println("processing");
                ScrabbleImportState userMove = (ScrabbleImportState) importer.fetch();
                player.cacheMove(userMove.getMove());
                //printCachedMove(userMove.getMove());
                player.setBlankPoints(userMove.getBlankPoints());
                if (player.takeTurn()) {
                    isPlayerTurn = false;
                } else {
                    //this will force the user to try again.
                    exportS.setUserMoveFailed(true);
                }
            }
        }
        //process the computer if it is not the users turn
        if (!isPlayerTurn) {
            comp.takeTurn();
            isPlayerTurn = true;
        }

    }


    private void printCachedMove(HashMap<ScrabbleBoardPoint, ScrabbleTile> currentMove) {
        for (ScrabbleBoardPoint p : currentMove.keySet()) {
            System.out.println("r: " + p.getRow() + " c: " + p.getCol() + " -> " + currentMove.get(p).readTile());
        }
    }

}