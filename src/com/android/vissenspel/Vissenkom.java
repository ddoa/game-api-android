package com.android.vissenspel;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.tiles.GameTiles;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.util.Log;

public class Vissenkom extends GameEngine {
	Vis vis;
    
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		
		createTileEnvironment();
		
		vis = new Vis();
		addGameObject(vis, 100, 250);
		
		@SuppressWarnings("unused")
		StrawberryControler sc = new StrawberryControler(this);
	}
	
	private void createTileEnvironment()
	{
		
		String[] tileImagesNames = { "vissentile1", "vissentile1", "vissentile2" };
	    int[][] tilemap = 
	    {
	    		{2,  2,   2,  2, 2, 2, -1, -1, -1, -1, -1, 1, 2, 1, 2, 1, 1},
		        {2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1},
		        {2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1},
		        {2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1},
		        {2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1},
		        {2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1},
		        {-1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		        {-1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		        {-1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		        {-1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		        {1, 1, 1, 1, 1, 1, 2, 2, 2, 2, -1, -1, -1, -1, -1, -1, -1},
		        {1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1},
		        {1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1},
		        {1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1},
				{1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1},
		        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1},
		        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1},
		        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1},
		        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1},
		        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1},
				{1, 2, 1, 2, 1, 2, 1, 2, -1, -1, -1, -1, -1, -1, -1, 1, 2}
	    };
	    GameTiles myTiles = new GameTiles(tileImagesNames, tilemap, 10);
	    setTileMap(myTiles);
		Log.d("Vissenkom", "GameTiles created");

	}
	
	/** 
	 * @see android.gameengine.icadroids.engine.GameEngine#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}
}