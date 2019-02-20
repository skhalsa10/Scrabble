package me.snizzle.game;

public interface GameLogic {

    void export(Exporter gui);

    interface Importer {
        boolean timeToFetchData();
    }

    interface Exporter {

    }

    void step();

}
