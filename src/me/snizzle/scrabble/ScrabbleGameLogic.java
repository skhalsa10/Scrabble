package me.snizzle.scrabble;

import me.snizzle.game.GameLogic;

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
        this.board = new ScrabbleBoard();
        this.tileBag = new ScrabbleTileBag();
        this.words = new ScrabbleWords("enable.txt");
        this.rules = new ScrabbleRules();
        this.player = new ScrabbleHumanPlayer(board,tileBag, rules);
        this.comp = new ScrabbleCompPlayer(board,tileBag, rules);

        this.isGameOver = false;
        this.isPlayerTurn = true;
    }



    public void export(Exporter exporter){
        //TODO
        exporter.exportState(exportS);
    }

    @Override
    public void step() {
        if(isPlayerTurn){
            //we need to fetch data  for the user turn
            if(importer.timeToFetchData()){
                ScrabbleImportState userMove = (ScrabbleImportState)importer.fetch();
                player.cacheMove( userMove.getMove());
            }
        }
        //time to let the computer take a turn
        else{
            comp.takeTurn();
        }

    }
}
