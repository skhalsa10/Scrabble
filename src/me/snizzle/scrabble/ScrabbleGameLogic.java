package me.snizzle.scrabble;

import me.snizzle.game.GameLogic;

public class ScrabbleGameLogic implements GameLogic {
    //declare some stuff
    private Importer importer;
    //declare objects used to make up the game.
    private ScrabblePlayer human;
    private ScrabblePlayer comp;
    private ScrabbleBoard board;
    private ScrabbleTileBag tileBag;
    private ScrabbleWords words;
    private ScrabbleRules rules;

    public ScrabbleGameLogic(Importer importer){
        this.importer = importer;
    }



    public void export(Exporter exporter){

    }

    @Override
    public void step() {

    }
}
