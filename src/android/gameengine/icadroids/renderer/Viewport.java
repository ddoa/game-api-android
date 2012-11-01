package android.gameengine.icadroids.renderer;

import java.util.Random;
import android.graphics.Point;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;

/**
 * The viewport follows a player object. <b> When you use the viewport, make
 * sure you have add a gameplayer to the game!</b>
 * 
 * Viewport is a Singleton, that means that if you want to use methods from it,
 * you must make a new object like 'Viewport vp = Viewport.getInstance()'.
 * 
 * To activate the viewport, you must set useViewPort to 'true'.
 * 
 * @author Leon & Lex
 * 
 */
public class Viewport {

	/**
	 * If you want to make use of the viewport, set useViewport on true
	 */
	public static boolean useViewport = false;

	/**
	 * The player object
	 */
	private MoveableGameObject player;

	/**
	 * Left edge of the game world
	 */
	private int minX;

	/**
	 * Top edge of the game world
	 */
	private int minY;

	/**
	 * Right edge of the game world
	 */
	private int maxX;

	/**
	 * Bottom edge of the game world
	 */
	private int maxY;

	/**
	 * The constant for placing the player horizontally centered
	 */
	public static final int PLAYER_HCENTER = 1;

	/**
	 * The constant for placing the player vertically centered
	 */
	public static final int PLAYER_VCENTER = 2;

	/**
	 * The constant for placing the player on the left side of the screen
	 */
	public static final int PLAYER_LEFT = 4;

	/**
	 * The constant for placing the player on the right side of the screen
	 */
	public static final int PLAYER_RIGHT = 8;

	/**
	 * The constant for placing the player on the top side of the screen
	 */
	public static final int PLAYER_TOP = 16;

	/**
	 * The constant for placing the player on the bottom side of the screen
	 */
	public static final int PLAYER_BOTTOM = 32;

	/**
	 * The constant for fixing the viewport, it won't adjust to player
	 */
	public static final int PLAYER_FIXED = 64;

	private static final Random random = new Random();
	private int viewportX, viewportY;
	protected int screenWidth, screenHeight;
	private boolean updateViewport = true;
	private int posInViewport = 3;
	private int leftLimit;
	private int rightLimit;
	private int bottomLimit;
	private int topLimit;
	private int playerX = 0;
	private int playerY = 0;

	/**
	 * Required by the Singleton pattern
	 */
	private static final Viewport instance = new Viewport();

	/**
	 * The amount of tolerance before the viewport starts moving. v = vertical,
	 * h = horizontal. When tolerance is zero, the viewport moves immediately
	 * when the player moves. When tolerance is 1, you can move to the edge of
	 * the screen before the viewport moves.
	 */
	private double vtolerance = 0.00;
	private double htolerance = 0.00;

	/**
	 * The amount of zooming for the viewport. 1.0 is the standard view, lower
	 * than 1.0 will cause the viewport to zoom out, higher than 1.0 will cause
	 * the viewport to zoom in.
	 */
	protected float zoomFactor = 1f;

	/**
	 * Constructs a new viewport
	 * 
	 * @param context
	 *            The context that provides additional information for the
	 *            viewport
	 * @param ge
	 *            The Game Engine that this viewport is part of
	 */
	private Viewport() {
		// Singleton
	}

	/**
	 * Sets the object that will be the player character
	 * 
	 * @param player
	 *            The object that will act as a player
	 */
	public void setPlayer(MoveableGameObject player) {
		this.player = player;
	}

	/**
	 * Returns an instance of the private Viewport class.
	 * 
	 * @return An instance of the Viewport class
	 */
	public static Viewport getInstance() {
		return instance;
	}

	/**
	 * This method sets the viewport limits.
	 */
	protected final void setViewportLimits() {
		if (player == null) {
			return;
		}
		double dist = (1 - vtolerance)
				* (screenHeight - player.getFrameHeight());
		if ((posInViewport & PLAYER_TOP) != 0) {
			topLimit = 0;
			bottomLimit = player.getFrameHeight() + (int) dist - screenHeight;
		} else if ((posInViewport & PLAYER_VCENTER) != 0) {
			topLimit = -(int) (dist / 2);
			bottomLimit = player.getFrameHeight() + (int) (dist / 2)
					- screenHeight;
		} else if ((posInViewport & PLAYER_BOTTOM) != 0) {
			topLimit = -(int) dist;
			bottomLimit = player.getFrameHeight() - screenHeight;
		}
		dist = (1 - htolerance) * (screenWidth - player.getFrameWidth());
		if ((posInViewport & PLAYER_LEFT) != 0) {
			leftLimit = 0;
			rightLimit = player.getFrameWidth() + (int) dist - screenWidth;
		} else if ((posInViewport & PLAYER_HCENTER) != 0) {
			leftLimit = -(int) (dist / 2);
			rightLimit = player.getFrameWidth() + (int) (dist / 2)
					- screenWidth;
		} else if ((posInViewport & PLAYER_RIGHT) != 0) {
			leftLimit = -(int) dist;
			rightLimit = player.getFrameWidth() - screenWidth;
		}
	}

