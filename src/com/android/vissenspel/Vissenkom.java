package com.android.vissenspel;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.tiles.GameTiles;
import android.util.Log;

/**
 * Main class of the game 'Vissenkom'.
 * 
 * @author Paul Bergervoet
 */
public class Vissenkom extends GameEngine {

    /**
     * MoveableGmeObject vis, player in the game
     */
    private Vis vis;

    /**
     * Initialize the game, create objects and level
     * 
     * @see android.gameengine.icadroids.engine.GameEngine#initialize()
     */
    @Override
    protected void initialize() {
	super.initialize();

	createTileEnvironment();

	vis = new Vis();
	addGameObject(vis, 120, 240);

	Monster engerd = new Monster(vis);
	addGameObject(engerd, 480, 240);

	@SuppressWarnings("unused")
	StrawberryControler sc = new StrawberryControler(this);
    }

    /**
     * Create background with tiles
     */
    private void createTileEnvironment() {
	String[] tileImagesNames = { "vissentile1", "vissentile2" };
	// layout: better not let the Eclipse formatter get at this...
	int[][] tilemap = {
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1,  1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1 },
		{ -1,  0,  0,  0,  1,  0,  0,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0, -1 },
		{ -1, -1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1, -1, -1,  0,  1,  0,  1,  0,  1,  0,  1,  0,  1,  0,  1,  0,  1, -1, -1, -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1, -1, -1,  1,  0,  1,  0,  1,  0,  1,  0,  1,  0,  1,  0,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1 },
		{ -1,  1,  1,  1,  0,  1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1,  1,  1,  1,  1,  1,  1,  1, -1 },
		{ -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1 },
		{ -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
	};
	GameTiles myTiles = new GameTiles(tileImagesNames, tilemap, 20);
	setTileMap(myTiles);
	Log.d("Vissenkom", "GameTiles created");
    }

    /**
     * Update the game. As yet, no extra actions, so this override might as well
     * be left out. Left for future additions
     * 
     * @see android.gameengine.icadroids.engine.GameEngine#update()
     */
    @Override
    public void update() {
	super.update();
    }
}