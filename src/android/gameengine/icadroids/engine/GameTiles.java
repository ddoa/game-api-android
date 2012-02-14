/**
 * 
 */
package android.gameengine.icadroids.engine;

import java.util.Vector;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.gameengine.icadroids.renderer.GameView;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * The class that makes the tiles out of a tileArray. 
 * 
 * First, intialize the tiletypes with GameTiles.tiletypes, than set the
 * tilesize with GameTiles.tileTypes, than make a tile Byte[][] map with
 * the numbers corresponding the position of the array 'tileTypes'. -1 is
 * no tile. Than add the map the map with 'addTileMap(map, 0, 0)'.
 * 
 * @author Edward van Raak 
 */
public class GameTiles {
	/***
	 * If set to true, debug mode will show tile collision borders. Usefull for
	 * testing collisions without sprites. Might slow down FPS quite a bit. 
	 */
	public static boolean debugMode = false;
	/***
	 * Array that holds different TileTypes with each it's own sprites and
	 * behavior.
	 */
	public static Sprite[] tileTypes = new Sprite[100];
	/***
	 * Array that holds booleans that tell whenever the given
	 * TileTypes are collided. 
	 */
	//public static Boolean[] TileCollided;
	/***
	 * Two dimensional array that holds the location of all tiles.
	 */
	public static byte tileArray[][];
	/***
	 * Integer that holds the current location of the tile type that is going to
	 * be added to the TileType array.
	 */
	private int currentTileType = 0;
	/***
	 * GameView is used to access tile and debug tile rendering.
	 */
	private GameView view;
	/***
	 * GameEngine is used to acess item lists and canvas instance.
	 */
	private GameEngine gameEngine;
	/***
	 * The width and height of every tile. Default value set to 50.
	 */
	public static int tileSize = 50;
	/***
	 * List that holds all the retangle boxes that will be used for collision
	 * detection
	 */
	//public static byte TileRectArray[][];	
	public static RectF tileRectArray[][];
	public static byte tileTypeArray[][];
	public static Vector<RectF> tileBoxes = new Vector<RectF>();
	public static Vector<Integer> tileBoxID = new Vector<Integer>();
	
	private static int mapStartX;
	private static int mapStartY;
	public static int tileMapHeight = 0;
	public static int tileMapWidth = 0;
	private static int mapHeight;
	private static int mapWidth;

	
	/***
	 * This class is used to create TileBased games.
	 * 
	 * @param gameEngine
	 *            GameEngine
	 * @param View
	 *            The GameView Renderer
	 * @param tileMap
	 *            The 2D Map Array
	 * @param TileMapWidth
	 *            The total Width of the map array
	 * @param TileMapHeight
	 *            The total height of the map array
	 * @param xPosition
	 *            The start position X of where to start drawing the tiles.
	 * @param yPostition
	 *            The start position Y of where to start drawing the tiles.
	 */
	public GameTiles(GameEngine gameEngine, GameView View, byte[][] tileMap,
			int TileMapWidth, int TileMapHeight, int xPosition, int yPostition) {
		this.view = View;
		this.gameEngine = gameEngine;
		GameTiles.mapStartX = xPosition;
		GameTiles.mapStartY = yPostition;
	}
	
	/** 
	 * Initialise the tile map, add rectangles and set the tile array.
	 * @param gameEngine
	 * @param View
	 * @param TileMap
	 * @param xPosition
	 * @param yPostition
	 * @return
	 */
	protected static GameTiles addTileMap(GameEngine gameEngine, GameView View,
		byte[][] TileMap, int xPosition, int yPostition) {
		GameTiles tiles = null;
		
		/**
		 * Automaticly check how big the map is
		 */
		for (int i=0; i < TileMap.length ; i++)
		{
			if(tileMapHeight < TileMap.length)
			{
				tileMapHeight = TileMap.length;
			}			
			for (int j=0; j < TileMap[i].length ; j++)
			{
				if(tileMapWidth < TileMap[i].length)
				{
				tileMapWidth = TileMap[i].length;
				}
			}
		}		
		Log.d("GameTiles", "new GameTiles now");
		Log.d("GameTiles", "WIDTH " + tileMapWidth);
		Log.d("GameTiles", "HEIGHT " + tileMapHeight);
		tileRectArray = new RectF[tileMapHeight][tileMapWidth];		
		tiles = new GameTiles(gameEngine, View, TileMap, tileMapWidth,
				tileMapHeight, xPosition, yPostition);
		setTileArray(TileMap);
		initTileCollisionRect();
		//TileCollided = new Boolean[TileTypes.length];		
		return tiles;
	}

	/***
	 * Initialise and place every collision rectangle on every tile.
	 * 
	 * @param x
	 *            The X location of the topleft of the retangle
	 * @param y
	 *            The Y location of the topleft of the retangle
	 */
	private static RectF placeCollisionRectangle(int x, int y) {
		RectF r = new RectF(x, y, x + tileSize, y + tileSize);
		tileBoxes.add(r);	
		return r;
	}
	
