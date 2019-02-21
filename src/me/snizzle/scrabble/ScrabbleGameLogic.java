package me.snizzle.scrabble;

import me.snizzle.game.GameLogic;

public class ScrabbleGameLogic implements GameLogic {
    //declare some stuff
    private Importer importer;
    //declare objects used to make up the game.
    private ScrabblePlayer player;
    private ScrabblePlayer comp;
    private ScrabbleBoard board;
    private ScrabbleTileBag tileBag;
    private ScrabbleWords words;
    private ScrabbleRules rules;
    private ScrabbleExportState exportS;

    //other objects needed for the logic
    private boolean isGameOver;
    private boolean isPlayerTurn;

    public ScrabbleGameLogic(Importer importer){
        this.importer = importer;
        this.exportS = new ScrabbleExportState();
        this.board = new ScrabbleBoard();
        this.tileBag = new ScrabbleTileBag();
        this.words = new ScrabbleWords("enable.txt");
        this.rules = new ScrabbleRules();
        this.player = new ScrabbleHumanPlayer(board,tileBag);
        this.comp = new ScrabbleCompPlayer(board,tileBag);

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

    }
}
