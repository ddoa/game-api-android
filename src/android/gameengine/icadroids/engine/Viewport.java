package android.gameengine.icadroids.engine;

import android.graphics.Point;
import android.graphics.Rect;
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
public class Viewport
{

	/**
	 * If you want to make use of the viewport, set useViewport on true
	 */
	public static boolean useViewport = false;

	/**
	 * Boolean indicating if the viewport has been initialized at startup of the
	 * game. If false, properties will just be set and effectuated when the
	 * GameView is ready. If true, property changes will be carried out
	 * immediately.
	 */
	private boolean isInitialized = false;

	/**
	 * The player object
	 */
	private MoveableGameObject player;

	/**
	 * Left edge of the game world.<br />
	 * At the moment this is always 0, but the variable is kept in (possible
	 * change?)
	 */
	private int minX;

	/**
	 * Top edge of the game world.<br />
	 * At the moment this is always 0, but the variable is kept in (possible
	 * change?)
	 */
	private int minY;

	/**
	 * Right edge of the game world.<br />
	 * Since minX is zero at the moment, this equals the width of the game
	 * world.
	 */
	private int maxX;

	/**
	 * Bottom edge of the game world.<br />
	 * Since minX is zero at the moment, this equals the height of the game
	 * world.
	 */
	private int maxY;

	/**
	 * The constant for not adjusting the player position (either vertical or
	 * horizontal)
	 */
	public static final int PLAYER_NOADJUST = 0;

	/**
	 * The constant for placing the player centered
	 */
	public static final int PLAYER_CENTER = 1;

	/**
	 * The constant for placing the player on the left side of the screen
	 */
	public static final int PLAYER_LEFT = 2;

	/**
	 * The constant for placing the player on the right side of the screen
	 */
	public static final int PLAYER_RIGHT = 3;

	/**
	 * The constant for placing the player on the top side of the screen
	 */
	public static final int PLAYER_TOP = 2;

	/**
	 * The constant for placing the player on the bottom side of the screen
	 */
	public static final int PLAYER_BOTTOM = 3;

	/**
	 * x-position of the top left corner of the viewport in the game world.
	 */
	private int viewportX = 0;

	/**
	 * y-position of the top left corner of the viewport in the game world.
	 */
	private int viewportY = 0;

	/**
	 * Width of the viewport, taking zooming into account. The width will
	 * generally equal the View width, unless the game is zoomed.
	 */
	private int viewportWidth;

	/**
	 * Height of the viewport, taking zooming into account. The height will
	 * generally equal the View height, unless the game is zoomed.
	 */
	private int viewportHeight;

	/**
	 * Horizontal position of the player on which the current viewport position
	 * is based. This value will be compared to actual playerX to see if the
	 * viewport needs adjustment.
	 */
	private int playerX = 0;

	/**
	 * Vertical position of the player on which the current viewport position is
	 * based. This value will be compared to actual playerY to see if the
	 * viewport needs adjustment.
	 */
	private int playerY = 0;

	/**
	 * Type of horizontal positioning of the player in the viewport. The value
	 * must be any of the for constants: PLAYER_NOADJUST, PLAYER_LEFT,
	 * PLAYER_CENTER, PLAYER_RIGHT.
	 */
	private int hpositioning = 0;

	/**
	 * Type of horizontal positioning of the player in the viewport. The value
	 * must be any of the for constants: PLAYER_NOADJUST, PLAYER_TOP,
	 * PLAYER_CENTER, PLAYER_BOTTOM.
	 */
	private int vpositioning = 0;

	/**
	 * Left limit for player movement within the viewport (distance from the
	 * player to the left side of the player box that is kept inside the
	 * viewport).
	 * 
	 * @see android.gameengine.icadroids.engine.Viewport#setViewportLimits()
	 */
	private int leftLimit;