	/**
	 * This method sets the viewport for the first time.
	 */
	protected void updateViewportFirstTime() {
		if (posInViewport == PLAYER_FIXED) {
			return;
		}

		if ((posInViewport & PLAYER_TOP) != 0) {
			viewportY = player.getY();
		} else if ((posInViewport & PLAYER_VCENTER) != 0) {
			viewportY = player.getY()
					- (screenHeight - player.getFrameHeight()) / 2;
		} else if ((posInViewport & PLAYER_BOTTOM) != 0) {
			viewportY = player.getY()
					- (screenHeight - player.getFrameHeight());
		}

		if ((posInViewport & PLAYER_LEFT) != 0) {
			viewportX = player.getX();
		} else if ((posInViewport & PLAYER_HCENTER) != 0) {
			viewportX = player.getX() - (screenWidth - player.getFrameWidth())
					/ 2;
		} else if ((posInViewport & PLAYER_RIGHT) != 0) {
			viewportX = player.getX() - (screenWidth - player.getFrameWidth());
		}
		updateViewport = true;
	}

	/**
	 * This method centers the player in the middle of the viewport
	 */
	public void update() {
		checkMovement();
		if (!updateViewport) {
			return;
		}

		if ((posInViewport & PLAYER_TOP + PLAYER_VCENTER + PLAYER_BOTTOM) != 0) {
			viewportY = Math.max(Math.min(viewportY, player.getY() + topLimit),
					player.getY() + bottomLimit);
		}

		if ((posInViewport & PLAYER_LEFT + PLAYER_HCENTER + PLAYER_RIGHT) != 0) {
			viewportX = Math.max(
					Math.min(viewportX, player.getX() + leftLimit),
					player.getX() + rightLimit);
		}

		viewportX = Math.max(minX, Math.min(viewportX, maxX - screenWidth));
		viewportY = Math.max(minY, Math.min(viewportY, maxY - screenHeight));

		updateViewport = false;
	}

	/**
	 * Generates a random integer between 0 (zero) and the given range, not
	 * including range.
	 * 
	 * @param range
	 *            the maximum number that may be returned
	 * @return a pseudo-random integer
	 */
	public static final int random(int range) {
		return (random.nextInt() << 1 >>> 1) % range;
	}

	/**
	 * Checks if the player has moved. If the player has moved then the viewport
	 * will update to the new location of the player.
	 */
	private void checkMovement() {
		if (playerX != player.getX() || playerY != player.getY()) {
			updateViewport = true;
			playerX = player.getX();
			playerY = player.getY();
		}
	}

	/**
	 * Check if the given gameitem lies within the viewport
	 * 
	 * @param item
	 *            the gameitem that needs to be checked
	 * @return true, if it lies in the viewport, false otherwise
	 */
	public final boolean isInViewport(GameObject item) {
		return item.getX() + item.getFrameWidth() > viewportX
				&& item.getY() + item.getFrameHeight() > viewportY
				&& item.getX() < viewportX + screenWidth
				&& item.getY() < viewportY + screenHeight;
	}

	/**
	 * Sets the position of the player on the screen. In many games, the game
	 * world is bigger than the screen. When you specify the player position,
	 * you can make the screen (viewport) move along with the player. The
	 * parameter tells how this must be done. For example, you can specify
	 * HCENTER or VCENTER, if you want to keep the player more or less in the
	 * middle of the viewport, or BOTTOM if you want the player in the bottom of
	 * the screen. <br/>
	 * Note: if you position the player at one of the edges of the screen, like
	 * BOTTOM, this means that you can not move out of the screen that way, but
	 * you can move up a bit. How much that is, is specified by
	 * setPlayerPositionTolerance.
	 * 
	 * @param pos
	 *            one of the following combinations: <br>
	 *            <ul>
	 *            <li>
	 *            <p>
	 *            PLAYER_FIXED <br/>
	 *            Viewport won't move. If your world is bigger than the screen,
	 *            the player can move out of sight on all sides!
	 *            </p>
	 *            <li>
	 *            <p>
	 *            PLAYER_TOP, PLAYER_VCENTER, PLAYER_BOTTOM <br/>
	 *            Player is at top, center or bottom of viewport. Horizontal
	 *            positioning is not controled, you can move out of view at the
	 *            sides!
	 *            </p>
	 *            <li>
	 *            <p>
	 *            PLAYER_LEFT, PLAYER_HCENTER, PLAYER_RIGHT <br/>
	 *            Player is at left, center or right of viewport. Vertical
	 *            positioning is not controled, you can move out of view at the
	 *            top or bottom!
	 *            </p>
	 *            <li>
	 *            <p>
	 *            PLAYER_TOP | PLAYER_LEFT, PLAYER_TOP | PLAYER_HCENTER,
	 *            PLAYER_TOP | PLAYER_RIGHT <br/>
	 *            PLAYER_VCENTER | PLAYER_LEFT, PLAYER_VCENTER | PLAYER_HCENTER,
	 *            PLAYER_VCENTER | PLAYER_RIGHT <br/>
	 *            PLAYER_BOTTOM | PLAYER_LEFT, PLAYER_BOTTOM | PLAYER_HCENTER,
	 *            PLAYER_BOTTOM | PLAYER_RIGHT <br/>
	 *            Combinations of vertical and horizontal positioning. The
	 *            following image illustrates this type of positioning: <br/>
	 *            </ul>
	 */
	public final void setPlayerPositionOnScreen(int pos) {
		posInViewport = pos;
		setViewportLimits();
	}

