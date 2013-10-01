package android.gameengine.icadroids.objects;

import java.util.ArrayList;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.objects.collisions.CollidingObject;
import android.gameengine.icadroids.objects.collisions.TileCollision;
import android.gameengine.icadroids.objects.collisions.ICollision;
import android.gameengine.icadroids.tiles.Tile;
import android.graphics.Rect;

/**
 * MoveableGameObject represents a moveable object in the game. Make sure to add
 * the MoveableGameObject to the object list, else it won't update. <br/>
 * The game engine does collision detection for MoveableGameItems. This is very
 * time consuming, so make sure you only extend this class when the items are
 * really moving!
 * 
 * <b> note: </b> Tile collision only works when the object is moving! </b>
 * 
 * @author Edward van Raak,Bas van der Zandt, Roel van Bergen
 */
public class MoveableGameObject extends GameObject {

	/** Holds the xspeed of this object */
	private double xSpeed = 0;
	/** Holds the yspeed of this object */
	private double ySpeed = 0;
	/** Holds the prevx of this object */
	private double prevX = 0;
	/** Holds the prevy of this object */
	private double prevY = 0;
	/** Holds the prevCenterX of this object */
	private double prevCenterX = 0;
	/** Holds the prevCenterY of this object */
	private double prevCenterY = 0;
	/** Holds the direction of this object */
	private double direction = 0;
	/** Holds the total x movement the object should perform, resets every loop */
	private double moveX = 0;
	/** Holds the total y movement the object should perform, resets every loop */
	private double moveY = 0;
	/** Holds the speed of this object */
	private double speed;
	/** Holds the friction of this object */
	private double friction = 0;

	private CollidingObject collidingObject = new CollidingObject();

	/**
	 * The update-method will be called every cycle of the game loop.
	 * Override this method to give an object any time driven behaviour.
	 * <br />
	 * Note: Always call <i>super.update()</i> first in your overrides, 
	 * because the default update does some important actions, like
	 * moving the object.
	 * 
	 * @see android.gameengine.icadroids.objects.GameObject#update()
	 */
	@Override
	public void update() {
		super.update();
		move();
		speed = calculateSpeed(xSpeed, ySpeed);
	}
	

	/**
	 * Sets the objects direction to move towards target point.
	 * 
	 * @param x
	 *            The x coordinate of your point.
	 * @param y
	 *            The y coordinate of your point.
	 */
	public final void moveTowardsAPoint(double x, double y) {
		double deltaX = x - getFullX();
		double deltaY = y - getFullY();
		double pointDirection = calculateDirection(deltaX, deltaY);
		setDirection(pointDirection);
	}

	/**
	 * Sets the direction of this object in degrees
	 * Direction 0 points up, directions go clockwise, so 90 is right, etc.
	 * 
	 * @param direction
	 *            the direction in degrees.
	 */
	public final void setDirection(double direction) {
		double thisDirection = direction;

		if (thisDirection >= 360) {
			thisDirection -= thisDirection
					- (360 * ((thisDirection / 360) % 1));
		}
		if (thisDirection < 0) {
			thisDirection -= thisDirection
					- (360 * ((thisDirection / -360) % 1));
		}

		double radianDirection = Math.toRadians(thisDirection)
				- (0.5 * Math.PI);
		double sv = Math.sin(radianDirection) * speed;
		double sh = Math.cos(radianDirection) * speed;
		ySpeed = sv;
		xSpeed = sh;
		this.direction = thisDirection;
	}

	/**
	 * Calculates the direction this object travels to based on the x speed and
	 * the y speed.
	 * 
	 * @param xSpeed
	 *            the x speed.
	 * @param ySpeed
	 *            the y speed.
	 * @return the direction in degrees.
	 */
	private double calculateDirection(double xSpeed, double ySpeed) {
		if (xSpeed >= 0 || ySpeed >= 0) {
			return Math.toDegrees(Math.atan2(ySpeed, xSpeed)) + 90;
		} else {
			return Math.toDegrees((Math.atan2(ySpeed, xSpeed))) + 450;
		}
	}