	/**
	 * Right limit for player movement within the viewport (distance from the
	 * player to the right side of the player box that is kept inside the
	 * viewport).
	 * 
	 * @see android.gameengine.icadroids.engine.Viewport#setViewportLimits()
	 */
	private int rightLimit;

	/**
	 * Bottom limit for player movement within the viewport (distance from the
	 * player to the lower side of the player box that is kept inside the
	 * viewport).
	 * 
	 * @see android.gameengine.icadroids.engine.Viewport#setViewportLimits()
	 */
	private int bottomLimit;

	/**
	 * Top limit for player movement within the viewport (distance from the
	 * player to the upper side of the player box that is kept inside the
	 * viewport).
	 * 
	 * @see android.gameengine.icadroids.engine.Viewport#setViewportLimits()
	 */
	private int topLimit;

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
	private Viewport()
	{
		// Singleton
	}

	/**
	 * Returns an instance of the private Viewport class.
	 * 
	 * @return An instance of the Viewport class
	 */
	public static Viewport getInstance()
	{
		return instance;
	}

	/**
	 * This method sets the viewport limits. The viewport limits form a box
	 * (rectangle) around the player position. This entire box is kept within
	 * the viewport. The box is defined by limits in four directions (left,
	 * right, top & bottom). These are used as min or max for the viewport
	 * position. For example, to check on the left side: viewportX should not be
	 * greater than playerX+leftLimit, so Math.min is used on these two numbers.
	 * (leftLimit is a number <= 0)<br />
	 * Limits are calculated in such a way that you don't have to do anything
	 * with viewport/player width & height anymore. Just min/max the viewport
	 * pos with playerpos+limit <br />
	 * Note: tolerances influence the size of the box: low tolerance creates a
	 * big box so the viewport will start moving quickly. High tolerance makes a
	 * small box, so you can move the player close to the edge of the viewport
	 * without moving the viewport.
	 */
	final void setViewportLimits()
	{
		if (player == null)
		{
			return;
		}
		double dist = (1 - vtolerance)
				* (viewportHeight - player.getFrameHeight());
		if (vpositioning == PLAYER_TOP)
		{
			topLimit = 0;
			bottomLimit = player.getFrameHeight() + (int) dist - viewportHeight;
		} else if (vpositioning == PLAYER_CENTER)
		{
			topLimit = -(int) (dist / 2);
			bottomLimit = player.getFrameHeight() + (int) (dist / 2)
					- viewportHeight;
		} else if (vpositioning == PLAYER_BOTTOM)
		{
			topLimit = -(int) dist;
			bottomLimit = player.getFrameHeight() - viewportHeight;
		}
		dist = (1 - htolerance) * (viewportWidth - player.getFrameWidth());
		if (hpositioning == PLAYER_LEFT)
		{
			leftLimit = 0;
			rightLimit = player.getFrameWidth() + (int) dist - viewportWidth;
		} else if (hpositioning == PLAYER_CENTER)
		{
			leftLimit = -(int) (dist / 2);
			rightLimit = player.getFrameWidth() + (int) (dist / 2)
					- viewportWidth;
		} else if (hpositioning == PLAYER_RIGHT)
		{
			leftLimit = -(int) dist;
			rightLimit = player.getFrameWidth() - viewportWidth;
		}
	}

	/**
	 * This method sets the viewport for the first time. This is needed to give
	 * the viewportX and -Y a starting value
	 */
	void updateViewportFirstTime()
	{
		if (!useViewport)
		{
			return;
		}

		if (vpositioning == PLAYER_TOP)
		{
			viewportY = player.getY();
		} else if (vpositioning == PLAYER_CENTER)
		{
			viewportY = player.getY()
					- (viewportHeight - (int) (player.getFrameHeight() * zoomFactor))
					/ 2;
		} else if (vpositioning == PLAYER_BOTTOM)
		{
			viewportY = player.getY()
					- (viewportHeight - (int) (player.getFrameHeight() * zoomFactor));
		}

		if (hpositioning == PLAYER_LEFT)
		{
			viewportX = player.getX();
		} else if (hpositioning == PLAYER_CENTER)
		{
			viewportX = player.getX()
					- (viewportWidth - (int) (player.getFrameWidth() * zoomFactor))
					/ 2;
		} else if (hpositioning == PLAYER_RIGHT)
		{
			viewportX = player.getX()
					- (viewportWidth - (int) (player.getFrameWidth() * zoomFactor));
		}
		// playerX = player.getX();
		// playerY = player.getY();
	}

