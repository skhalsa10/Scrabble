package me.snizzle.scrabble;

import me.snizzle.game.GameLogic;
import me.snizzle.game.GameLoop;
import me.snizzle.game.Renderable;

public class ScrabbleGameLoop extends GameLoop {
    private GameLogic gameLogic;
    private Renderable gui;

    public ScrabbleGameLoop(GameLogic gameLogic, Renderable gui){
        this.gameLogic = gameLogic;
        this.gui = gui;
    }



    @Override
    public void handle(long now) {
        gameLogic.export((GameLogic.Exporter)gui);
        gui.render();
        gameLogic.step();
    }
}