	/**
	 * Set the tolerance of the positioning. When tolerance is zero, the
	 * viewport moves immediately when the player moves. When tolerance is 1,
	 * you can move to the edge of the screen before the viewport moves. Values
	 * in between result in a smaller or bigger delay before the viewport moves. <br/>
	 * Example: In a left-to right platform game, you may position the player at
	 * LEFT, VCENTER. If you set the horizontal tolerance at 0.3, you may move
	 * to the right 30% of the screen before the viewport moves along. If you
	 * set vertical tolerance at 0.8, you can move 80% of the way up, before the
	 * viewport moves up also.
	 * 
	 * @param ht
	 *            horizontal tolerance, a value between 0 and 1
	 * @param vt
	 *            vertical tolerance, a value between 0 and 1
	 */
	public final void setPlayerPositionTolerance(double ht, double vt) {
		htolerance = ht;
		vtolerance = vt;
		setViewportLimits();
	}

	/**
	 * Set the boundaries of this game. If you want the game world to be as big
	 * as the screen, you need not use this method. By default the bounds will
	 * be set to the size of the screen. That is: minX=0, maxX=screenWidth,
	 * minY=0, maxY=screenHeight.
	 * 
	 * @param minX
	 *            the minimal horizontal coordinate
	 * @param minY
	 *            the minimal vertical coordinate
	 * @param maxX
	 *            the maximal horizontal coordinate
	 * @param maxY
	 *            the maximal vertical coordinate
	 */
	public void setBounds(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	/**
	 * Gets left edge of the game world.
	 * 
	 * @return int x-position of left edge, as specified by setBounds.
	 */
	public int getMinX() {
		return minX;
	}

	/**
	 * Gets right edge of the game world.
	 * 
	 * @return int, x-position of right edge, as specified by setBounds.
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * Gets top edge of the game world.
	 * 
	 * @return int y-position of top edge, as specified by setBounds.
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Gets bottom edge of the game world.
	 * 
	 * @return int y-position of bottom edge, as specified by setBounds.
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Get random x between left edge of world and right edge minus the
	 * specified width. In this way you can find a random position for an item
	 * of the given width and the item will fit completely into the world.
	 * 
	 * @param width
	 * @return a random x-position
	 */
	public int getRandomX(int width) {
		return minX + random(maxX - minX - width);
	}

	/**
	 * Get random y between top edge of world and bottom edge minus the
	 * specified height. In this way you can find a random position for an item
	 * of the given height and the item will fit completely into the world.
	 * 
	 * @param height
	 * @return a random y-position
	 */
	public int getRandomY(int height) {
		return minY + random(maxY - minY - height);
	}

	/**
	 * Set the position of the viewport manually. You must do this if you set
	 * the player position in viewport to FIXED. If it is not fixed, it is no
	 * use setting the viewport, because the viewport will adjust to the player
	 * position.
	 * 
	 * @param x
	 *            xpos of viewport
	 * @param y
	 *            ypos of viewport
	 */
	public void setViewport(int x, int y) {
		viewportX = x;
		viewportY = y;
	}

	/**
	 * Gets the x-position of the viewport.
	 * 
	 * @return x position of the viewport
	 */
	public int getViewportX() {
		return viewportX;
	}

	/**
	 * Gets the y-position of the viewport.
	 * 
	 * @return y position of the viewport
	 */
	public int getViewportY() {
		return viewportY;
	}

	/**
	 * Gets the position of the viewport.
	 * 
	 * @return a Point-object containing the x- and y-position of the viewport
	 */
	public Point getViewportLocation()
	{
		return new Point(viewportX, viewportY);
	}
	
	/**
	 * Gets the zoomfactor for zooming in.
	 * 
	 * @return the value of the zoomfactor
	 */
	public float getZoomFactor() {
		return zoomFactor;
	}

	/**
	 * Sets the zoomfactor for the viewport. A higher zoomfactor will zoom the
	 * viewport in further.
	 * 
	 * @param zoomFactor
	 *            the zoomfactor for the viewport.
	 */
	public void setZoomFactor(float zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
}