package android.gameengine.icadroids.objects.collisions;

import android.gameengine.icadroids.tiles.Tile;

/**
 * TileCollision is a simple class used by collision detection to package the
 * data on a tile collision (that is: Tile & side of the tile where collision occurs). 
 * TileCollisions will be in the 'collided'-list that is passed on
 * by collision detection in the collisionOccurred-method
 * 
 * @author Paul Bergervoet
 *
 */
public class TileCollision {
	
	/**
	 * Constant indicating that <i>collisionSide</i> is the top of the tile
	 */
	public static final int TOP = 0;
	
	/**
	 * Constant indicating that <i>collisionSide</i> is the right side of the tile
	 */
	public static final int RIGHT = 1;
	
	/**
	 * Constant indicating that <i>collisionSide</i> is the bottom of the tile
	 */
	public static final int BOTTOM = 2;
	
	/**
	 * Constant indicating that <i>collisionSide</i> is the left side of the tile
	 */
	public static final int LEFT = 3;
	
	/**
	 * The tile involved in the collision
	 */
	public Tile theTile;
	
	/**
	 * The side of the tile onto which the Object has collided.<br />
	 * See constants for values.
	 */
	public int collisionSide;
	
	/**
	 * Create a simple TileCollision object.
	 * 
	 * @param t	The tile
	 * @param cs	The collisionSide
	 */
	public TileCollision(Tile t, int cs)
	{
		theTile = t;
		collisionSide = cs;
	}

}
