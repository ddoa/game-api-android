package android.gameengine.icadroids.objects.collisions;

import java.util.List;
import java.util.Vector;

import android.gameengine.icadroids.objects.graphics.Sprite;
import android.gameengine.icadroids.tiles.GameTiles;
import android.gameengine.icadroids.tiles.Tile;
import android.graphics.Rect;
import android.util.FloatMath;

/**
 * CollidingObject holds methods to calculate tile collisions. It's also the
 * class that calls the collisionOccured() method.
 * 
 * @author Bas van der Zandt
 * 
 */
public class CollidingObject {

	/**
	 * Get a tile on a specific x and y position in the game world
	 * 
	 * @param xPosition
	 *            x position of the tile
	 * @param yPosition
	 *            y position of the tile
	 * @param gameTiles
	 * @return The Tile object at the given x and y position
	 */
	public Tile getTileOnPosition(int xPosition, int yPosition,
			GameTiles gameTiles) {

		if (gameTiles.tileArray != null) {

			int tiley = yPosition / gameTiles.tileSize;
			int tilex = xPosition / gameTiles.tileSize;

			if (tiley >= 0 && tiley < gameTiles.tileArray.length) {
				if (tilex >= 0 && tilex < gameTiles.tileArray[tiley].length) {
					return gameTiles.tileArray[tiley][tilex];
				}
			}
		}
		return null;
	}

	/**
	 * <b> This method is automatically called by a MoveableGameObject </b>
	 * 
	 * This method calculates if there has been an collision when the object has
	 * moved.
	 * 
	 * It calculates its collision by adding a collision box around it. This
	 * collision box includes it's current position and it's next position. This
	 * has the benefit that, when a object travels very fast, it will not pass
	 * through the tile.
	 * 
	 * This method will call collisionOccurred();
	 * 
	 * @param startX
	 *            The current x position of the object
	 * @param startY
	 *            The current y position of the object
	 * @param endX
	 *            The next x position of the object
	 * @param endY
	 *            The next y position of the object
	 * @param sprite
	 *            The sprite of the object
	 * @param gameTiles
	 *            The gameTiles object to check the collision with
	 * @param collisionObject
	 *            An ICollision object that needs to be called when a collision
	 *            has occurred
	 */
	public void calculateCollision(double startX, double startY, double endX,
			double endY, Sprite sprite, GameTiles gameTiles,
			ICollision collisionObject) {

		Rect AABB = createCollisionAABB(startX, startY, endX, endY, sprite);
		// create an collision box around the gameobject

		List<Tile> collidedTiles = findTilesAt(AABB, false, gameTiles);
		// loop through all the tiles to find the collided tiles

		if (collidedTiles.size() > 0) {
			collisionObject.collisionOccurred(collidedTiles);
			// Tile found? collision occurred
		}
	}

	/**
	 * Create a collision AABB box
	 * 
	 * @param startX
	 *            Start x position of the object
	 * @param startY
	 *            Start y position of the object
	 * @param endX
	 *            End x position of the object
	 * @param endY
	 *            End y position of the object
	 * @param sprite
	 *            The sprite of the object
	 * @return An collision AABB box
	 */
	private Rect createCollisionAABB(double startX, double startY, double endX,
			double endY, Sprite sprite) {

		Rect AABB = new Rect();

		if (startX <= endX) {
			AABB.left = (int) startX;
			AABB.right = (int) endX;
		} else {
			AABB.right = (int) startX;
			AABB.left = (int) endX;
		}

		if (startY <= endY) {
			AABB.top = (int) startY;
			AABB.bottom = (int) endY;
		} else {
			AABB.bottom = (int) startY;
			AABB.top = (int) endY;
		}

		if (sprite != null) {
			AABB.right += sprite.getFrameWidth();
			AABB.bottom += sprite.getFrameHeight();
		}
		return AABB;
	}

	/**
	 * Calculates the angle between an object and a tile
	 * 
	 * @param startX
	 *            X position of the object
	 * @param startY
	 *            Y position of the object
	 * @param tile
	 *            A tile
	 * @param sprite
	 *            The sprite of the object. When the object has no sprite, use
	 *            null
	 * @return The angle between the object and the tile
	 */
	public double calculateCollisionAngle(double startX, double startY,
			Tile tile, Sprite sprite) {

		int tileSize = tile.getGameTiles().tileSize;

		int tileX = (tile.getTileNumberX() * tileSize) + (tileSize / 2);
		int tileY = (tile.getTileNumberY() * tileSize) + (tileSize / 2);

		if (sprite != null) {
			startX += sprite.getFrameWidth() / 2;
			startY += sprite.getFrameWidth() / 2;
		}

		double deltaX = tileX - startX;
		double deltaY = tileY - startY;

		if (deltaX >= 0 || deltaY >= 0) {
			return Math.toDegrees(Math.atan2(deltaY, deltaX)) + 90;
		} else {
			return Math.toDegrees((Math.atan2(deltaY, deltaX))) + 450;
		}
	}

	/**
	 * This function should finds tiles inside a given rectangle
	 * 
	 * @param rectangle
	 *            the rectangle specified in a left, top to bottom,right
	 *            location
	 * @param returnInvisible
	 *            When true, the tiles that have tiletypes lower than 0 will be
	 *            included.
	 * @return the list of tile objects found at the location. The size is zero
	 *         when nothing is found.
	 */
	public final List<Tile> findTilesAt(Rect rectangle,
			boolean returnInvisible, GameTiles gameTiles) {

		float deltaX = rectangle.width();
		float deltaY = rectangle.height();

		float numberOfTilesX = FloatMath.ceil(((float) gameTiles.tileSize)
				/ deltaX);
		float numberOfTilesY = FloatMath.ceil(((float) gameTiles.tileSize)
				/ deltaY);

		float pixelsCheckX = deltaX / numberOfTilesX;
		float pixelsCheckY = deltaY / numberOfTilesY;

		List<Tile> foundTiles = new Vector<Tile>();

		for (int i = (rectangle.left); i <= rectangle.right; i = (int) (i + pixelsCheckX)) {
			for (int j = (rectangle.top); j <= rectangle.bottom; j = (int) (j + pixelsCheckY)) {
				Tile tileCollided = getTileOnPosition(i, j, gameTiles);
				if (tileCollided != null) {
					if ((tileCollided.getTileType() > -1 && !returnInvisible)
							|| (returnInvisible)) {
						if (!foundTiles.contains(tileCollided)) {
							foundTiles.add(tileCollided);
						}
					}
				}
			}
		}
		return foundTiles;
	}

}
