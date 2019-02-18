package me.snizzle.scrabble;

import javafx.stage.Stage;
import me.snizzle.game.Renderable;

public class ScrabbleGUI implements ScrabbleGameLogic.Importer, ScrabbleGameLogic.Exporter, Renderable{
    Stage gameStage;

    public ScrabbleGUI(Stage gameStage){
        this.gameStage = gameStage;
    }

    //@Override
    public void render() {

    }

    @Override
    public boolean timeToFetchData() {
        return false;
    }
}
