package me.snizzle.game;

public interface GameLogic {

    void export(Exporter gui);

    interface Importer {
        boolean timeToFetchData();
        LogicImportState fetch();
    }

    interface Exporter {
        void exportState(LogicExportState exportState);
    }

    void step();

}
