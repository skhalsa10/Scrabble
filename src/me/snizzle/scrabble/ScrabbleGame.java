package me.snizzle.scrabble;

import javafx.stage.Stage;
import me.snizzle.game.Game;
import me.snizzle.game.GameLoop;

/**
 * this defines the
 */
public class ScrabbleGame extends Game {

    public ScrabbleGame(Stage gameStage) {
        super(gameStage);
    }


    @Override
    public void createGame() {
        gui = new ScrabbleGUI(gameStage);
        gameLogic = new ScrabbleGameLogic((ScrabbleGameLogic.Importer) gui);
        gameLoop = new ScrabbleGameLoop(gameLogic,gui);

    }
}
