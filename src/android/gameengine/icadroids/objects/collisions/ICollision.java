package android.gameengine.icadroids.objects.collisions;

import java.util.List;

import android.gameengine.icadroids.tiles.Tile;

/**
 * Every object that wants <b> Tile </b> collision needs to implement this
 * interface. A MoveableGameObject implements this method by itself, so instead
 * of implement, override this method when you're extending MoveableGameObject.
 * 
 * @author Bas van der Zandt
 * 
 */
public interface ICollision {
	/**
	 * This method will be triggered when there has been a collision with a
	 * tile.
	 * 
	 * Note: sometimes you must use <b> undoMove(); </b> , or the object will
	 * get stuck into the wall!
	 * 
	 * @param collidedTiles
	 *            Every collided tile will be provided in this list. You can
	 *            directly manipulate the tiles, or, if you want to know which
	 *            side the object has collided with the tile, use the method
	 *            getCollisionSide(); .
	 */
	void collisionOccurred(List<Tile> collidedTiles);
}
