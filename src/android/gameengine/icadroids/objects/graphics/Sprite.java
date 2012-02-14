package android.gameengine.icadroids.objects.graphics;

import android.gameengine.icadroids.engine.GameEngine;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Sprite holds and handles correct loading of images (bitmaps), and includes
 * some useful methods for getting information about the image.
 * 
 * @author Bas van der Zandt
 * 
 */
public class Sprite {

	/**
	 * The bitmap that the sprite uses
	 */
	protected Bitmap spriteBitmap;

	/**
	 * The width in pixels of the sprite
	 */
	protected int spriteWidth;
	/**
	 * The height in pixels of the sprite
	 */
	protected int spriteHeight;

	/**
	 * Make a Sprite without loading any bitmap in it
	 */
	public Sprite() {

		// overloaded constructor
	}

	/**
	 * Load a sprite with the given resource
	 * 
	 * @param resourceName
	 *            The name of the resource in the /res/drawable folder <b>
	 *            without extension </b>, so when your picture in the
	 *            /res/drawable is named 'picture.jpg', this parameter should be
	 *            'picture' .
	 */
	public Sprite(String resourceName) {
		loadSprite(resourceName);
	}

	/**
	 * Load the given resource in the sprite object
	 * 
	 * @param resourceName
	 *            The name of the resource in the /res/drawable folder <b>
	 *            without extension </b>, so when your picture in the
	 *            /res/drawable is named 'picture.jpg', this parameter should be
	 *            'picture' .
	 */
	public void loadSprite(String resourceName) {
		int resID = GameEngine
				.getAppContext()
				.getResources()
				.getIdentifier(resourceName, "drawable",
						GameEngine.getAppContext().getPackageName());

		spriteBitmap = BitmapFactory.decodeResource(GameEngine.getAppContext()
				.getResources(), resID);
		spriteBitmap.setDensity(Bitmap.DENSITY_NONE); // Don't let Android
														// automaticaly
														// resize your Bitmap!
		calculateSize(spriteBitmap);
	}

	/**
	 * Calculate the size of the sprite. Causes a nullpointer when no sprite is
	 * loaded.
	 * 
	 * @param sprite
	 *            The bitmap that needs to be calculated
	 */
	protected final void calculateSize(Bitmap sprite) {
		spriteHeight = sprite.getHeight();
		spriteWidth = sprite.getWidth();
	}

	/**
	 * Get the sprite in Bitmap. Returns nullpointer when no sprite is loaded
	 * 
	 * @return The sprite in bitmap
	 */
	public final Bitmap getSprite() {
		return spriteBitmap;
	}

	/**
	 * Get the width of the sprite
	 * 
	 * @return Sprite width in pixels, <b>returns 0 when no sprite is loaded
	 *         </b>
	 */
	public int getFrameWidth() {
		if (spriteBitmap != null) {
			return spriteWidth;
		} else {
			return 0;
		}
	}

	/**
	 * Get the height of the sprite
	 * 
	 * @return Sprite height in pixels, <b>returns 0 when no sprite is loaded
	 *         </b>
	 */
	public final int getFrameHeight() {
		if (spriteBitmap != null) {
			return spriteHeight;
		} else {
			return 0;
		}
	}

	/**
	 * Load a bitmap into a sprite
	 * 
	 * Note: bitmaps are already loaded images, when you want to load a resource
	 * that hasn't been loaded yet, use 'loadSprite()'
	 * 
	 * @param sprite
	 *            The bitmap that need to be loaded
	 */
	public final void setSprite(Bitmap sprite) {
		calculateSize(sprite);
		spriteBitmap = sprite;
	}

	/**
	 * Get the center width of the sprite
	 * 
	 * @return Sprite width / 2
	 */
	public float getSpriteCenterX() {
		return spriteWidth / 2;
	}

	/**
	 * Get the center height of the sprite
	 * 
	 * @return Sprite height / 2
	 */
	public float getSpriteCenterY() {
		return spriteHeight / 2;
	}
}
