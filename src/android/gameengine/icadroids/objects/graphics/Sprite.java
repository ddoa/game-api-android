package android.gameengine.icadroids.objects.graphics;

import java.util.Vector;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.engine.GameView;
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
	 * Save the resource location to load at a later time when the surface is
	 * not created yet
	 */
	private String loadDelay;

	public static Vector<Sprite> loadDelayedSprites;

	/**
	 * Make a Sprite without loading any bitmap in it
	 */
	public Sprite() {

		// overloaded constructor
	}

	/**
	 * Load a sprite with the given resource
	 * Sprites must be stored in the res/drawable
	 * folders of your Android project. If you have only one image, store it in
	 * the 'nodpi' folder. If you have various versions (hi-res and lo-res) for
	 * rendering on devices of varying screen sizes, use the lo- & hi-dpi folders.
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
	 * Sprites must be stored in the res/drawable
	 * folders of your Android project. If you have only one image, store it in
	 * the 'nodpi' folder. If you have various versions (hi-res and lo-res) for
	 * rendering on devices of varying screen sizes, use the lo- & hi-dpi folders.
	 * 
	 * @param resourceName
	 *            The name of the resource in the /res/drawable folder <b>
	 *            without extension </b>, so when your picture in the
	 *            /res/drawable is named 'picture.jpg', this parameter should be
	 *            'picture' .
	 */
	public void loadSprite(String resourceName) {
		if (GameView.surfaceLoaded) {
			int resID = GameEngine
					.getAppContext()
					.getResources()
					.getIdentifier(resourceName, "drawable",
							GameEngine.getAppContext().getPackageName());

			spriteBitmap = BitmapFactory.decodeResource(GameEngine
					.getAppContext().getResources(), resID);
			calculateSize(spriteBitmap);

		} else {
			loadDelay = resourceName;
			if (loadDelayedSprites != null) {
				loadDelayedSprites.add(this);
			} else {
				loadDelayedSprites = new Vector<Sprite>();
				loadDelayedSprites.add(this);
			}
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
	public void initialize() {
		if (loadDelay != null) {
			loadSprite(loadDelay);
		}
	}

	/**
	 * Calculate the size of the sprite. Throws a null pointer exception when no sprite is
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
	 * Get the sprite in Bitmap. Throws a null pointer exception when no sprite is loaded
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
