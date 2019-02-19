package me.snizzle.game;

public interface GameLogic {

    interface Importer {
        boolean timeToFetchData();
    }

    interface Exporter {

    }

    void step();

}
