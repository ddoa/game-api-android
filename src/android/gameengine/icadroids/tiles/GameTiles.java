/**
 * 
 */
package android.gameengine.icadroids.tiles;

import java.util.Vector;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.graphics.Canvas;

/**
 * 
 * Gametiles are especially useful if you want to quickly build a level/map
 * (walls, floor, etc) for a platform- or boardgame.
 * 
 * This class makes for every tile a seperated object and place it on the right
 * position. The tile objects can be manipulated separately.
 * 
 * @author Edward van Raak & Bas van der Zandt
 */
public class GameTiles {
	/***
	 * Array that holds different TileTypes with each it's own sprites and
	 * behavior.
	 */
	public Vector<Sprite> tileTypes = new Vector<Sprite>();
	/***
	 * Two dimensional array that holds the location of all tiles.
	 */
	private Tile tileArray[][];
	/***
	 * The width and height of every tile. Default value set to 50.
	 */
	public int tileSize = 50;

	/**
	 * The width in pixels of the tilemap
	 */
	private int mapWidth = 0;
	/**
	 * The height in pixels of the tilemap
	 */
	private int mapHeigth = 0;

	/**
	 * Make a new tilemap object with the given tile resources, the tilemap and
	 * the tile size
	 * 
	 * @param tileResources
	 *            a list of string with the names of the images you want to use
	 *            in the tilemap. This images have only the name of the images
	 *            without the extension! For example: new String[] = {"tile1",
	 *            "tile2", "tile3"};
	 * @param map
	 *            An 2d Array of the overlay map. The numbers are the position
	 *            of the tileResources, starting at 0. -1 means invisible Tile.
	 *            Example: new Int[][] = {{0,-1,2,2,-1},{1,1,-1,2,-1}}
	 * @param tileSize
	 *            The size of a tile. This size is the width and height of a
	 *            tile.
	 */
	public GameTiles(String[] tileResources, int[][] map, int tileSize) {
		addTileResources(tileResources);
		addTileMap(map, tileSize);
	}

	/**
	 * This overloaded constructor will make an GameTile object with an <b>
	 * empty </b> tilemap.
	 * 
	 * @param tileSize
	 */
	public GameTiles(int tileSize) {
		this.tileSize = tileSize;
	}

	/**
	 * Add / replace the current tile map. This method will also convert the
	 * given int[][] map to a Tile[][] map.
	 * 
	 * @param map
	 *            An 2d Array of the overlay map. The numbers are the position
	 *            of the tileResources, starting at 0. -1 means invisible Tile.
	 *            Example: new Int[][] = {{0,-1,2,2,-1},{1,1,-1,2,-1}}
	 * @param tileSize
	 *            The size of a tile. This size is the width and height of a
	 *            tile.
	 */
	public void addTileMap(int[][] map, int tileSize) {

		tileArray = new Tile[map.length][];

		for (int i = 0; i < tileArray.length; i++) {
			tileArray[i] = new Tile[map[i].length];
		}

		for (int i = 0; i < tileArray.length; i++) {
			for (int j = 0; j < tileArray[i].length; j++) {
				int tileIndex = map[i][j];
				if (tileIndex > -1) {
					tileArray[i][j] = new Tile(tileIndex, this);
					tileArray[i][j].setTileIndex(i, j);
				}

			}

		}

		this.tileSize = tileSize;
		calculateMapWidthHeigth(tileArray);

	}

	/***
	 * Loop through the TileArray to draw the Tiles. The TileType is checked on
	 * every loop so that the correct sprite is drawn.
	 */
	public void drawTiles(Canvas c) {
		if (tileArray != null) {
			for (int i = 0; i < tileArray.length; i++) {
				for (int j = 0; j < tileArray[i].length; j++) {
					if (tileArray[i][j] != null) {
						c.drawBitmap(
								tileTypes.get(tileArray[i][j].getTileType())
										.getSprite(), j * tileSize, i
										* tileSize, null);
					}
				}
			}
		}
	}

	/***
	 * Change a tile in the game world, add or remove a tile.
	 * <p/>
	 * Notes:
	 * <p/>
	 * The x and y positions are the indices in the tile array. <br/>
	 * The value -1 for the tileType removes the tile at the given position.<br /> 
	 * If you specify a position that was previously empty,
	 * a new Tile will be created.<br/>
	 * 
	 * @param yTile
	 *            the y-pos of the tile in the tile array
	 * @param xTile
	 *            the y-pos of the tile in the tile array
	 * @param tileType
	 *            the new sprite type that should replace this tile
	 */
	public void changeTile(int xTile, int yTile, int tileType) {
		if(tileType < 0){
			tileArray[yTile][xTile] = null;
		}
		else if(tileArray[yTile][xTile] != null){
			tileArray[yTile][xTile].setTileType(tileType);
		} else {
			tileArray[yTile][xTile] = new Tile(tileType, this);
		}
		
	}

	/**
	 * @return the tileTypes
	 */
	public Vector<Sprite> getTileTypes() {
		return tileTypes;
	}

	/**
	 * @param tileTypes
	 *            the tileTypes to set
	 */
	public void setTileTypes(Vector<Sprite> tileTypes) {
		this.tileTypes = tileTypes;
	}

	/**
	 * Get the list with all the Tile objects in it.
	 * @return the tileArray
	 */
	public Tile[][] getTileArray() {
		return tileArray;
	}

	/**
	 * Add resources to the list
	 * 
	 * @param resourceNames
	 *            all resources you want to add to the list
	 */
	public void addTileResources(String[] resourceNames) {
		for (int i = 0; i < resourceNames.length; i++) {
			tileTypes.add(new Sprite(resourceNames[i]));
		}
	}

	/**
	 * Get the height of the map
	 * 
	 * @return Map height
	 */
	public int getMapHeigth() {
		return mapHeigth;
	}

	/**
	 * Get the width of the map
	 * 
	 * @return Map width
	 */
	public int getMapWidth() {
		return mapWidth;
	}

	/**
	 * Calculate the size of the map based on the tile size and the longest
	 * array in the 2d array.
	 * 
	 * @param tileArray
	 *            the 2d array to calculate the size
	 */
	private void calculateMapWidthHeigth(Tile[][] tileArray) {
		int maxSizeX = 0;
		for (int i = 0; i < tileArray.length; i++) {
			if (tileArray[i].length > maxSizeX) {
				maxSizeX = tileArray[i].length;
			}
			mapWidth = maxSizeX * tileSize;
			mapHeigth = tileArray.length * tileSize;

		}
	}
	
	/**
	 * Get a tile on a specific x and y position in the game world
	 * 
	 * @param xPosition
	 *            x position of the tile
	 * @param yPosition
	 *            y position of the tile
	 * @return The Tile object at the given x and y position
	 */
	public Tile getTileOnPosition(int xPosition, int yPosition) {
			int tiley = yPosition / tileSize;
			int tilex = xPosition / tileSize;

			return getTileOnIndex(tilex, tiley);
	}
	
	/**
	 * get a Tile on a specified index number of the tile list
	 * @param xIndex The x index of the list
	 * @param yIndex The y index of the List
	 * @return The tile Object at the given index position
	 */
	public Tile getTileOnIndex(int xIndex, int yIndex) {

		if (tileArray != null) {

			if (yIndex >= 0 && yIndex < tileArray.length) {
				if (xIndex >= 0 && xIndex < tileArray[yIndex].length) {
					return tileArray[yIndex][xIndex];
				}
			}
		}
		return null;
	}
	
	
	
}
