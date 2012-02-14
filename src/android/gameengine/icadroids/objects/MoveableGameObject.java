package android.gameengine.icadroids.objects;

import java.util.ArrayList;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.engine.GameTiles;
import android.gameengine.icadroids.objects.collisions.CardinalDirections;
import android.graphics.RectF;

/**
 * MoveableGameObject represents a moveable object in the game. Make sure to add
 * the MoveableGameObject to the object list, else it won't update. <br/>
 * The game engine does collision detection for MoveableGameItems. This is very
 * time consuming, so make sure you only extend this class when the items are
 * really moving!
 * 
 * @author Edward van Raak,Bas van der Zandt, Roel van Bergen
 */
public class MoveableGameObject extends GameObject implements
		CardinalDirections {

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
	/** Holds the tile number which this object should move up to */
	private double moveUpToType = 0;
	/** Holds the speed of this object */
	private double speed;
	/** Holds the friction of this object */
	private double friction = 0;
	/**
	 * This variable will be true when horizontal collision is detected, false
	 * on vertical.
	 */
	private boolean horizontal;
	/**
	 * This variable will be true for when 'move up to side' should be
	 * performed.
	 */
	private boolean moveUpToSide;
	/** This holds the degrees the collision calculates and requires */
	private double degrees;

	/**
	 * Value that holds whenever collision should be skipped because 2
	 * collisions were found in sucession.
	 */
	private boolean skipCollision = false;
	/**
	 * This list holds all the rectangles of the tiles this object collided
	 * with, the collision updates this list.
	 */
	private ArrayList<RectF> collidedTiles = new ArrayList<RectF>();
	/** This list holds all the tiletypes this object has collided with. */
	private ArrayList<Integer> collidedTypes = new ArrayList<Integer>();
	/** This array holds tiles which this object collided with */
	private Boolean[] tileCollided = new Boolean[GameTiles.tileTypes.length];

	/**
	 * Constructor in which you can initialize most of your variables you need.
	 */
	public MoveableGameObject() {
		for (int i = 0; i < tileCollided.length; i++) {
			tileCollided[i] = false;
		}
	}

	@Override
	public void update() {
		super.update();
		move();
		updatePlayer();
		speed = calculateSpeed(xSpeed, ySpeed);
		rectangle.set((float) getFullX(), (float) getFullY(),
				(float) getFrameWidth() + (float) getFullX(),
				(float) getFrameHeight() + (float) getFullY());
		calculateTileCollisions();
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
		direction = calculateDirection(deltaX, deltaY);
		setDirection(direction);
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
		if (speed > 0) {
			prevX = xlocation;
			prevY = ylocation;
			prevCenterX = getCenterX();
			prevCenterY = getCenterY();
			xSpeed = calculateFriction(xSpeed);
			ySpeed = calculateFriction(ySpeed);
			xlocation += xSpeed;
			ylocation += ySpeed;
		}
	}

	/**
	 * Updates the player movement and sets the previous location this function
	 * is called in the update loop and is mandatory for a moving object to
	 * function correctly.
	 */
	private void updatePlayer() {

		prevX = xlocation;
		prevY = ylocation;
		prevCenterX = getCenterX();
		prevCenterY = getCenterY();

		xlocation += moveX;
		ylocation += moveY;
		moveX = 0;
		moveY = 0;

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
	 * Using the angle calculated by calculateTileCollision, handle the
	 * collision.
	 * 
	 * @param tiletype
	 * @param side
	 * @return true if a collision was detected
	 */
	public final boolean handleTileCollision(int tiletype, int side) {
		int tilenum = 0;
		double tempCollidedTileX = collidedTiles.get(0).centerX();
		double tempCollidedTileY = collidedTiles.get(0).centerY();
		/**
		 * Incase moveUpToSide was activated, find a tile of the correct type as
		 * was specified by moveUpToTileSide()
		 */
		if (moveUpToSide) {
			for (int i = 0; i < collidedTypes.size(); i++) {
				if (collidedTypes.get(i) == moveUpToType) {
					tilenum = i;
				}
			}
		}
		/***
		 * Use the final angle to check if the there was an horizontal or
		 * vertical collision
		 */
		if (degrees == SOUTH_EAST || degrees == SOUTH_WEST
				|| degrees == NORTH_WEST || degrees == NORTH_EAST) {
			skipCollision = true;
			undoMove();
			return true;
		}
		if ((degrees > NORTH_EAST && degrees <= FULL_EAST)
				|| (degrees >= EAST && degrees < SOUTH_EAST)
				&& (side == 4 || side == 5)) {
			return handleRightSideCollision(tempCollidedTileX, tilenum);
		}
		if (degrees > 135 && degrees < 225 && (side == 1 || side == 5)) {
			return handleLeftSideCollision(tempCollidedTileX, tilenum);
		}
		if (degrees > 225 && degrees < 315 && (side == 3 || side == 5)) {
			return handleTopSideCollision(tempCollidedTileY, tilenum);
		}
		if (degrees > 45 && degrees < 135 && (side == 2 || side == 5)) {
			return handleBottomSideCollision(tempCollidedTileY, tilenum);
		}
		return false;
	}

	/**
	 * This function performs a sequence of actions in which it checks for
	 * vertical only collision
	 * 
	 * @param tempCollidedTileY
	 * @return <b>TRUE</b> if there has been a vertical <b>only </b> collision.
	 *         Otherwise it will return <b>FALSE</b>
	 */
	private boolean handleVerticalMoveUpTo(double tempCollidedTileY) {
		for (int i = 0; i < collidedTiles.size(); i++) {
			RectF currentSqr = collidedTiles.get(i);
			if (tempCollidedTileY != currentSqr.centerY()) {
				undoMove();
				return true;
			}
		}
		return false;
	}

	/**
	 * This function performs a sequence of actions in which it checks for
	 * horizontal only collision
	 * 
	 * @param tempCollidedTileX
	 * @return <b>TRUE</b> if there has been a horizontal <b>only </b>
	 *         collision. Otherwise it will return <b>FALSE</b>
	 */
	private boolean handleHorizontalMoveUpTo(double tempCollidedTileX) {
		for (int i = 0; i < collidedTiles.size(); i++) {
			RectF currentSqr = collidedTiles.get(i);
			if (tempCollidedTileX != currentSqr.centerX()) {
				undoMove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Handles the sequence of actions this object should perform for a
	 * collision at the right side of this object.
	 * 
	 * @param tempCollidedTileX
	 *            the X position of collided tile.
	 * @param tilenum
	 *            the tile number it should perform this function on.
	 * @return always returns true
	 */
	private boolean handleRightSideCollision(double tempCollidedTileX,
			int tilenum) {
		skipCollision = true;
		if (moveUpToSide) {
			if (handleHorizontalMoveUpTo(tempCollidedTileX)) {
				return true;
			}
			setPosition(collidedTiles.get(tilenum).left - rectangle.width(),
					getY());
			moveUpToSide = false;
		}
		setHorizontal(true);
		return true;
	}

	/**
	 * Handles the sequence of actions this object should perform for a
	 * collision at the left side of this object.
	 * 
	 * @param tempCollidedTileX
	 *            the X position of collided tile.
	 * @param tilenum
	 *            the tile number it should perform this function on.
	 * @return always returns true
	 */
	private boolean handleLeftSideCollision(double tempCollidedTileX,
			int tilenum) {
		skipCollision = true;
		if (moveUpToSide) {
			if (handleHorizontalMoveUpTo(tempCollidedTileX)) {
				return true;
			}
			setPosition(collidedTiles.get(tilenum).right, getY());
			moveUpToSide = false;
		}
		setHorizontal(true);
		return true;
	}

	/**
	 * Handles the sequence of actions this object should perform for a
	 * collision at the top side of this object.
	 * 
	 * @param tempCollidedTileY
	 *            the Y position of collided tile.
	 * @param tilenum
	 *            the tile number it should perform this function on.
	 * @return always returns true
	 */
	private boolean handleTopSideCollision(double tempCollidedTileY, int tilenum) {
		skipCollision = true;
		if (moveUpToSide) {
			if (handleVerticalMoveUpTo(tempCollidedTileY)) {
				return true;
			}
			setPosition(getX(), collidedTiles.get(tilenum).bottom);
			moveUpToSide = false;
		}
		setHorizontal(true);
		return true;
	}

	/**
	 * Handles the sequence of actions this object should perform for a
	 * collision at the bottom side of this object.
	 * 
	 * @param tempCollidedTileY
	 *            the Y position of collided tile.
	 * @param tilenum
	 *            the tile number it should perform this function on.
	 * @return always returns true
	 */
	private boolean handleBottomSideCollision(double tempCollidedTileY,
			int tilenum) {
		skipCollision = true;
		if (moveUpToSide) {
			if (handleVerticalMoveUpTo(tempCollidedTileY)) {
				return true;
			}
			setPosition(getX(),
					collidedTiles.get(tilenum).top - rectangle.height());
			moveUpToSide = false;
		}
		setHorizontal(true);
		return true;
	}

	/***
	 * Checks whenever this object colides with the given tiletype. If so, this
	 * method returns true.
	 * 
	 * @author Edward van Raak & Roel van Bergen
	 * @param tiletype
	 *            The map tile type
	 * @return
	 */
	private final ArrayList<RectF> calculateTileCollisions() {
		int xSector = 0;
		int ySector = 0;
		collidedTiles.clear();
		collidedTypes.clear();
		/**
		 * Calculate the size of the sector
		 */
		xSector = (int) Math.ceil(rectangle.width() / GameTiles.tileSize) + 1;
		ySector = (int) Math.ceil(rectangle.height() / GameTiles.tileSize) + 1;
		/**
		 * Loop through all the tiles in the current sector, add any collided
		 * tiles to the collidedTiles list (Refactor to level 1 complexity in
		 * the future!)
		 */
		int xVertice = (int) Math.floor(xlocation / GameTiles.tileSize);
		int yVertice = (int) Math.floor(ylocation / GameTiles.tileSize);
		for (int i = xVertice - xSector; i < xVertice + xSector; i++) {
			if (i >= GameTiles.tileMapWidth) {
				break;
			}
			for (int j = yVertice - ySector; j < yVertice + ySector; j++) {
				i = i < 0 ? 0 : i;
				j = j < 0 ? 0 : j;
				if (j >= GameTiles.tileMapHeight) {
					break;
				}
				if (RectF.intersects(rectangle, GameTiles.tileRectArray[j][i])
						&& GameTiles.tileArray[j][i] != -1) {
					collidedTiles.add(GameTiles.tileRectArray[j][i]);
					collidedTypes.add((int) GameTiles.tileArray[j][i]);
				}
			}
		}
		/**
		 * Loop through the list of intersected tiles and calculate the angle
		 * between the center point of the tile and the center point of the
		 * sprite then add these angles to the angle list.
		 */
		double sumdx = 0;
		double sumdy = 0;
		for (int i = 0; i < collidedTiles.size(); i++) {
			RectF currentSqr = collidedTiles.get(i);
			sumdx += getPrevCenterX() - currentSqr.centerX();
			sumdy += getPrevCenterY() - currentSqr.centerY();
		}
		double avgdx = sumdx / collidedTiles.size();
		double avgdy = sumdy / collidedTiles.size();
		degrees = Math.toDegrees(Math.atan2(avgdy, avgdx)) + 180;
		/**
		 * Reset the list that holds what type is being collided
		 */
		for (int i = 0; i < tileCollided.length; i++) {
			tileCollided[i] = false;
		}
		/**
		 * If no tiles were found that intersect with this game object...
		 */
		if (collidedTiles.size() == 0) {
			/** Stop with collsision detection **/
			skipCollision = false; // collision activated again
			return null;
		}
		/**
		 * Set elements in the collided tiletype list to true
		 */
		for (int i = 0; i < collidedTypes.size(); i++) {
			int x = collidedTypes.get(i);
			tileCollided[x] = true;
		}
		return collidedTiles;
	}

	/**
	 * Check collision on all sides of this sprite.
	 * 
	 * @param type
	 *            What tile type should be checked
	 * @return true if a collision has occured on any side of this game object.
	 */
	public final boolean collided(int type) {
		return checkSides(type, 5);
	}

	/**
	 * Check collision on the the left side of this sprite.
	 * 
	 * @param type
	 *            What tile type should be checked
	 * @return true if a collision has occured on the left side of this game
	 *         object.
	 */
	public final boolean collidedLeft(int type) {
		return checkSides(type, 1);
	}

	/**
	 * Check collision on the the bottom side of this sprite.
	 * 
	 * @param type
	 *            What tile type should be checked
	 * @return true if a collision has occured on the bottom side of this game
	 *         object.
	 */
	public final boolean collidedBottom(int type) {
		return checkSides(type, 2);
	}

	/**
	 * Check collision on the the top side of this sprite.
	 * 
	 * @param type
	 *            What tile type should be checked
	 * @return true if a collision has occured on the top side of this game
	 *         object.
	 */
	public final boolean collidedTop(int type) {
		return checkSides(type, 3);
	}

	/**
	 * Check collision on the the right side of this sprite.
	 * 
	 * @param type
	 *            What tile type should be checked
	 * @return true if a collision has occured on the right side of this game
	 *         object.
	 */
	public final boolean collidedRight(int type) {
		return checkSides(type, 4);
	}

	/**
	 * Check sides for collision
	 * 
	 * @param type
	 *            The type that should be checked
	 * @param side
	 *            The side that should be checked
	 * @return true if collision was found
	 */
	private final boolean checkSides(int type, int side) {
		if (skipCollision) {
			System.out.println("Collision skipped!");
			// return false;
		}
		if (!tileCollided[type]) {
			return false;
		}
		if (handleTileCollision(type, side)) {
			return true;
		}
		return false;
	}

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
				if (this.rectangle.intersect(GameEngine.items.get(i).rectangle)) {
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
	 * Gets the instance of a collided gameobject, this will get the first
	 * object you collided with. Do not use this if you want to check for
	 * multiple objects use getCollidedObjects instead! This function will
	 * return a null if there has been no collision. Be sure to catch the null
	 * or else you might receive a null pointer exception.
	 * 
	 * @retun returns the instance of the object or a null depending if there is
	 *        a collision with an object or not.
	 */
	public final GameObject getCollidedObject() {

		if (getCollidedObjects() != null) {
			return getCollidedObjects().get(0);
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
	 * Use this function to get the angle between you and another object. For
	 * example: You can use this function to check if you approaching another
	 * object from the left or right.
	 * 
	 * @param object
	 *            an instance of another object to calculate the angle for.
	 * @return the angle of the object or 0 if the object is null.
	 */
	public final int getAngle(GameObject object) {
		if (object == null) {
			return 0;
		}
		double dx = object.getCenterX() - this.getCenterX();
		double dy = object.getCenterY() - this.getCenterY();
		int angle = (int) Math.round(Math.toDegrees(Math.atan2(dy, dx)) + 180);
		return angle;
	}

	/**
	 * Set horizontal
	 * 
	 * @param horizontal
	 *            the horizontal to set
	 */
	private final void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	/**
	 * This method will return true when horizontal collision is detected, false
	 * on vertical.
	 * 
	 * @return 'True' when there is a horizontal collision, false when it's
	 *         vertical.
	 */
	public boolean getHorizontal() {
		return horizontal;
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
	 * Gets the Y speed of this object. Note that an object only has a speed if
	 * it first has been set with setSpeed(double),setxSpeed(double) or
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
		rectangle.set((float) this.getPrevX(), (float) this.getPrevY(),
				(float) this.getPrevX() + (float) getFrameWidth(),
				(float) this.getPrevY() + (float) getFrameHeight());
	}

	/**
	 * If this method is called inside the update loop, it will cause this
	 * object to move as close as possible to the tile it had collided with.
	 * 
	 * @param activate
	 *            When set to false, this method will not have any effect.
	 * @param tiletype
	 *            The tiletype that should be used.
	 **/
	public final void moveUpToTileSide(int tiletype) {
		moveUpToType = tiletype;
		moveUpToSide = true;
	}
}
