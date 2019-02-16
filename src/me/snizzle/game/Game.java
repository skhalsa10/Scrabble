package me.snizzle.game;

import javafx.stage.Stage;

/**
 *
 * I realized I keep making games for this class. I figured this would be the perfect place
 * to practice a Factory. Alas the game abstract class. It includes a logic which represents the game.
 * It also contains a game loop mechanism (this loop is not yet right.) it also includes a renderer that links up
 * to the gameLogic object exporter and importer to transfer state from the logic to the gui. The gui also
 * acts as a way to collect information. There may be a way to abstract this out but i have not figured it out yet.
 *
 */
public abstract class Game {
    protected GameLogic game;
    protected GameLoop gameLoop;
    protected Renderable gui;
    protected Stage gameStage;

    public Game(Stage gameStage){
        this.gameStage = gameStage;
        this.createGame();

    }

    /**
     * this is the factory magic ... I think :) We will see if I can figure it out.
     */
    public abstract void createGame();
}
