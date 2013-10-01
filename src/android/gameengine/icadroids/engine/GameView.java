package android.gameengine.icadroids.engine;

import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Class that correctly renders the game on the screen. <br />
 * Game programmers will not be accessing this class directly, it is a utility
 * class used by the GameEngine. <br />
 * Note: javadoc comments not finished
 * 
 * @author Edward, Lex, Leon, Roel, Bas
 * 
 */
public class GameView extends SurfaceView implements Callback
{

	private SurfaceHolder holder;
	private Viewport viewport;
	private Matrix matrix = new Matrix();

	private GameEngine gameEngine;
	private Paint rectanglePaint = new Paint();
	private Sprite backgroundImage = new Sprite();

	private boolean backgroundFit = false;

	/**
	 * Surfaceloaded will be true when the surface has been loaded
	 */
	public static boolean surfaceLoaded = false;

	/**
	 * The background colour of the map
	 */
	public static int BACKGROUND_COLOR = Color.LTGRAY;

	/**
	 * Constructs a new view
	 * 
	 * @param ge
	 *            The Game Engine that this GameRenderer is part of.
	 * @param gameThread
	 *            The thread that this renderer will use.
	 */
	public GameView(GameEngine ge)
	{
		super(GameEngine.getAppContext());
		gameEngine = ge;
		holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
	}

	/**
	 * This method is called immediately before a surface is being destroyed, it
	 * Ensures that the thread stops when the gameloop is stopped.
	 */
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		GameEngine.printDebugInfo("GameView", "surface destroyed");
	}

	/**
	 * This method is called immediately after the surface is first created and
	 * it starts a new thread.
	 */
	public void surfaceCreated(SurfaceHolder holder)
	{
		GameEngine.printDebugInfo("GameView", "surface created");

		surfaceLoaded = true;

		rectanglePaint.setARGB(255, 0, 0, 0);
		rectanglePaint.setStrokeWidth(2);
		rectanglePaint.setStrokeMiter(5);
		rectanglePaint.setColor(Color.RED);
		rectanglePaint.setStyle(Style.STROKE);
		// initialize the game
		gameEngine.initializeGameEngine();

		if (Viewport.useViewport)
		{
			viewport = Viewport.getInstance();
			// this call is necessary, because user does not have to set the
			// world size (default)
			viewport.setBounds(0, 0, gameEngine.getMapWidth(),
					gameEngine.getMapHeight());
			viewport.intialize(getWidth(), getHeight());
			matrix.reset();
			matrix.postScale(viewport.getZoomFactor(), viewport.getZoomFactor());
		}
		gameEngine.startThread();
	}

	/**
	 * This method is called immediately after any structural changes (format or
	 * size) have been made to the surface.
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		GameEngine.printDebugInfo("GameView", "surface changed");
		// Empty method
	}

	public void drawDebugTiles(Canvas canvas, float left, float top,
			int tileSize)
	{
		rectanglePaint.setColor(Color.BLUE);
		canvas.drawRect(left, top, left + tileSize, top + tileSize,
				rectanglePaint);
	}

	/**
	 * Checks of the viewport is zoomed in. If the zoomfactor is not equal to
	 * 1.0 then canvas is allowed to zoom in.
	 * 
	 * @param canvas
	 */
	private void checkZoomed(Canvas canvas)
	{
		if (viewport.zoomFactor != 1.0)
		{
			canvas.setMatrix(matrix);
		}
	}

	/**
	 * Draws the background image of the game.
	 * 
	 * @param canvas
	 *            The canvas used to draw the background image
	 */
	private void drawBackground(Canvas canvas)
	{
		if (backgroundImage != null)
		{
			if (backgroundImage.getSprite() != null)
			{
				Rect mask;
				if (backgroundFit || !Viewport.useViewport)
				{
					mask = new Rect();
					mask.set(0, 0, getWidth(), getHeight()); // fit
				} else
				{
					viewport = Viewport.getInstance();
					mask = viewport.getDrawMask();
				}
				canvas.drawBitmap(backgroundImage.getSprite(), null, mask, null);
			}
		}
	}

	/**
	 * Sets the background image that will be used in the game.
	 * 
	 * @param backgroundString
	 *            The name of the image that will be used as background.
	 */
	public void setBackgroundImage(String backgroundString)
	{
		if (backgroundString == null)
		{
			backgroundImage = null;
		} else
		{
			backgroundImage.loadSprite(backgroundString);
		}
	}

	/**
	 * Sets the zoomfactor for the current viewport.
	 * 
	 * @param zoomFactor
	 *            The amount of zooming
	 */
	void setZoomFactor(float zoomFactor)
	{

		if (Viewport.useViewport)
		{
			viewport = Viewport.getInstance();

			viewport.setZoomFactor(getWidth(), getHeight(), zoomFactor);
			matrix.reset();
			matrix.postScale(zoomFactor, zoomFactor);
		}
	}

	/**
	 * This method allows android to draw.
	 * 
	 * @param canvas
	 *            The canvas used to draw.
	 */
	@Override
	public void onDraw(Canvas canvas)
	{
		canvas.drawColor(BACKGROUND_COLOR);
		drawBackground(canvas);
		if (Viewport.useViewport)
		{
			if (viewport == null)
			{
				viewport = Viewport.getInstance();
			}
			canvas.setMatrix(matrix);
			viewport.update();
			checkZoomed(canvas);
			canvas.translate(viewport.getTranslateX(), viewport.getTranslateY());
		}

		if (gameEngine.isTileBasedMap())
		{
			GameEngine.gameTiles.drawTiles(canvas);
		}

		// Paint items in reverse order of the list, so item added last will be
		// painted
		// first and thus be on the bottom of the object depth
		for (int i = GameEngine.items.size() - 1; i >= 0; i--)
		{
			GameObject item = GameEngine.items.get(i);
			if (Viewport.useViewport)
			{
				if (viewport.isInViewport(item))
				{
					item.drawGameObject(canvas);
					// View.isHardwareAccelerated();
				}
			} else
			{
				item.drawGameObject(canvas);
			}
		}
		canvas.setMatrix(null);

		for (GameObject item : GameEngine.items)
		{
			item.drawCustomObjects(canvas);
		}

	}

	public Point getViewportLocation()
	{
		if (Viewport.useViewport)
		{
			if (viewport != null)
			{
				return viewport.getViewportLocation();
			}
		}
		return new Point(0, 0);
	}

	public void setBackgroundFit(boolean backgroundFit)
	{
		this.backgroundFit = backgroundFit;
	}

}