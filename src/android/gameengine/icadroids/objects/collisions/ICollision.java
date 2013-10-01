package android.gameengine.icadroids.objects.collisions;

import java.util.List;

/**
 * Every MoveableGameObject that wants to do something with tile collisions 
 * needs to implement this interface. The GameEngine will check for tile collisions 
 * for MoveableGameObjects that implement this interface. 
 * It will do <b>no</b> checking for MoveableGameObjects that don't implement 
 * this interface. This increases performance of the game.<br />
 * Tile collision is totally separate from collision with other GameObjects, 
 * which can always be checked by calling the getCollidedObjects() method
 * in your update() of the MoveableGameObject. <br />
 * <br />
 * Note: It is useless to let classes other than MoveableGameObjects implement
 * this interface. Nothing will happen if you do ;-)!
 * 
 * @author Bas van der Zandt, Paul Bergervoet
 * 
 */
public interface ICollision {
	/**
	 * This method will be triggered when there have been collisions with tiles.
	 * If there are no tile collisions, this method will not be called, so the list
	 * will not be empty or null. (As opposed to the list of colliding<b>Object</b>s)
	 * <br />
	 * The list contains objects of the type TileCollision. These TileCollisions
	 * contain the Tile you hit, plus an int indicating the side of the Tile
	 * (TOP, BOTTOM, LEFT or RIGHT).
	 * <br />
	 * Tile collisions are calculated along the 'line of the move'. So if the object
	 * is moving fast and passing several tiles in one move, the tile it hits first
	 * will be the first in the list. You may hit several tiles at the same time,
	 * especially if the size of the object is bigger than the size of the Tile, these
	 * tile collisions are in the list in random order. 
	 * <br />
	 * The picture below shows the
	 * tile collisions that will be found when a green objects passes over a background
	 * of white tiles at great speed. First, tiles 1-3 will be hit at the left side, then tiles
	 * 4-5 also on the left side, then tiles 6-7 on their bottom side, etc.
	 * <br />
	 * <p><img src="tilecollision.png"</p>
	 * <br />
	 * Collision detection <b>does not change the position</b> 
	 * of the MoveableGameObject. If you don't do anything, objects will fly through
	 * walls. Use methods like undoMove, bounce, moveUpToTileSide to react.
	 * Typically, you scan the list front to end and take action on the first collision
	 * that is interesting and discard the rest.
	 * 
	 * @param collidedTiles List of TileCollision holding all tile collisions in this move.
	 * 
	 * @see android.gameengine.icadroids.objects.collisions.TileCollision
	 * @see android.gameengine.icadroids.objects.MoveableGameObject#undoMove()
	 * @see android.gameengine.icadroids.objects.MoveableGameObject#bounce(TileCollision)
	 * @see android.gameengine.icadroids.objects.MoveableGameObject#moveUpToTileSide(TileCollision)
	 */
	public void collisionOccurred(List<TileCollision> collidedTiles);
}
