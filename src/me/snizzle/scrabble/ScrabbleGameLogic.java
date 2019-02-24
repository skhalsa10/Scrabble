package me.snizzle.scrabble;

import me.snizzle.game.GameLogic;

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
    private ScrabbleWords words;
    private ScrabbleRules rules;
    private ScrabbleExportState exportS;

    //other boolean logic needed
    private boolean isGameOver;
    private boolean isPlayerTurn;

    public ScrabbleGameLogic(Importer importer){
        this.importer = importer;
        this.exportS = new ScrabbleExportState();
        this.tileBag = new ScrabbleTileBag();
        //TODO should I really have the words here? probably needs to be tucked into the rules
        this.words = new ScrabbleWords("resources/enable.txt");
        this.rules = new ScrabbleRules();
        this.board = new ScrabbleBoard(rules);
        this.player = new ScrabbleHumanPlayer(board,tileBag, rules);
        this.comp = new ScrabbleCompPlayer(board,tileBag, rules);

        this.isGameOver = false;
        this.isPlayerTurn = true;
    }



    public void export(Exporter exporter){
        //I will update the exportS with the current tray and the new tiles played since last render
        exportS.exportUserTray(player.);

        exporter.exportState(exportS);
    }

    /**
     * this method will sted through the logic. it does that with one of three ways. it will process the user
     * move if ready. if not the entire step is ignored and will be processed again later. this could happen a million times
     * while it waits for the user to finish the move.
     *
     * the other thing it will do is process the computers turn.
     */
    @Override
    public void step() {
        if(isPlayerTurn ){
            //we need to fetch data  for the user turn if ready. if not skip step and come back.
            if(importer.timeToFetchData()){
                ScrabbleImportState userMove = (ScrabbleImportState)importer.fetch();
                player.cacheMove( userMove.getMove());
                if(player.takeTurn()){
                    isPlayerTurn = false;
                }else{
                    //this will force the user to try again.
                    exportS.setUserMoveFailed(true);
                }
            }
        }
        //process the computer if it is not the users turn
        if(!isPlayerTurn){
            comp.takeTurn();
            isPlayerTurn = true;
        }

    }
}
