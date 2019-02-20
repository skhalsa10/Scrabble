package me.snizzle.scrabble;

import me.snizzle.game.GameLogic;
import me.snizzle.game.GameLoop;
import me.snizzle.game.Renderable;

/**
 * this has been a common theme through out the class so I abstracted this gameloop pattern out for  use
 * with the gamefactory. this currently only works with GUIs but we could add a text based abstraction that can be used
 * the renderable will just write to a text output. IF ANIMATION IS NEEDED the speed at witch the handle runs will need to be controlled
 * to a constant frame rate.
 */
public class ScrabbleGameLoop extends GameLoop {
    private GameLogic gameLogic;
    private Renderable gui;

    public ScrabbleGameLoop(GameLogic gameLogic, Renderable gui){
        this.gameLogic = gameLogic;
        this.gui = gui;
    }


    /**
     * the general idea behind how this will be used is that we export the current state  to the gui.
     * the gui then renders the state. the state steps to a new version using step. this involves a computer move
     * which does not require to the gui or it requires the gui for the human moves. the gui will take its time building
     * up a step of human changed state  when it is ready to import to the logic it will flip a switch that triggers the import.
     * @param now
     */
    @Override
    public void handle(long now) {
        gameLogic.export((GameLogic.Exporter)gui);
        gui.render();
        gameLogic.step();
    }
}
