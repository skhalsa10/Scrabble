package me.snizzle.scrabble;

import me.snizzle.game.GameLogic;

public class ScrabbleGameLogic implements GameLogic {
    //declare some stuff
    private Importer importer;
    //declare objects used to make up the game.

    public ScrabbleGameLogic(Importer importer){
        this.importer = importer;
    }

    interface Importer {
        boolean timeToFetchData();
    }

    interface Exporter {

    }

    @Override
    public void step() {

    }
}
