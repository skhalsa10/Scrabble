package me.snizzle.game;

import javafx.animation.AnimationTimer;

/**
 * this defines a looping behavior. Until we create our own threads we are still using the AnimationTimer
 * to move through time. this class is just a wrapper.
 */
public abstract class GameLoop extends AnimationTimer {

}
