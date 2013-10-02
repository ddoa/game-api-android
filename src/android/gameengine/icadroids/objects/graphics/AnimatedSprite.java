package android.gameengine.icadroids.objects.graphics;

import android.gameengine.icadroids.engine.GameView;
import android.graphics.Rect;

/**
 * AnimatedSprite is the same as sprite, but has some extra animation
 * functionalities in it.
 * 
 * An animatedsprite is a film strip of pictures that will be played after each
 * other.
 * 
 * To start animating a sprite, call 'startAnimate()'
 * 
 * @author Bas van der Zandt
 * 
 */
public class AnimatedSprite extends Sprite {

	/**
	 * The position of the current frame
	 */
	private Rect currentFrame = new Rect(0, 0, 0, 0);
	/**
	 * Number of frames that are available
	 */
	private int numberOfFrames = 1;
	/**
	 * The current frame of the animation
	 */
	private int currentFrameNumber = 0;
	/**
	 * speed of the animation
	 */
	private int animationSpeed = 1;
	/**
	 * counts how many updates has passed
	 */
	private int updateCounter = 0;
	/**
	 * The framesize
	 */
	private int frameWidth = 0;
	/**
	 * Animation is on or off
	 */
	private boolean animate = false;

	/**
	 * Make an AnimatedSprite without loading any bitmap in it
	 */
	public AnimatedSprite() {

	}
	
	/**
	 * Create an Animated sprite using the specified image and having
	 * the specified number of frames.
	 * Sprite images must be stored in the res/drawable
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
	public AnimatedSprite(String resourceName, int numberOfFrames)
	{
		loadAnimatedSprite(resourceName, numberOfFrames);
	}
	
	/**
	 * Load a sprite with the given resource.
	 * Sprites must be stored in the res/drawable
	 * folders of your Android project. If you have only one image, store it in
	 * the 'nodpi' folder. If you have various versions (hi-res and lo-res) for
	 * rendering on devices of varying screen sizes, use the lo- & hi-dpi folders.
	 * 
	 * @param resourceName
	 *            The name of the resource in the /res/drawable folder <b>
	 *            without extension </b>, so when your picture in the
	 *            /res/drawable is named 'picture.jpg', this parameter should be
	 *            "picture" .
	 * @param numberOfFrames
	 *            The number of frames in the strip
	 *            
	 * @see android.gameengine.icadroids.objects.graphics.Sprite#loadSprite(java.lang.String)
	 */
	public final void loadAnimatedSprite(String resourceName, int numberOfFrames) {
		super.loadSprite(resourceName);
		// Note: frameWidth will be calculated by override of initialize(), 
		// in case loading is postponed.
		this.numberOfFrames=numberOfFrames;
		if (GameView.surfaceLoaded) {
			// so we assume the sprite has been loaded and not postponed...
			//if(!animate){    NO!!! animate is pas true als je animatie gestart hebt
			//	frameWidth = spriteWidth;    dus nu altijd!
			//}
			calculateFrameWidth();
			calculateFramePosition(currentFrameNumber);
			//GameEngine.printDebugInfo("AnimatedSprite", "animated sprite loaded");
		}
	}

	/**
	 * Initialize the sprite image.
	 * <br />
	 * Note: Don't call this yourself. 
	 * This method is automatically called by the GameEngine when loading of 
	 * the sprite has been postponed. (In the Android Activity lifecycle, sprites
	 * can only be loaded after a certain point, or an error will be thrown.
	 * The GameEngine postpones loading until after this point)
	 */
	@Override
	public void initialize() {
		super.initialize();
		// sprite moet er nu zijn, dus spriteWidth, dus FrameWidth uitrekenen
		calculateFrameWidth();
		calculateFramePosition(currentFrameNumber);
	}

	/**
	 * Method that needs to be called every update of the gameloop. <b>
	 * GameObjects handle this method by itself!</b>
	 */
	public final void updateToNextFrame() {
		if (animate && animationSpeed > 0 && spriteBitmap != null) {
			updateCounter++;
			if ((updateCounter % animationSpeed) == 0) {
				nextFrame();
				updateCounter = 0;
			}
		}
	}

