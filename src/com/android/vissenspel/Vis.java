package com.android.vissenspel;

import java.util.ArrayList;
import java.util.List;

import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.input.TouchInput;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.objects.collisions.ICollision;
import android.gameengine.icadroids.objects.collisions.TileCollision;
import android.util.Log;

/**
 * Vis is the player object of the game.
 * It swims through the game, eats strawberries and avoids the monster
 * (although right now, nothing bad happens when the monster gets you, just i.b.)
 * 
 * @author Paul Bergervoet
 */
public class Vis extends MoveableGameObject implements ICollision {

    /**
     * Total score from strawberries eaten
     */
    private int score;

    /**
     * Constructor of Vis
     */
    public Vis() {
	setSprite("fishframes", 4);
	setFriction(0.05);

	setSpeed(8);
	setDirection(90);

	score = 0;
	// might also be in intialization of game
	TouchInput.use = false;
	OnScreenButtons.use = true;
    }

    /**
     * update 'Vis': handle collisions and input from buttons
     * 
     * @see android.gameengine.icadroids.objects.MoveableGameObject#update()
     */
    @Override
    public void update() {
	super.update();
	// collisions with objects
	ArrayList<GameObject> gebotst = getCollidedObjects();
	if (gebotst != null) {
	    for (GameObject g : gebotst) {
		if (g instanceof Strawberry) {
		    score = score + ((Strawberry) g).getPoints();
		    Log.d("hapje!!!", "score is nu " + score);
		    g.deleteThisGameObject();
		} else if (g instanceof Monster) {
		    Log.d("Gepakt", "Ai, wat nu...");
		}
	    }
	}
	// input from OnScreenButtons, now just 4 directional buttons
	if (OnScreenButtons.dPadUp) {
	    setDirectionSpeed(0, 8);
	    setFrameNumber(1);
	}
	if (OnScreenButtons.dPadDown) {
	    setDirectionSpeed(180, 8);
	    setFrameNumber(3);
	}
	if (OnScreenButtons.dPadRight) {
	    setDirectionSpeed(90, 8);
	    setFrameNumber(0);
	}
	if (OnScreenButtons.dPadLeft) {
	    setDirectionSpeed(270, 8);
	    setFrameNumber(2);
	}
    }

    /**
     * Handle tile collisions: Vis bounces off tiles of type 1 only.
     * @see android.gameengine.icadroids.objects.collisions.ICollision#collisionOccurred(java.util.List)
     */
    @Override
    public void collisionOccurred(List<TileCollision> collidedTiles) {
	// Do we know for certain that the for-each loop goes through the list front to end?
	// If not, we have to use a different iterator!
	for (TileCollision tc : collidedTiles) { 
	    if (tc.theTile.getTileType() == 1) {
		moveUpToTileSide(tc);
		setSpeed(0);
		return; 		// might be considered ugly by some colleagues...
	    }
	}
    }

}
