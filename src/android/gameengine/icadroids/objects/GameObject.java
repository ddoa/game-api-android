package android.gameengine.icadroids.objects;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.objects.graphics.AnimatedSprite;
import android.gameengine.icadroids.renderer.Viewport;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * GameObject is a (not moving) object with a lot of useful methods in it. You
 * can bind a sprite on it, activate alarms, get the size, position, etc.
 * 
 * <b>Don't forget to add the GameObject to the game with 'addGameObject'!</b>
 * 
 * @author Bas van der Zandt
 * 
 */
public class GameObject {
	/**
	 * When active is false, the object can be destroyed
	 */
	public boolean active = true;
	/**
	 * Indicates if the object is visible or not.
	 */
	public boolean isVisible = true;
	/**
	 * The exact x position of the object
	 */
	protected double xlocation = 0;
	/**
	 * the exact y position of the object
	 */
	protected double ylocation = 0;
	/**
	 * The sprite (image) of the object.
	 */
	private AnimatedSprite sprite = new AnimatedSprite();
	/**
	 * The sprite position on the screen
	 */
	public Rect position = new Rect(0, 0, 0, 0);
	/**
	 * Start position of the object
	 */
	int[] startposition = new int[2];

	/**
	 * Initialize resources.
	 * 
	 * @param ge
	 *            the GameEngine which the object is running on
	 */
	public final void intializeGameObject() {
		intialize();
	}

	/**
	 * Called when the application starts.
	 */
	public void intialize() {

	}

	/**
	 * update is triggered every loop of the game
	 */
	public void update() {
		sprite.updateToNextFrame();
		updatePlayerFramePosition();
		calculateOutsideWorld();
	}

	/**
	 * Draw the GameObject on the screen
	 * 
	 * @param canvas
	 *            Android canvas
	 */
	public void drawGameObject(Canvas canvas) {
		if (sprite.getSprite() != null && isVisible) {
			canvas.drawBitmap(sprite.getSprite(),
					sprite.getCurrentFrameRectangle(), position, null);
		}
	}

	/**
	 * update position rectangle used for drawing the sprite on the right
	 * position with the right size on the screen. This rectangle is also used
	 * for collision detection between GameObjects.
	 */
	protected void updatePlayerFramePosition() {
		position.set(getX(), getY(), getX() + sprite.getFrameWidth(), getY()
				+ getFrameHeight());
	}

	/**
	 * Set a sprite for the GameObject.
	 * 
	 * <b>Note: Extremely small sprites don't work well with Collision
	 * Detection!</b>
	 * 
	 * @param resourceName
	 *            The name of the resource in the /res/drawable folder <b>
	 *            without extension </b>, so when your picture in the
	 *            /res/drawable is named 'picture.jpg', this parameter should be
	 *            'picture' .
	 */
	public final void setSprite(String resourceName) {
		sprite.loadSprite(resourceName);
	}

	/**
	 * Start animating the sprite
	 * 
	 * @param frameWidth
	 *            The width size of each frame
	 */
	public final void startAnimate(int frameWidth) {
		sprite.startAnimate(frameWidth);
	}

	/**
	 * Stop animating the sprite
	 */
	public final void stopAnimate() {
		sprite.stopAnimate();
	}

	/**
	 * get the x position on the screen
	 * 
	 * @return x position on the screen
	 */
	public final int getX() {
		return (int) Math.round(xlocation);
	}

	/**
	 * Get the y position on the screen
	 * 
	 * @return the y position on the screen
	 */
	public final int getY() {
		return (int) Math.round(ylocation);
	}

	/**
	 * Get the center x of the GameObject
	 * 
	 * @return the center x coordinate of the GameObject
	 */
	public final float getCenterX() {
		return (float) (xlocation + getSprite().getSpriteCenterX());
	}

	/**
	 * Get the center y of the GameObject
	 * 
	 * @return the center y coordinate of the GameObject
	 */
	public final float getCenterY() {
		return (float) (ylocation + getSprite().getSpriteCenterY());
	}

	/**
	 * Get the absolute x position of the GameObject
	 * 
	 * @return the absolute x position
	 */
	public final double getFullX() {
		return xlocation;
	}

	/**
	 * Get the absolute y position of the GameObject
	 * 
	 * @return the absolute y position
	 */
	public final double getFullY() {
		return ylocation;
	}

	/**
	 * Set x position of the GameObject WARNING: Do NOT use this method to move
	 * the player or a lot of methods will behave incorectly.
	 * 
	 * @param x
	 *            The x position
	 */
	public final void setX(double x) {
		this.xlocation = x;
	}

	/**
	 * Set y position of GameObject WARNING: Do NOT use this method to move the
	 * player or a lot of methods will behave incorectly.
	 * 
	 * @param y
	 *            The y position
	 */
	public final void setY(double y) {
		this.ylocation = y;
	}

