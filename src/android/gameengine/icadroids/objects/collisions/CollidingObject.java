package android.gameengine.icadroids.objects.collisions;

import java.util.List;
import java.util.Vector;

import android.gameengine.icadroids.objects.graphics.Sprite;
import android.gameengine.icadroids.tiles.GameTiles;
import android.gameengine.icadroids.tiles.Tile;

/**
 * CollidingObject holds methods to calculate tile collisions. It's also the
 * class that calls the collisionOccured() method.
 * NOTE: main method for detection is very long and complicated. This can be improved
 * by making all its local variables global and then splitting up. Now it is no use, we would
 * have loads of parameteres and problems with return values, probably.
 * <br />
 * <b>This is a utility class vor the GameEngine. Game programmers will have no need
 * of this class! (Hide it, make it an inner class?)</b>
 * 
 * @author Bas van der Zandt, Paul Bergervoet
 * 
 */
public class CollidingObject {


	/**
	 * <b>This method is automatically called by a MoveableGameObject, do
	 * not call this yourself!</b>
	 * <br />
	 * This method calculates if there has been an collision <i>with tiles</i>
	 * when the object has moved. It will call the method 
	 * collisionOccurred(ArrayList<TileCollision>) if there are collisions
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
	 *     
	 * @see android.gameengine.icadroids.objects.collisions.ICollision#collisionOccurred(List)
	 */
	public void calculateCollision(double endX, double endY, 
			double startX, double startY, Sprite sprite, GameTiles gameTiles,
			ICollision collisionObject) 
	{	// no tiles, no sprite: no tile collisions
		if (gameTiles == null || sprite == null )
		{
			return; 
		}
		List<TileCollision> collidedTiles = new Vector<TileCollision>();
		
        int xTileIndex; 		// index of column of collided tiles (horizontal collision)
        int yTileIndex; 		// index of row of collided tiles (vertical collision)
        int collisionX; 			// Xpos of possible collision (gridline on tile grid)
        int collisionY; 			// Ypos of possible collision (gridline on tile grid)
        int itemXatCollision; 	// xpos of item at collision ( =collisionX, -width if collision on right side)
        int itemYatCollision; 	// ypos of item at collision ( =collisionY, -height if collision at bottom)
        double xFactor; 		// part of move that is done up to collision
        double yFactor; 		// part of move that is done up to collision
        int xCollisionSide;		// collisionSide for horizontal collisions, will be either 1 or 3
        int yCollisionSide;		// collisionSide for vertical collisions, will be either 0 or 2
        
        boolean moveleft = ( endX < startX );
        boolean moveup = ( endY < startY );
		int gridsize = gameTiles.tileSize;
		int objwidth = sprite.getFrameWidth();
		int objheight = sprite.getFrameHeight();

        // 1: Find gridlines ( x and y ) where collision occurs (if any).
        // 		Determine corresponding indexes in tilemap
        // 		Note: it is assumed gameTiles start at position (0,0), ref to coordinates of map commented out
        if ( moveleft ) 			// find gridline for horizontal collision
        { 	// find index of gridline just left of start-left side of item
            // -1: entire tile left of previous pos of object, we collide into right side
            xTileIndex = divdown(startX /*- mapStartX*/, gridsize) - 1;
            // x of collision is right side of tile (hence '+1')
            collisionX = (xTileIndex + 1) * gridsize /*+ mapStartX*/;
            // x of item equals collisionX because collision is on left side
            itemXatCollision = collisionX;
            // collision will be on right side of tile, because object is moving to the left
            xCollisionSide = 1;
        } else
        { 	// find index of gridline just right of start-right side of item
            xTileIndex = divdown(Math.ceil(startX) + objwidth -1 /*- mapStartX*/, gridsize) + 1;
            // x of collision is left side of tile
            collisionX = xTileIndex * gridsize /*+ mapStartX*/;
            // x of item equals collisionX-width because collision is on right side
            itemXatCollision = collisionX - objwidth;
            // collison will be on left side of tile, because object is moving to the right
            xCollisionSide = 3;
        }
        if ( moveup ) // vertical collision?? (comments ommitted, are like horizontal)
        {
            yTileIndex = divdown(startY /*- mapStartY*/, gridsize) - 1;
            collisionY = (yTileIndex + 1) * gridsize /*+ mapStartY*/;
            itemYatCollision = collisionY;
            yCollisionSide = 2;	// moving up, so hitting bottom of tile
        } else
        {
            yTileIndex = divdown(Math.ceil(startY) + objheight -1 /*- mapStartY*/, gridsize) + 1;
            collisionY = yTileIndex * gridsize /*+ mapStartY*/;
            itemYatCollision = collisionY - objheight;
            yCollisionSide = 0;
        }
        // 2: calculate the part of move that has been done until the collision: (colx - prevx)/(newx - prevx)
        // Note: if factor >=1, the collision position is further away than the move. Therefore it has not
        // been reached and there is no collision. This property will be used as a collision test.
        xFactor = calcMoveFactor(startX, endX, itemXatCollision);
        yFactor = calcMoveFactor(startY, endY, itemYatCollision);
        // System.out.println("col6, xf: " + MathFloat.toString(xFactor, 2, 2) + ", yf: "
        //        + MathFloat.toString(yFactor, 2, 2));

        // 3. Loop through colliding gridlines, starting always with the nearest one.
        // After handling collision with one gridline, move to the next, recalculating Factor.
        while ( xFactor < 1 || yFactor < 1 )
        {	// handle collision that comes first, that is the lower factor (<1 guaranteed by loop criterion)
           	if (xFactor <= yFactor)
          	{ 	// horizontal collision occurrs earlier than vertical
           		if ( (xTileIndex >= 0) && (xTileIndex < gameTiles.getMapWidth()) )
           		{	// find matching ypos, then tiles in range [ypos, ypos+frameheight]
           			int ypos = (int)(startY + xFactor*(endY-startY));
           			// find corresponding tilenumebers, limit to size of map through min & max. 
           			int firsttile = Math.max(0, divdown(ypos/*-mapStartY*/, gridsize));
           			int lasttile = Math.min(gameTiles.getMapHeigth()-1, divdown(ypos + objheight-1/*-mapStartY*/, gridsize));
           			// loop through y-range
           			for (int yindex = firsttile; yindex <=lasttile; yindex++)
           			{	// see if there is a tile at the current position
           				Tile t = gameTiles.getTileOnIndex(xTileIndex, yindex);
           				if ( t != null )
           					if ( t.getTileType() != -1 )
           						collidedTiles.add(new TileCollision(t, xCollisionSide));
           			}
           		}
                if ( moveleft )
                {	// move collision gridline to the left for next check
                    xTileIndex--;
                    collisionX = (xTileIndex + 1) * gridsize /*+ mapStartX*/;
                    itemXatCollision = collisionX;
                } else
                {	// move collision gridline to the right for next check
                    xTileIndex++;
                    collisionX = xTileIndex * gridsize /*+ mapStartX*/;
                    itemXatCollision = collisionX - objwidth;
                }
                // calculate new xFactor, since gridline has moved
                xFactor = calcMoveFactor(startX, endX, itemXatCollision);
            } else			
            {	// vertical collision occurs earlier (comments omitted, are like horizontal collision)
           		if ( (yTileIndex >= 0) && (yTileIndex < gameTiles.getMapHeigth()) )   // nog een keer spelfout verbeteren: height :-)
           		{	
           			int xpos = (int)(startX + yFactor*(endX-startX));
           			int firsttile = Math.max(0, divdown(xpos/*-mapStartX*/, gridsize));
           			int lasttile = Math.min(gameTiles.getMapWidth()-1, divdown(xpos + objwidth-1/*-mapStartX*/, gridsize));
           			for (int xindex = firsttile; xindex <= lasttile; xindex++)
           			{
           				Tile t = gameTiles.getTileOnIndex(xindex, yTileIndex);
           				if ( t != null )
           					if ( t.getTileType() != -1 )
           						collidedTiles.add(new TileCollision(t, yCollisionSide));
           			}
           		}
                if ( moveup )
                {	yTileIndex--;
                    collisionY = (yTileIndex + 1) * gridsize /*+ mapStartY*/;
                    itemYatCollision = collisionY;
                } else
                {	yTileIndex++;
                    collisionY = yTileIndex * gridsize /*+ mapStartY*/;
                    itemYatCollision = collisionY - objheight;
                }
                 yFactor = calcMoveFactor(startY, endY, itemYatCollision);
            }
            // System.out.println("col6, xf: " + MathFloat.toString(xFactor, 2, 2) + ", yf: "
            //        + MathFloat.toString(yFactor, 2, 2));
        }
        // 4. call collisonOccurred with list of tileCollisions
		if (collidedTiles.size() >  0) {
			// System.out.println("=====");	// Debug feedback
			// System.out.println("startx,y: "+startX+", "+startY+" endx,y: "+endX+", "+endY);
			collisionObject.collisionOccurred(collidedTiles);
			// Tile found? collision occurred
		}
	}

