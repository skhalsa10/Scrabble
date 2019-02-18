package me.snizzle.game;

import javafx.stage.Stage;
import me.snizzle.scrabble.ScrabbleGame;

/**
 * I have been making a lot of different games for this class so far. So I figured I
 * might as well build a GameFactory. I can then create a gamelauncher that is a GUI that allows any game
 * that I make in this class to be launched. The goal is to have a showcase of all games. I will need to remodify
 * the Domino game to make use of the Game Factory but...
 */
public class GameFactory {

    /**
     * Here we have the function that is used to build the type of game wanted.
     *
     * thisneeds a stage to render to. my idea is to make a second window with a new stage this can be closed to
     * get back to the original gamelauncher GUI
     *
     * @param game the ENUM of the GameType that is wanted
     * @param gameStage this will be the GUI stage
     * @return an instance of the GameType requested rendered to the given stage. or NULL if incorrect gametype.
     */
    public static Game getGame(GameType game, Stage gameStage){
        switch(game){
            case SCRABBLE : {
                return new ScrabbleGame(gameStage);
            }
            case DOMINO: {
                return null;
            }
            default: {
                return null;
            }
        }
    }
}
