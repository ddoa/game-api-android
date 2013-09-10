package com.android.vissenspel;

import android.gameengine.icadroids.objects.*;
import android.util.Log;

/**
 * Strawberries are eaten by the Vis and give points. Strawberries have a
 * limited life span. 
 * 
 * @author Paul Bergervoet
 */
public class Strawberry extends GameObject {

    /**
     * Life span of strawberry. Right now is set at 250, may be randomized by controler
     */
    private static int maxAge = 250;

    /**
     * Points of strawberry. Right now is set at 50, may be randomized by controler
     */
    private int points;

    /**
     * Age of strawberry. Update() will count the cycles
     */
    private int age;

    /**
     * reference to the game
     */
    private Vissenkom mygame;

    /**
     * Constructor of Strawberry
     * Points are (as yet) randomized between 25 & 75
     * 
     * @param spel reference to game.
     */
    public Strawberry(Vissenkom spel) {
	mygame = spel;
	setSprite("strawberry");
	points = 25 + (int) (50 * Math.random());
	age = 0;
    }

    /**
     * Update Strawberry every cycle of the game loop.
     * Cout the age, and remove if too old.
     * 
     * @see android.gameengine.icadroids.objects.GameObject#update()
     */
    @Override
    public void update() {
	super.update();

	age++;
	if (age > maxAge) {
	    mygame.deleteGameObject(this);
	    //Log.d("Aard", "aardbei dood " + age);
	}
    }

    /**
     * Get the points of this Strawberry
     * 
     * @return value of this Strawberry
     */
    public int getPoints() {
	return points;
    }

}
