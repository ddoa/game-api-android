package android.gameengine.icadroids.objects;

import android.gameengine.icadroids.objects.graphics.AnimatedSprite;
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
	private boolean active = true;
	/**
	 * Indicates if the object is visible or not.
	 */
	private boolean isVisible = true;
	/**
	 * The exact x position of the upper left corner of the object.
	 */
	protected double xlocation = 0;
	/**
	 * the exact y position of the upper left corner of the object.
	 */
	protected double ylocation = 0;
	/**
	 * Depth of the object in rendering. Number between 0 and 1. 1  means at the top (foreground)
	 * 0 means at the bottom (background)
	 */
	private float depth = 0;
	/**
	 * The sprite (image) of the object.
	 */
	private AnimatedSprite sprite = new AnimatedSprite();
	/**
	 * The bounding rectangle that contains the sprite on the screen
	 */
	public Rect position = new Rect(0, 0, 0, 0);
	/**
	 * Start position of the object
	 */
	int[] startposition = new int[2];

	/**
	 * Initialize resources.
	 * 
	 */
	public final void intializeGameObject() {
		intialize();
	}

	/**
	 * Called when the application starts. You can override this method to do initialization
	 * at the <i>very start of the game</i>. Therefore it is only useful for objects that are
	 * always present in the game, from start of the game. 
	 * These objects must be created in the constructor of your game.
	 * 
	 */
	public void intialize() {

	}

	/**
	 * Ask if an object is still alive, that is: it hasn't been deleted from the game
	 * or added in this cycle of the game.
	 * 
	 * @return
	 * 		boolean indicating if this object still is in the game 
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * Ask if an object is visible
	 * 
	 * @return
	 * 		boolean indicating if this object still is visible 
	 */
	public boolean isVisible()
	{
		return isVisible;
	}

	/**
	 * Mark an object as 'dead', to be removed at end of cycle.<br />
	 * Note: don't use this method to delete objects, use deleteGameObject
	 * instead. This method will call clearActive() and it will also do other
	 * necessary actions, like removing Alarms belonging to the object.
	 * <br />
	 * Note: this method should be invisible to game programmers.
	 */
	public void clearActive()
	{
		active = false;
	}
	
	/**
	 * The update-method will be called every cycle of the game loop.
	 * Override this method to give an object any time driven behaviour.
	 * <br />
	 * Note: Always call <i>super.update()</i> first in your overrides, 
	 * because the default update does some important actions, like
	 * sprite animation.
	 */
	public void update() {
		sprite.updateToNextFrame();
		updatePlayerFramePosition();
	}

	/**
	 * Override this method to implement your own rendered canvas objects
	 * like text, rectangles or colors. When you are using a viewport,
	 * the rendering will be relative to the screen. If you want absolute
	 * rendering, use 'drawCustomObjects'.
	 * 
	 *<b> Don't forget to call super(canvas)!! </b>
	 * Draw the GameObject on the screen, called by game renderer
	 * 
	 * @param canvas The Android canvas. Use this provided object to
	 * draw for example text, rectangles and colors. For the api see:
	 * http://developer.android.com/reference/android/graphics/Canvas.html
	 */
	public void drawGameObject(Canvas canvas) {
		if (sprite.getSprite() != null && isVisible) {
			canvas.drawBitmap(sprite.getSprite(),
					sprite.getCurrentFrameRectangle(), position, null);
		}
	}
	
	/**
	 * Override this method to draw your own text, rectangle and
	 * other objects on the screen (like a game dashboard). The objects
	 * will be rendered on the absolute x,y position.<br />
	 * <b>Note:</b> This method was used to paint things on the screen. In the
	 * cuurent version you can use the Dashboard instead.
	 * 
	 * @param canvas The Android canvas. Use this provided object to
	 * draw for example text, rectangles and colors. For the api see:
	 * http://developer.android.com/reference/android/graphics/Canvas.html
	 */
	public void drawCustomObjects(Canvas canvas){
		//Override this method to implement your own rendered objects
	}

	/**
	 * Update position rectangle used for drawing the sprite on the right
	 * position with the right size on the screen. This rectangle is also used
	 * for collision detection between GameObjects.
	 */
	protected void updatePlayerFramePosition() {
		position.set(getX(), getY(), getX() + sprite.getFrameWidth(), getY()
				+ getFrameHeight());
	}

	/**
	 * Set a sprite for the GameObject. Sprite images must be stored in the res/drawable
	 * folders of your Android project. If you have only one image, store it in
	 * the 'nodpi' folder. If you have various versions (hi-res and lo-res) for
	 * rendering on devices of varying screen sizes, use the lo- & hi-dpi folders.
	 * 
	 * <b>Note: Extremely small sprites don't work well with Collision
	 * Detection!</b>
	 * 
	 * @param resourceName
	 *            The name of the resource in the /res/drawable folder <b>
	 *            without extension </b>, so when your picture in the
	 *            /res/drawable is named 'picture.jpg', this parameter should be
	 *            "picture" .
	 */
	public final void setSprite(String resourceName) {
		sprite.loadAnimatedSprite(resourceName, 1);
	}

	/**
	 * Set a sprite for the GameObject. Sprite images must be stored in the res/drawable
	 * folders of your Android project. If you have only one image, store it in
	 * the 'nodpi' folder. If you have various versions (hi-res and lo-res) for
	 * rendering on devices of varying screen sizes, use the lo- & hi-dpi folders.
	 * 
	 * <b>Note: Extremely small sprites don't work well with Collision
	 * Detection!</b>
	 * 
	 * @param resourceName
	 *            The name of the resource in the /res/drawable folder <b>
	 *            without extension </b>, so when your picture in the
	 *            /res/drawable is named 'picture.jpg', this parameter should be
	 *            "picture" .
	 * @param numberOfFrames
	 *            The number of frames in the strip
	 */
	public final void setSprite(String resourceName, int numberOfFrames) {
		sprite.loadAnimatedSprite(resourceName, numberOfFrames);
	}

	/**
	 * Set a sprite for the GameObject. Use this method if you create
	 * sprites yourself, for instance if you create subclasses of
	 * AnimatedSprite that have special animations.
	 * 
	 * @param theSprite
	 *         AnimatedSprite that has been created earlier
	 */
	public final void setSprite(AnimatedSprite theSprite) {
		sprite = theSprite;
	}
	/**
	 * Start animating the sprite, from the current frame
	 */
	public final void startAnimate() {
		sprite.startAnimate();
	}

	/**
	 * Stop animating the sprite. The animation will stop at the current frame
	 * and will not go back to the first frame
	 */
	public final void stopAnimate() {
		sprite.stopAnimate();
	}

	/**
	 * Get the x position on the screen, rounds the exact X-pos to an int.
	 * 
	 * @return x position on the screen as an int
	 */
	public final int getX() {
		return (int) Math.round(xlocation);
	}

	/**
	 * Get the y position on the screen, rounds the exact Y-pos to an int.
	 * 
	 * @return the y position on the screen as an int
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
	 * Get the exact x position of the GameObject
	 * 
	 * @return the exact x position, as a double
	 */
	public final double getFullX() {
		return xlocation;
	}

	/**
	 * Get the exact y position of the GameObject
	 * 
	 * @return the exact y position, as a double
	 */
	public final double getFullY() {
		return ylocation;
	}

	/**
	 * Set x position of the GameObject WARNING: Do NOT use this method to move
	 * the player or a lot of methods will behave incorrectly.
	 * 
	 * @param x
	 *            The x position
	 */
	public final void setX(double x) {
		this.xlocation = x;
	}

	/**
	 * Set y position of the GameObject. WARNING: Do NOT use this method to move the
	 * player or a lot of methods will behave incorrectly.
	 * 
	 * @param y
	 *            The y position
	 */
	public final void setY(double y) {
		this.ylocation = y;
	}

	/**
	 * Set the position on the screen. WARNING: Do NOT use this method to move the
	 * player or a lot of methods will behave incorrectly.
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
	 * Get the frame width of the object's sprite, this will be also the width
	 * of the object in the game.
	 * 
	 * @return The width of the frame in pixels
	 */
	public final int getFrameWidth() {
		return sprite.getFrameWidth();
	}

	/**
	 * Get the frame height of the object's sprite, this will be also the height
	 * of the object in the game.
	 * 
	 * @return The height of the frame in pixels
	 */
	public final int getFrameHeight() {
		return sprite.getFrameHeight();
	}

	/**
	 * Get the sprite position as rectangle
	 * 
	 * @return Rectangle with the current position + size of the GameObject
	 */
	public final Rect  getPosition() {
		return position;
	}

	/**
	 * Set the speed of the sprite animation.
	 * 
	 * @param speed
	 *            The number of game loops that must occur before the next
	 *            frame, so higher speed is slower animation. 0 stops animation.
	 */
	public final void setAnimationSpeed(int speed) {
		sprite.setAnimationSpeed(speed);
	}

	/**
	 * Set the frame number of the sprite. This method can be used both
	 * with animated sprites and non-animated sprites (for instance when
	 * there are frames for looking left, right, ...).<br />
	 * Of course, sprite must be a film strip.
	 * 
	 * @param number
	 *            The frame number, frame numbers range from 0 to NrOfFrames-1
	 */
	public final void setFrameNumber(int number) {
		sprite.setFrameNumber(number);
	}

	/**
	 * Delete the GameObject. This will also delete the Alarms of this object.
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
	 *
	public final void setLayerPosition(float position) {
		GameEngine.items.remove(GameEngine.items.indexOf(this));
		GameEngine.items.add(Math.round(GameEngine.items.size() * position),
				this);
	} Deleted, because too hard to keep the itemslist ok during update()....
	*/

	/**
	 * Jump to the object's start position
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
	 * @deprecated
	 */
	//public boolean alarmsActiveForThisObject() {
	//	return active;
	//}
	
	/**
	 * Use this function to get the angle between you and another object. For
	 * example: You can use this function to check if you approaching another
	 * object from the left or right.
	 * Direction 0 points up, directions go clockwise, so 90 is right, etc.
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

	/**
	 * Get the depth of the object in rendering. 
	 * Depth is a number between 0 and 1. 1  means at the top (foreground)
	 * 0 means at the bottom (background)
	 * 
	 * @return depth, a float
	 */
	public float getDepth() {
	    return depth;
	}

	/**
	 * Set the depth of the object in rendering. Number between 0 and 1. 
	 * 1  means at the top (foreground), 0 means at the bottom (background).
	 * <br />
	 * Setting the depth is only useful <b>before</b> you add the Object to the game,
	 * because it influences its position in the list of Objects. Using this method on Objects
	 * that have already been added has no effect.
	 * Instead of using this method, it is recommended to use the addGameObject method with depth-parameter
	 * in GameEngine
	 * 
	 * @param depth the depth, a float
	 * @see android.gameengine.icadroids.engine.GameEngine#addGameObject(GameObject gameObject, float layerposition)
	 */
	public void setDepth(float depth) {
	    this.depth = depth;
	}
	
}