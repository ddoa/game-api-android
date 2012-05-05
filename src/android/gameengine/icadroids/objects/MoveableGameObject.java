package android.gameengine.icadroids.objects;

import java.util.ArrayList;
import java.util.List;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.objects.collisions.CollidingObject;
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
public class MoveableGameObject extends GameObject implements ICollision {

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

	CollidingObject collidingObject = new CollidingObject();

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
			collidingObject.calculateCollision(xlocation, ylocation, xlocation
					- movementX, ylocation - movementY, this.getSprite(),
					GameEngine.gameTiles, this);

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
	 * Sets the friction of this object
	 */
	public final void setFriction(double friction) {
		this.friction = friction;
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
	 * Calculates the changes in speed.
	 * 
	 * @param directionSpeed
	 *            the current speed
	 * @return the new speed.
	 */
	private double calculateFriction(double directionSpeed) {
		if (directionSpeed < 0) {
			if (directionSpeed - friction > 0) {
				return 0;
			} else {
				return directionSpeed += friction;
			}
		}
		if (directionSpeed > 0) {
			if (directionSpeed - friction < 0) {
				return 0;
			} else {
				return directionSpeed -= friction;
			}
		}
		return 0;
	}

	/**
	 * Gets the direction of the objects movement in degrees.
	 * 
	 * @return the direction(angle) in degrees.
	 */
	public final double getDirection() {
		return direction;
	}

	/**
	 * Gets the direction of the objects movement in radians.
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
	 * first has been set with setSpeed(double),setxSpeed(double) or
	 * setySpeed(double)
	 * 
	 * @return The Y speed.
	 */
	public final double getSpeed() {
		return speed;
	}

	/**
	 * Gets the X speed of this object. Note that an object only has a speed if
	 * it first has been set with setSpeed(double),setxSpeed(double) or
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

	// Collision methods

	/**
	 * Checks wether or not this gameObject has collided with one or multiple
	 * gameObjects or MovableGameObjects. It will return a list with the
	 * collided objects, it returns a null if there is no collision.
	 * 
	 * @return An arraylist of all objects that have been collided with.
	 * 
	 *         Note that you will never get the object calling this function
	 *         back.
	 */
	public final ArrayList<GameObject> getCollidedObjects() {
		ArrayList<GameObject> collidedObjects = new ArrayList<GameObject>();

		for (int i = 0; i < GameEngine.items.size(); i++) {
			if (GameEngine.items.get(i) != this) {
				if (this.position.intersect(GameEngine.items.get(i).position)) {
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
	 * @return returns if this object has collided with an instance of given
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
	 * Calculates on which side of the object an collision has occurred with an
	 * tile.
	 * 
	 * @param object
	 *            The object that has the collision
	 * @param tile
	 *            The colided tile
	 * @return Returns when collision is on: <b> 0 - Top, 1 - Right, 2 - Bottom,
	 *         3 - Left</b>
	 */
	public int getCollisionSide(Tile tile) {

		double angle = collidingObject.calculateCollisionAngle(
				getFullX(), getFullY(), tile,
				getSprite());

		if ((angle >= 315 && angle < 360) || (angle >= 0 && angle <= 45)) { // collision
																			// top
			return 0;
		}
		if (angle >= 135 && angle <= 225) { // collision bottom
			return 2;
		}
		if (angle > 45 && angle < 135) { // collision right
			return 1;
		}
		if (angle > 225 && angle < 315) { // collision left
			return 3;
		}
		return -1;
	}

	/**
	 * Move as close as possible to the given (collided) tile
	 * 
	 * Note: this method is specially designed to work with collided tiles. 
	 * Using Tiles that are not close to the object can cause strange behavior.
	 * @param tile
	 */
	public void moveUpToTileSide(Tile tile){
		undoMove();
		
		int tilePositionX = (tile.getTileNumberX() * tile.getGameTiles().tileSize)
				+ (tile.getGameTiles().tileSize / 2);
		int tilePositionY = (tile.getTileNumberY() * tile.getGameTiles().tileSize)
				+(tile.getGameTiles().tileSize / 2);
		
		Rect ObjectAABB = new Rect();
		ObjectAABB.top = getY() - (tile.getGameTiles().tileSize / 2);
		ObjectAABB.left = getX() - (tile.getGameTiles().tileSize / 2);
		ObjectAABB.right = getX() + getFrameWidth() + (tile.getGameTiles().tileSize / 2);
		ObjectAABB.bottom = getY() + getFrameHeight() + (tile.getGameTiles().tileSize / 2);
		
		int deltaX = Math.abs(tilePositionX - ObjectAABB.centerX());
		int deltaY = Math.abs(tilePositionY - ObjectAABB.centerY());
		
		int movement = 0;
		if(deltaX > deltaY){
			if(ObjectAABB.right < tilePositionX){
				movement = (tilePositionX - ObjectAABB.right) - 1;
			}
			if(ObjectAABB.left > tilePositionX){
				movement = (tilePositionX - ObjectAABB.left) + 1;
			}
			xlocation += movement;
		}
		if(deltaX < deltaY){
			if(ObjectAABB.bottom < tilePositionY){
				movement = (tilePositionY - ObjectAABB.bottom) - 1;
			}
			if(ObjectAABB.top > tilePositionY){
				movement = (tilePositionY - ObjectAABB.top) + 1;
			}
			ylocation += movement;
		}
		
	}
	
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
	public Tile getTileOnPosition(int xPosition, int yPosition){
		return collidingObject.getTileOnPosition(xPosition, yPosition, GameEngine.gameTiles);
	}
	
	/**
	 * Get the colliding object if you want to make use of the methods that are in there
	 * @return the collidingObject
	 */
	public CollidingObject getCollidingObject() {
		return collidingObject;
	}
	
	/* (non-Javadoc)
	 * @see android.gameengine.icadroids.objects.collisions.ICollision#collisionOccurred(java.util.List)
	 */
	public void collisionOccurred(List<Tile> collidedTiles) {
		
	}

}