	/**
	 * Not used method, need future implementation.
	 * @return
	 */
	@SuppressWarnings("unused")
	private Rect getRectangleAtLocation()
	{
		Rect r = null;		
		return r;		
	}

	/***
	 * Loop through the TileArray to create collision retangles.
	 */
	private static void initTileCollisionRect() {
		for (int i = 0; i < tileArray.length; i++) {
			for (int j = 0; j < tileArray[i].length; j++) {
				tileRectArray[i][j] = placeCollisionRectangle(j * tileSize + mapStartY, i * tileSize + mapStartX);
				//TileTypeArray[x][y] =  TileArray[i][j]; //omdraaien?
				 //System.out.println("x!: " +  x + " " + "y!: " + y);	
					 	
				tileBoxID.add((int) tileArray[i][j]);	//magic 				
			}
		}
	}

	/***
	 * Loop through the TileArray to create Tiles. The TileType is checked on
	 * every loop so that the correct sprite is drawn.
	 */
	public void DrawTiles()
	{	
		int vectorpos = 0;
		Canvas c = gameEngine.getGameloop().c;
		if(!debugMode)
		{
		for (int i=0; i < tileArray.length ; i++)
		{
			   for (int j=0; j < tileArray[i].length ; j++)
			   {			 
				  if(tileArray[i][j] != -1)				
				  {
					view.drawTile(c, tileTypes[tileArray[i][j]], j*tileSize + mapStartX, i*tileSize + mapStartY);	
				  }						 		  
			   }			   	
		}
		}
		if(debugMode) /** Draws tiles in the form of lines, so you can test collision without tiles.  Slows down game be carefull**/
		  {
			for (int i=0; i < tileArray.length ; i++)
			{
				for (int j=0; j < tileArray[i].length ; j++)
				{				  
					 view.drawDebugTiles(c, tileBoxes.get(vectorpos).left,tileBoxes.get(vectorpos).top, tileSize);				  
					 vectorpos++;	
				}				  
			}	
		  }
	}

	/***
	 * Very slow collision detection from Tile Side. Every game loop, every tile
	 * checks collision for every item. Testing purposes only because of obvious
	 * slowdown.
	 * 
	 * @deprecated
	 * @param collisionRect
	 */
	@SuppressWarnings("unused")
	private void checkCollisionForAllTiles() {
		for (int i = 0; i < tileBoxes.size(); i++) {
			for (int j = 0; j < GameEngine.items.size(); j++) {
				if (RectF.intersects(tileBoxes.get(i),
						GameEngine.items.get(j).rectangle)) {
					Log.d("Tile Collision Test", "COLLISION DETECTED!");
				}
			}
		}

	}

	/***
	 * Change a tile in the game world.
	 * <p/>
	 * Notes
	 * <p/>
	 * The x and y positions are the indices in the tile-array, not the x and y
	 * positions in the world. <br/>
	 * A negative value for the tilenumber removes the tile at the given
	 * position. <br/>
	 * 
	 * @param xTile
	 *            the y-pos of the tile in the tile array
	 * @param yTile
	 *            the y-pos of the tile in the tile array
	 * @param tileType
	 *            the new sprite type that should replace this tile
	 */
	public static void changeTile(int xTile, int yTile, int tileType) {
		tileArray[xTile][yTile] = (byte) tileType;
	}

	/**
	 * Delete a tile in the game world.
	 * <p/>
	 * Notes
	 * <p/>
	 * The x and y positions are the indices in the tile-array, not the x and y
	 * positions in the world. <br/>
	 * 
	 * @param xTile
	 *            xTile the y-pos of the tile in the tile array
	 * @param yTile
	 *            yTile the y-pos of the tile in the tile array
	 */
	public static void deleteTile(int xTile, int yTile) {
		tileArray[xTile][yTile] = -1;
	}

	public void AddTileType(Sprite sprite) {
		tileTypes[currentTileType] = sprite;
		currentTileType++;
	}

	/**
	 * @return the tileTypes
	 */
	public Sprite[] getTileTypes() {
		return tileTypes;
	}

	/**
	 * @param tileTypes
	 *            the tileTypes to set
	 */
	public void setTileTypes(Sprite[] tileTypes) {
		GameTiles.tileTypes = tileTypes;
	}

	/**
	 * @return the tileArray
	 */
	public byte[][] getTileArray() {
		return tileArray;
	}

	/**
	 * @param tileArray
	 *            the tileArray to set
	 */
	public static void setTileArray(byte[][] tileArray) {
		GameTiles.tileArray = tileArray;
	}
	
	/**
	 * Get the height of the Tilemap
	 * @return the height of the map
	 */
	public int getMapHeight() {
		return mapHeight * tileSize;
	}
	
	/**
	 * Get the width of the Tilemap
	 * @return the width of the tilemap
	 */
	public int getMapWidth() {
		return mapWidth * tileSize;
	}
	  
}