    /**
     * This method solves the problem that (int) (a/b)*b works as a round down to multiples of b for positive ints
     * a, and as a round up for negative ints. 
     * 
     * @param pos
     *                a number, usually an X- or Y-position
     * @param deler
     *                the divisor, usually tilesize
     * @return pos/deler, one down if pos is negative (i.e. divdown( -6, 10) yields -1)
     */
    private int divdown(double pos, int deler)
    {
        int res = (int)(pos/deler);
        if (res<0) res--;
        return res;
    }
    
    /**
     * Gets the fraction of the move that has been done up to a collision with a tile.
     * Utility method for collision detection, to find out if vertical collision happens before horizontal
     * Example: if object moves from pos 5 (start) to 25 (end) and gridline is at 10 (pos), collision
     * occurs at (10-5)/(25-5) = 0.25, that is after one quarter of the move.
     * Factors will be calculated in both x and y directions and will determine which collision is first.
     * 
     * @return the fraction of the move, a number between 0 and 1
     */
    private double calcMoveFactor(double start, double end, int pos)
    {
        if (end == start )
        {	// the calculation is not quite right (since (colx-prevx)/0 is not equal to 1.... 
            // but it is ok for collision detection, because >=1 means 'no collision'
            return 1;
        } else
        {
            return ( (double)pos - start)/(end - start);
        }
    	
    }

/*	public Tile getTileOnIndex(int xPosition, int yPosition,
			GameTiles gameTiles) {

		if (gameTiles.tileArray != null) {

			if (yPosition >= 0 && yPosition < gameTiles.tileArray.length) {
				if (xPosition >= 0 && xPosition < gameTiles.tileArray[yPosition].length) {
					return gameTiles.tileArray[yPosition][xPosition];
				}
			}
		}
		return null;
	}
	*//**
	 * Get a tile on a specific x and y position in the game world
	 * 
	 * @param xPosition
	 *            x position of the tile
	 * @param yPosition
	 *            y position of the tile
	 * @param gameTiles
	 * @return The Tile object at the given x and y position
	 *//*
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
	}*/
}