	/**
	 * calculates the actual speed based on the x and y speed.
	 * 
	 * @return the actual speed.
	 */
	private double calculateSpeed(double xSpeed, double ySpeed) {
		return Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));
	}

	/**
	 * Sets the speed of this object
	 * 
	 * @param speed
	 *            the speed to give this object.
	 */
	public final void setSpeed(double speed) {
		this.speed = speed;
		setDirection(direction);
	}

	/**
	 * Sets both the direction and the speed of this object.
	 * Direction 0 points up, directions go clockwise, so 90 is right, etc.
	 * 
	 * @param direction
	 *            the direction you want to set in the degrees
	 * @param speed
	 *            the speed you want to set.
	 */
	public final void setDirectionSpeed(double direction, double speed) {
		setSpeed(speed);
		setDirection(direction);
	}

	/**
	 * Sets the x speed of this object.
	 * 
	 * @param xSpeed
	 *            the xSpeed.
	 */
	public final void setxSpeed(double xSpeed) {
		speed = calculateSpeed(xSpeed, ySpeed);
		direction = calculateDirection(xSpeed, ySpeed);
		this.xSpeed = xSpeed;
	}

	/**
	 * Sets the y speed of this object.
	 * 
	 * @param ySpeed
	 *            the ySpeed.
	 */
	public final void setySpeed(double ySpeed) {
		speed = calculateSpeed(xSpeed, ySpeed);
		direction = calculateDirection(xSpeed, ySpeed);
		this.ySpeed = ySpeed;
	}

	/**
	 * Sets the current direction of this object in radians
	 * Direction 0 points up, directions go clockwise, so 0.5*pi is right, etc.
	 * 
	 * @param Direction
	 *            the direction in radians.
	 */
	public final void setDirectionRadians(double Direction) {
		setDirection(Math.toDegrees(Direction));
	}

	/**
	 * Moves this object and sets the previous location. This function only
	 * works when this object has a speed.
	 */
	private void move() {
		if (speed > 0 || moveX != 0 || moveY != 0) {
			prevX = xlocation;
			prevY = ylocation;
			prevCenterX = getCenterX();
			prevCenterY = getCenterY();
			xSpeed = calculateFriction(xSpeed);
			ySpeed = calculateFriction(ySpeed);

			double movementX = xSpeed + moveX;
			double movementY = ySpeed + moveY;

			xlocation += movementX;
			ylocation += movementY;

			// Calculate collision
			if ( this instanceof ICollision )
			collidingObject.calculateCollision(xlocation, ylocation, xlocation
					- movementX, ylocation - movementY, this.getSprite(),
					GameEngine.gameTiles, (ICollision)this);

			moveX = 0;
			moveY = 0;
		}
	}

	/**
	 * This function allows you to move this object by specifying the x and y.
	 * Each successive call in one loop will increase the amount of movement
	 * this object should perform at the end of the loop.
	 * 
	 * @param x_movement
	 *            the amount of X pixels this object should move.
	 * @param y_movement
	 *            the amount of Y pixels this object should move.
	 */
	public final void movePlayer(int x_movement, int y_movement) {
		moveX += x_movement;
		moveY += y_movement;
	}

	/**
	 * @return returns <b>true</b> if this object is moving to the left, returns
	 *         <b>false</b> otherwise.
	 */
	public final boolean movesLeft() {
		return xSpeed < 0;
	}

	/**
	 * @return returns <b>true</b> if this object is moving to the right,
	 *         returns <b>false</b> otherwise.
	 */
	public final boolean movesRight() {
		return xSpeed > 0;
	}

	/**
	 * @return returns <b>true</b> if this object is moving up, returns
	 *         <b>false</b> otherwise.
	 */
	public final boolean movesUp() {
		return ySpeed < 0;
	}

	/**
	 * @return returns <b>true</b> if this object is moving down, returns
	 *         <b>false</b> otherwise.
	 */
	public final boolean movesDown() {
		return ySpeed > 0;
	}

	/**
	 * Bounces the object , what this does is reversing the horizontal or
	 * vertical direction.
	 * 
	 * @param horizontal
	 *            Put this on TRUE if you want to bounce Horizontal, FALSE if
	 *            you want to bounce vertical.
	 */
	public final void bounce(boolean horizontal) {
		if (horizontal) {
			reverseHorizontalDirection();
		} else {
			reverseVerticalDirection();
		}
	}

	/**
	 * Sets the friction of this object, that is the amount of speed reduction.
	 * <br />
	 * The decrease in speed is measured as a fraction, if you want a 5% decrease
	 * in speed per cycle of the game loop, use 0.05.
	 * 
	 * @param friction 
	 * 			the fraction of decrease in speed per cycle of the game loop.
	 * 			Must be a number between 0 and 1
	 */
	public final void setFriction(double friction) {
		if ( friction > 0 && friction < 1 ) {
			this.friction = friction;
		} else {
			this.friction = 0;
		}
	}

	/**
	 * Gets the friction of this object
	 * 
	 * @return the friction.
	 */
	public final double getFriction() {
		return friction;
	}

	/**
	 * Calculates the changes in speed due to friction.
	 * 
	 * @param directionSpeed
	 *            the current speed
	 * @return the new speed.
	 */
	private double calculateFriction(double directionSpeed) {
		return (1-friction)* directionSpeed;
	}

	/**
	 * Gets the direction of the objects movement in degrees.
	 * Direction 0 points up, directions go clockwise, so 90 is right, etc.
	 * 
	 * @return the direction(angle) in degrees.
	 */
	public final double getDirection() {
		return direction;
	}

	/**
	 * Gets the direction of the objects movement in radians.
	 * Direction 0 points up, directions go clockwise, so 0.5*pi is right, etc.
	 * 
	 * @return the direction(angle) in radians
	 */
	public final double getDirectionRadians() {
		return Math.toRadians(direction);
	}

	/**
	 * Reverses the horizontal direction of the objects movement.
	 */
	public final void reverseHorizontalDirection() {
		setxSpeed(-xSpeed);
	}

	/**
	 * Reverses the vertical direction of the objects movement.
	 */
	public final void reverseVerticalDirection() {
		setySpeed(-ySpeed);
	}

	/**
	 * Gets the previous X position of this object, which is saved every loop.
	 * 
	 * @return the previous X position.
	 */
	public final double getPrevX() {
		return prevX;
	}

	/**
	 * Gets the previous Y position of this object, which is saved every loop.
	 * 
	 * @return the previous Y position.
	 */
	public final double getPrevY() {
		return prevY;
	}

	/**
	 * Gets the previous centerX position of this object, which is saved every
	 * loop.
	 * 
	 * @return the prevCenterX of this object
	 */
	public final double getPrevCenterX() {
		return prevCenterX;
	}

	/**
	 * Gets the previous centerY position of this object, which is saved every
	 * loop.
	 * 
	 * @return the prevCenterY of this object
	 */
	public final double getPrevCenterY() {
		return prevCenterY;
	}

	/**
	 * Gets the speed of this object. Note that an object only has a speed if it
	 * first has been set with setSpeed(double), setxSpeed(double) or
	 * setySpeed(double)
	 * 
	 * @return The speed.
	 */
	public final double getSpeed() {
		return speed;
	}

	/**
	 * Gets the X speed of this object. Note that an object only has a speed if
	 * it first has been set with setSpeed(double), setxSpeed(double) or
	 * setySpeed(double)
	 * 
	 * @return The X speed.
	 */
	public final double getxSpeed() {
		return xSpeed;
	}

	/**
	 * Gets the Y speed of this object. Note that an object only has a speed if
	 * it first has been set with setSpeed(double),setxSpeed(double) or
	 * setySpeed(double)
	 * 
	 * @return The Y speed.
	 */
	public final double getySpeed() {
		return ySpeed;
	}

	/**
	 * This function will reset this object to its previous Position. the
	 * Previous Position will is set every loop.
	 */
	public final void undoMove() {
		setPosition(this.getPrevX(), this.getPrevY());
		updatePlayerFramePosition();
	}

	/**
	 * Triggered when the MoveableGameObject moves outside of the world.
	 * Override this method to take action when this happens!
	 * 
	 */
	public void outsideWorld() {
		// Override to use this method
	}

	// Collision methods

	/**
	 * Checks wether or not this gameObject has collided with one or multiple
	 * GameObjects or MovableGameObjects. It will return a list with the
	 * collided objects, it returns a null if there is no collision.<br />
	 * Call this method inside your <i>update()</i>, if you want to handle
	 * collisions.
	 * 
	 * @return An arraylist of all objects that this object has collided with.
	 * 
	 *         Note that you will never get the object calling this function
	 *         back.
	 */
	public final ArrayList<GameObject> getCollidedObjects() {
		ArrayList<GameObject> collidedObjects = new ArrayList<GameObject>();

		// move this part to GameEngine itself (and make 'items' private!!
		// check if other item is active
		for (int i = 0; i < GameEngine.items.size(); i++) {
			if (GameEngine.items.get(i) != this) {
				if (Rect.intersects(this.position, GameEngine.items.get(i).position)) {
					collidedObjects.add(GameEngine.items.get(i));
				}
			}
		}
		if (collidedObjects.size() > 0) {
			return collidedObjects;
		}
		return null;
	}

	/**
	 * Checks if your object has collided with another object of a certain
	 * class.
	 * 
	 * @param objectClass
	 *            Asks for any generic class, classes you should use are
	 *            Gameobject,MoveableGameobject or any extensions of these two.
	 * 
	 * @return returns true if this object has collided with an instance of the given
	 *         class
	 */
	public final <T> boolean collidedWith(Class<T> objectClass) {

		ArrayList<GameObject> tempArray = getCollidedObjects();
		if (tempArray == null) {

			return false;
		}

		for (int i = 0; i < tempArray.size(); i++) {
			if (tempArray.get(i).getClass().isAssignableFrom(objectClass)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Move as close as possible to the side of tile, as specified in the TileCollision object,
	 * using the direction that the GameObject already has 
	 * 
	 * Note: Using Tiles that are not involved in a collision or are not close to the object 
	 * can cause strange behaviour: The GameObject is moved to the extended line that
	 * passes through the specified side of the tile, using the <i>original direction</i>
	 * of the object's speed. 
	 * If there wasn't any collision in the first place, this may be somewhere not very close
	 * to the tile!
	 * 
	 * @param tc the TileCollision (usually provided by the the 'collisionOccurred' call )
	 */
	public void moveUpToTileSide(TileCollision tc)
	{
		int side = tc.collisionSide;
		// the position we want to move to, x or y
		int pos;
		if ( side == 0 || side == 2 )
		{	// move to horizontal tile edge, top or bottom
			pos = tc.theTile.getTileY();
			if ( side == 2 )
			{	// bottom, add gridsize to pos
				pos = pos + GameEngine.gameTiles.tileSize;
			} else
			{	// top of tile, adapt for sprite height
				pos = pos - getSprite().getFrameHeight();
			}
			// new y will be tileside (pos), x is changed in the same proportion as y, with respect to original move
			// note: if there was no move then prevY will equal ylocation and we do nothing to x
			if ( ylocation != prevY )
			{
				xlocation = prevX + (xlocation-prevX)* (((double)pos-prevY)/(ylocation-prevY));
			} 
			ylocation = (double) pos;
		} else
		{	// move to vertical tile edge, left or right
			pos = tc.theTile.getTileX();
			if ( side == 1 )
			{	// right, add gridsize to pos
				pos = pos + GameEngine.gameTiles.tileSize;
			} else
			{
				pos = pos - getSprite().getFrameWidth();
			}
			
			if ( xlocation != prevX )
			{	
				ylocation = prevY + (ylocation-prevY)* (((double)pos-prevX)/(xlocation-prevX));
			}

			xlocation = (double) pos;			

		}		
	}
	
	/**
	 * Bounce at the tile this object has just collided into. The TileCollision
	 * object will be provided by the tile-collision detection, so this method will
	 * typically be called at collision handling.<br />
	 * The object will be moved to the side of the tile and, according to the side
	 * of the tile it bounces on, x- or y-Speed will be reversed, so the object
	 * will move away in the next cycle of the game loop.
	 * 
	 * @param tc 	The TileCollision (that is Tile plus side) you want to bounce onto
	 */
	public void bounce(TileCollision tc)
	{
		moveUpToTileSide(tc);
		if ( tc.collisionSide == 0 || tc.collisionSide == 2)
		{
			reverseVerticalDirection();
		} else
		{
			reverseHorizontalDirection();
		}
		
	}
	
	/**
	 * Get a tile on a specific x and y position in the game world.<br />
	 * ToDo Note: this method should not be here, but in class GameEngine.
	 * Change in next version!
	 * 
	 * @param xPosition
	 *            x position of the tile
	 * @param yPosition
	 *            y position of the tile
	 * @return The Tile object at the given x and y position
	 */
	public Tile getTileOnPosition(int xPosition, int yPosition){
		return GameEngine.gameTiles.getTileOnPosition(xPosition, yPosition);
	}
	
	/**
	 * Get the colliding object if you want to make use of the methods that are in there
	 * @return the collidingObject
	 */
	public CollidingObject getCollidingObject() {
		return collidingObject;
	}
}
