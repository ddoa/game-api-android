package android.gameengine.icadroids.objects.collisions;

import android.gameengine.icadroids.tiles.Tile;

/**
 * TileCollision is a simple class used by collision detection to package the
 * data on a tile collision. TileCollisions will be in the 'collided'-list that is passed on
 * by collision detection in the collisionOccurred-method
 * 
 * @author gebruiker
 *
 */
public class TileCollision {
	
	/**
	 * The tile involved in the collision
	 */
	public Tile theTile;
	
	/**
	 * The side of the tile onto which the Object has collided<br />
	 *  0 - Top, 1 - Right, 2 - Bottom, 3 - Left
	 */
	public int collisionSide;
	
	public TileCollision(Tile t, int cs)
	{
		theTile = t;
		collisionSide = cs;
	}

}