	/**
	 * Set the position on the screen.
	 * 
	 * @param x
	 *            The x position
	 * @param y
	 *            The y position
	 */
	public final void setPosition(double x, double y) {
		this.xlocation = x;
		this.ylocation = y;
	}

	/**
	 * Get the sprite Object of the GameObject
	 * 
	 * @return Sprite Object
	 */
	public final AnimatedSprite getSprite() {
		return sprite;
	}

	/**
	 * Get the size of the size of the frame width. This wil standard be the
	 * sprite width.
	 * 
	 * @return The width size of the frame
	 */
	public final int getFrameWidth() {
		return sprite.getFrameWidth();
	}

	/**
	 * Get the size of the size of the frame height. This will standard be the
	 * sprite height.
	 * 
	 * @return The height size of the frame
	 */
	public final int getFrameHeight() {
		return sprite.getFrameHeight();
	}

	/**
	 * Calculates if the object is outside the world or not.
	 */
	private void calculateOutsideWorld() {
		Viewport vp = Viewport.getInstance();
		if (Viewport.useViewport) {
			if (xlocation > vp.getMaxX() || xlocation < 0 - getFrameWidth()) {
				outsideWorld(true);
			} else if (ylocation > vp.getMaxY()
					|| ylocation < 0 - getFrameHeight()) {
				outsideWorld(false);
			}
		} else {
			if (xlocation > GameEngine.getScreenWidth()
					|| xlocation < 0 - getFrameWidth()) {
				outsideWorld(true);
			} else if (ylocation > GameEngine.getScreenHeight()
					|| ylocation < 0 - getFrameHeight()) {
				outsideWorld(false);
			}
		}
	}

	/**
	 * Triggered when the GameObject moves outside of the world.
	 * 
	 * @param horizontal
	 *            is true when the object moves outside the left or right edge.
	 *            False when it moves outside the top or bottom edge.
	 */
	public void outsideWorld(boolean horizontal) {
		// Override to use this method
	}

	/**
	 * Get the sprite position as rectangle
	 * 
	 * @return Rectangle with the current positon + size of the GameObject
	 */
	public final Rect getPosition() {
		return position;
	}

	/**
	 * The speed of the sprite animation.
	 * 
	 * @param speed
	 *            The number of game loops that must occur before the next
	 *            frame, so higher speed is slower animation. 0 stops animation.
	 */
	public final void setAnimationSpeed(int speed) {
		sprite.setAnimationSpeed(speed);
	}

	/**
	 * Set the frame number of the sprite when animated.
	 * 
	 * @param number
	 *            The frame number, frame numbers start at 0
	 */
	public final void setFrameNumber(int number) {
		sprite.setFrameNumber(number);
	}

	/**
	 * Delete the GameObject
	 */
	public final void deleteThisGameObject() {
		active = false;
	}

	/**
	 * Set the visibility of the GameOject. The GameObject will still exist.
	 * 
	 * @param visible
	 *            True for visible, false for invisible
	 */
	public final void setVisibility(boolean visible) {
		isVisible = visible;
	}

	/**
	 * Sets the image at the specified position of the layer. This position can
	 * be any number between 0.0f and 1.0f.
	 * 
	 * @param position
	 *            The position value of the image, images with a higher value
	 *            will be drawn in the background
	 */
	public final void setLayerPosition(float position) {
		GameEngine.items.remove(GameEngine.items.indexOf(this));
		GameEngine.items.add(Math.round(GameEngine.items.size() * position),
				this);
	}

	/**
	 * Jump to the object start position
	 * 
	 * Note: this only works when a start position is set, this happens
	 * automatically when 'addGameObject' is called.
	 */
	public void jumpToStartPosition() {
		xlocation = startposition[0];
		ylocation = startposition[1];
	}

	/**
	 * Set a (new) start position which the objects jumps to when using
	 * 'jumpToStartPosition()'.
	 * 
	 * @param x
	 *            The start X position
	 * @param y
	 *            The start Y position
	 */
	public void setStartPosition(int x, int y) {
		startposition[0] = x;
		startposition[1] = y;
	}

	/**
	 * Used for correct garbage collection of the alarms (see the interface
	 * IAlarm) <b> Don't call this method by yourself! If you want your own
	 * implementation, overide this method. </b>
	 * 
	 * @return If the alarm can still be active for this object
	 */
	public boolean alarmsActiveForThisObject() {
		return active;
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
	public final double getAngle(GameObject object) {
		
		double deltaX = object.getFullX() - getFullX();
		double deltaY = object.getFullY() - getFullY();
		
		if (deltaX >= 0 || deltaY >= 0) {
			return Math.toDegrees(Math.atan2(deltaX, deltaX)) + 90;
		} else {
			return Math.toDegrees((Math.atan2(deltaY, deltaX))) + 450;
		}
	}
}