package android.gameengine.icadroids.objects.graphics;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.gameengine.icadroids.objects.graphics.Sprite#loadSprite(java.
	 * lang.String)
	 */
	@Override
	public final void loadSprite(String resourceName) {
		super.loadSprite(resourceName);
		frameWidth = spriteWidth;
		calculateFramePosition(currentFrameNumber);
	}

	/**
	 * Method that needs to be called every update of the gameloop. <b>
	 * GameObjects handle this method by itself!</b>
	 */
	public final void updateToNextFrame() {
		if (animate && animationSpeed > 0) {
			updateCounter++;
			if ((updateCounter % animationSpeed) == 0) {
				nextFrame();
				updateCounter = 0;
			}
		}
	}

	/**
	 * Go to the next frame of the animation
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
	 * @param currentFrameNumber
	 *            The frame number to jump to
	 */
	public final void setFrameNumber(int currentFrameNumber) {
		this.currentFrameNumber = currentFrameNumber;
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
	 * @param frameWidth
	 *            The size of each frame
	 */
	public final void startAnimate(int frameWidth) {
		this.frameWidth = frameWidth;
		calculateSize(spriteBitmap);
		numberOfFrames = spriteWidth / frameWidth;
		animate = true;
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

	/*
	 * (non-Javadoc)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.gameengine.icadroids.objects.graphics.Sprite#getSpriteCenterX()
	 */
	@Override
	public final float getSpriteCenterX() {
		return getFrameWidth() / 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.gameengine.icadroids.objects.graphics.Sprite#getSpriteCenterY()
	 */
	@Override
	public final float getSpriteCenterY() {
		return getFrameHeight() / 2;
	}
}