	/**
	 * Checks if the player has moved. If the player has moved then the viewport
	 * will update to the new location of the player.
	 */
	private void checkMovement()
	{
		if (playerX != player.getX() || playerY != player.getY())
		{
			playerX = player.getX();
			playerY = player.getY();
		}
	}

	/**
	 * Update the viewport so the player object will be in the desired position
	 */
	void update()
	{
		checkMovement();
		if (vpositioning != 0)
		{
			viewportY = Math.max(Math.min(viewportY, player.getY() + topLimit),
					player.getY() + bottomLimit);
		}

		if (hpositioning != 0)
		{
			viewportX = Math.max(
					Math.min(viewportX, player.getX() + leftLimit),
					player.getX() + rightLimit);
		}

		viewportX = Math.max(minX, Math.min(viewportX, maxX - viewportWidth));
		viewportY = Math.max(minY, Math.min(viewportY, maxY - viewportHeight));

	}

	/**
	 * Sets the object that will be the player character
	 * 
	 * @param player
	 *            The object that will act as a player
	 */
	void setPlayer(MoveableGameObject player)
	{
		this.player = player;
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
	void setBounds(int minX, int minY, int maxX, int maxY)
	{
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	/**
	 * Sets the position of the player on the screen. In many games, the game
	 * world is bigger than the screen. When you specify the player position,
	 * you can make the screen (viewport) move along with the player. The
	 * parameters tell how this must be done. <br />
	 * For the vpositioning (vertical adjustment) you can use
	 * Viewport.PLAYER_TOP, Viewport.PLAYER_CENTER, Viewport.PLAYER_BOTTOM or
	 * Viewport.PLAYER_NOADJUST, if you don't want adjustment of the player
	 * position in the vertical direction.<br />
	 * For the hpositioning (horizontal adjustment) you can use
	 * Viewport.PLAYER_LEFT, Viewport.PLAYER_CENTER, Viewport.PLAYER_RIGHT or
	 * Viewport.PLAYER_NOADJUST, if you don't want adjustment of the player
	 * position in the horizontal direction.<br />
	 * Note: if you position the player at one of the edges of the screen, like
	 * BOTTOM, this means that you can not move out of the screen that way, but
	 * you can move up a bit. How much that is, is specified by
	 * setPlayerPositionTolerance.
	 * 
	 * @param hpositioning
	 *            the horizontal postioning, or zero if there is no horizontal
	 *            adjustment of the viewport
	 * @param vpositioning
	 *            the vertical postioning, or zero if there is no vertical
	 *            adjustment of the viewport
	 */
	final void setPlayerPositionOnScreen(int hpositioning, int vpositioning)
	{
		this.hpositioning = hpositioning;
		this.vpositioning = vpositioning;
		if (isInitialized)
		{
			setViewportLimits();
		}
	}

	/**
	 * Set the tolerance of the positioning. When tolerance is zero, the
	 * viewport moves immediately when the player moves. When tolerance is 1,
	 * you can move to the edge of the screen before the viewport moves. Values
	 * in between result in a smaller or bigger delay before the viewport moves. <br/>
	 * Example: In a left-to right platform game, you may position the player at
	 * LEFT, CENTER. If you set the horizontal tolerance at 0.3, you may move to
	 * the right 30% of the screen before the viewport moves along. If you set
	 * vertical tolerance at 0.8, you can move 80% of the way up, before the
	 * viewport moves up also.
	 * 
	 * @param ht
	 *            horizontal tolerance, a value between 0 and 1
	 * @param vt
	 *            vertical tolerance, a value between 0 and 1
	 */
	final void setPlayerPositionTolerance(double ht, double vt)
	{
		htolerance = ht;
		vtolerance = vt;
		if (isInitialized)
		{
			setViewportLimits();
		}
	}

	/**
	 * Sets the zoomfactor for the viewport. A higher zoomfactor will zoom the
	 * viewport in further.
	 * 
	 * @param zoomFactor
	 *            the zoomfactor for the viewport.
	 */
	void setZoomFactor(int viewWidth, int viewHeight, float zoomFactor)
	{
		this.zoomFactor = zoomFactor;
		if (isInitialized)
		{
			viewportWidth = Math.round(viewWidth / zoomFactor);
			viewportHeight = Math.round(viewHeight / zoomFactor);
			setViewportLimits();
			// ToDo: make nice transition by keeping player in the same place
		}
	}

	/**
     * 
     */
	final void intialize(int viewWidth, int viewHeight)
	{
		viewportWidth = Math.round(viewWidth / zoomFactor);
		viewportHeight = Math.round(viewHeight / zoomFactor);
		setViewportLimits();
		updateViewportFirstTime();
	}

	/**
	 * Check if the given gameitem lies within the viewport
	 * 
	 * @param item
	 *            the gameitem that needs to be checked
	 * @return true, if it lies in the viewport, false otherwise
	 */
	public final boolean isInViewport(GameObject item)
	{
		return item.getX() + item.getFrameWidth() > viewportX
				&& item.getY() + item.getFrameHeight() > viewportY
				&& item.getX() < viewportX + viewportWidth
				&& item.getY() < viewportY + viewportHeight;
	}

	/**
	 * Return rectangle for the (background)image. The Image is scaled to this
	 * rec, visible part is cut out
	 * 
	 * @return target rectangle to scale the image, from which the visible part
	 *         is cut
	 */
	final Rect getDrawMask()
	{
		return new Rect(minX - Math.round(viewportX * zoomFactor), minY
				- Math.round(viewportY * zoomFactor),
				Math.round((maxX - viewportX) * zoomFactor),
				Math.round((maxY - viewportY) * zoomFactor));
	}

	/**
	 * Get translation for the x-coordinates (shift in drawing: Canvas always
	 * draws from coordinate 0, so you shift position the other way)
	 * 
	 * @return int x-translation.
	 */
	int getTranslateX()
	{
		return minX - viewportX;
	}

	/**
	 * Get translation for the y-coordinates (shift in drawing: Canvas always
	 * draws from coordinate 0, so you shift position the other way)
	 * 
	 * @return int y-translation.
	 */
	int getTranslateY()
	{
		return minY - viewportY;
	}

	/**
	 * Gets the x-position of the viewport.
	 * 
	 * @return x position of the viewport
	 */
	public int getViewportX()
	{
		return viewportX;
	}

	/**
	 * Gets the y-position of the viewport.
	 * 
	 * @return y position of the viewport
	 */
	public int getViewportY()
	{
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
	 * Translates a give screen position to a position in the game world, taking
	 * into consideration the viewport location and the zoom factor.
	 * 
	 * @param x
	 *            the screen x
	 * @param y
	 *            the screen y
	 * @return Point, containing the x,y-position in the game world
	 */
	Point translateToGamePosition(float x, float y)
	{
		return new Point(viewportX + (int) (x / zoomFactor), viewportY
				+ (int) (y / zoomFactor));
	}

	/**
	 * Gets the zoomfactor for zooming in.
	 * 
	 * @return the value of the zoomfactor
	 */
	public float getZoomFactor()
	{
		return zoomFactor;
	}

}