	/**
	 * Go to the next frame of the animation
	 * By default this method will make your animated sprite cycle
	 * through all frames. Override this method if you want to create
	 * custom animations
	 */
	public final void nextFrame() {
		currentFrameNumber++;
		if (currentFrameNumber >= numberOfFrames) {
			currentFrameNumber = 0;
		}
		calculateFramePosition(currentFrameNumber);
	}

	/**
	 * Go to the previous frame of the animation
	 */
	public final void previousFrame() {
		currentFrameNumber--;
		if (currentFrameNumber < 0) {
			currentFrameNumber = (numberOfFrames - 1);
		}
		calculateFramePosition(currentFrameNumber);
	}

	/**
	 * Stop animating the sprite
	 */
	public final void stopAnimate() {
		animate = false;
	}
	
	/**
	 * Set the frame number of the animation
	 * 
	 * @param frameNumber
	 *            The frame number to jump to
	 */
	public final void setFrameNumber(int frameNumber) {
		this.currentFrameNumber = 
				Math.max(0, Math.min(frameNumber, numberOfFrames-1));
		calculateFramePosition(currentFrameNumber);
	}

	/**
	 * Calculate the frame position used for correct rendering of the animated
	 * sprite
	 * 
	 * @param frameNumber
	 *            The framenumber to calculate
	 */
	private final void calculateFramePosition(int frameNumber) {
		currentFrame.set((frameWidth * frameNumber), 0,
				(frameWidth * (frameNumber + 1)), spriteHeight);
	}

	/**
	 * Get the current frame number of the animation
	 * 
	 * @return The current frame number of the animation
	 */
	public final int getCurrentFrameNumber() {
		return currentFrameNumber;
	}

	/**
	 * Get the current frame size as rectangle
	 * 
	 * @return The current frame size as rectangle
	 */
	public final Rect getCurrentFrameRectangle() {
		return currentFrame;
	}

	/**
	 * Start animating the sprite
	 * 
	 */
	public final void startAnimate() {
		animate = true;
		calculateFramePosition(currentFrameNumber);
	}
	
	/**
	 * calculate the number of frames from the sprite and frame widths.
	 */
	private void calculateFrameWidth(){
		frameWidth = spriteWidth / numberOfFrames;
	}

	/**
	 * Set the speed of the animation
	 * 
	 * @param animationSpeed
	 *            How many update must occur before going to the next frame. So
	 *            when animationSpeed is 3, it takes 3 updates to go to the next
	 *            frame.
	 */
	public final void setAnimationSpeed(int animationSpeed) {
		this.animationSpeed = animationSpeed;
	}

	/**
	 * Get the animation speed
	 * 
	 * @return animation speed
	 */
	public int getAnimationSpeed() {
		return animationSpeed;
	}

	/**
	 * Get the frame width of this AnimatedSprite
	 * 
	 * @see android.gameengine.icadroids.objects.graphics.Sprite#getFrameWidth()
	 */
	@Override
	public final int getFrameWidth() {
		if (spriteBitmap != null) {
			return frameWidth;
		} else {
			return 0;
		}
	}

	/**
	 * Get the number of frames of the sprite
	 * 
	 * @return The number of frames
	 */
	public int getNumberOfFrames() {
		return numberOfFrames;
	}

	/**
	 * Get the middle of the frame, horizontally
	 * 
	 * @see
	 * android.gameengine.icadroids.objects.graphics.Sprite#getSpriteCenterX()
	 */
	@Override
	public final float getSpriteCenterX() {
		return getFrameWidth() / 2;
	}

	/**
	 * Get the middle of the frame, vertically
	 * 
	 * @see
	 * android.gameengine.icadroids.objects.graphics.Sprite#getSpriteCenterY()
	 */
	@Override
	public final float getSpriteCenterY() {
		return getFrameHeight() / 2;
	}
}
