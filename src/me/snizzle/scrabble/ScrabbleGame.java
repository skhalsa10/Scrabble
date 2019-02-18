package me.snizzle.scrabble;

import javafx.stage.Stage;
import me.snizzle.game.Game;

/**
 * this defines the scrabble game. it is created with the game factory
 */
public class ScrabbleGame extends Game {

    public ScrabbleGame(Stage gameStage) {
        super(gameStage);
    }


    /**
     * this is the magic for a Scrabble specific game
     */
    @Override
    public void createGame() {
        gui = new ScrabbleGUI(gameStage);
        gameLogic = new ScrabbleGameLogic((ScrabbleGameLogic.Importer) gui);
        gameLoop = new ScrabbleGameLoop(gameLogic,gui);

    }
}
