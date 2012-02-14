package android.gameengine.icadroids.renderer;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Class that correctly renders the game on the screen.
 * 
 * @author Edward, Lex, Leon, Roel, Bas
 * 
 */
public class GameView extends SurfaceView implements Callback {

	private SurfaceHolder holder;
	private Viewport viewport;
	private Matrix matrix = new Matrix();

	private GameEngine gameEngine;
	private Paint rectanglePaint = new Paint();
	private Sprite backgroundImage = new Sprite();
	private Rect rect = new Rect();

	private boolean backgroundFit = false;
	private boolean tileBasedMap = false;
	private Thread gameThread;

	/**
	 * The height of the map
	 */
	public static int MAP_HEIGHT = 2000;

	/**
	 * The width of the map
	 */
	public static int MAP_WIDTH = 5000;

	/**
	 * The background colour of the map
	 */
	public static int BACKGROUND_COLOR = Color.LTGRAY;

	/**
	 * Constructs a new viewport
	 * 
	 * @param ge
	 *            The Game Engine that this GameRenderer is part of.
	 * @param gameThread
	 *            The thread that this renderer will use.
	 */
	public GameView(GameEngine ge, Thread gameThread) {
		super(GameEngine.getAppContext());
		this.gameThread = gameThread;
		gameEngine = ge;
		holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);	
	}

	/**
	 * This method is called immediately before a surface is being destroyed, it
	 * Ensures that the thread stops when the gameloop is stopped.
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("surface destroyed");
	}

	/**
	 * This method is called immediately after the surface is first created and
	 * it starts a new thread.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surface created");
		rectanglePaint.setARGB(255, 0, 0, 0);
		rectanglePaint.setStrokeWidth(2);
		rectanglePaint.setStrokeMiter(5);
		rectanglePaint.setColor(Color.RED);
		rectanglePaint.setStyle(Style.STROKE);
		if (Viewport.useViewport) {
			if(viewport == null)
			{
				viewport = Viewport.getInstance();
			}
			viewport.setPlayer(gameEngine.getPlayer());
			matrix.reset();
			matrix.postScale(viewport.getZoomFactor(), viewport.getZoomFactor());
		}
		if (gameThread.getState() == Thread.State.NEW) {
			Log.d("GameThread", "Thread started");
			gameThread.start();
		}
		if (Viewport.useViewport) {			
			viewport.screenHeight = Math
					.round((getHeight() / 
							viewport.zoomFactor));
			viewport.screenWidth = Math
					.round((getWidth() / viewport.zoomFactor));
			if (tileBasedMap) {
				viewport.setBounds(0, 0, gameEngine.getMapWidth(),
						gameEngine.getMapHeight());
			} else {
				viewport.setBounds(0, 0, MAP_WIDTH, MAP_HEIGHT);
			}
			viewport.setViewportLimits();
			viewport.updateViewportFirstTime();
		}
	}

	/**
	 * This method is called immediately after any structural changes (format or
	 * size) have been made to the surface.
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		System.out.println("surface changed");
		// Empty method
	}

	public void drawTile(Canvas canvas, Sprite sprite, int x, int y) {
		canvas.drawBitmap(sprite.getSprite(), x, y, null);
	}

	public void drawDebugTiles(Canvas canvas, float left, float top,
			int tileSize) {
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
	private void checkZoomed(Canvas canvas) {
		if (viewport.zoomFactor != 1.0) {
			canvas.setMatrix(matrix);
		}
	}

	/**
	 * Draws the background image of the game.
	 * 
	 * @param canvas
	 *            The canvas used to draw the background image
	 */
	private void drawBackground(Canvas canvas) {
		if (backgroundImage != null) {
			if (backgroundImage.getSprite() != null) {
				if (backgroundFit || !Viewport.useViewport) {
					rect.set(0, 0, getWidth(), getHeight()); // fit
				} else {
					if (viewport.zoomFactor >= 1.0) {
						int offsetX = this.getWidth() - viewport.screenWidth;
						int offsetY = this.getHeight() - viewport.screenHeight;
						rect.set(
								viewport.getMinX()
										- Math.round(viewport.getViewportX()
												* viewport.zoomFactor),
								viewport.getMinY()
										- Math.round(viewport.getViewportY()
												* viewport.zoomFactor),
								Math.round(viewport.getMaxX()
										* viewport.zoomFactor)
										- Math.round(viewport.getViewportX()
												* viewport.zoomFactor)
										+ offsetX,
								Math.round(viewport.getMaxY()
										* viewport.zoomFactor)
										- Math.round(viewport.getViewportY()
												* viewport.zoomFactor)
										+ offsetY); // dynamic
													// zoom-in
					} else {
						rect.set(
								viewport.getMinX()
										- Math.round(viewport.getViewportX()
												* viewport.zoomFactor),
								viewport.getMinY()
										- Math.round(viewport.getViewportY()
												* viewport.zoomFactor),
								Math.round(viewport.getMaxX()
										* viewport.zoomFactor)
										- Math.round(viewport.getViewportX()
												* viewport.zoomFactor),
								Math.round(viewport.getMaxY()
										* viewport.zoomFactor)
										- Math.round(viewport.getViewportY()
												* viewport.zoomFactor)); // dynamic
																			// zoom-out
					}
				}
				canvas.drawBitmap(backgroundImage.getSprite(), null, rect, null);
			}
		}
	}

	/**
	 * Sets the background image that will be used in the game.
	 * 
	 * @param backgroundString
	 *            The name of the image that will be used as background.
	 */
	public void setBackgroundImage(String backgroundString) {
		if (backgroundString == null) {
			backgroundImage = null;
		} else {
			backgroundImage.loadSprite(backgroundString);
		}
	}

	/**
	 * Sets the zoomfactor for the current viewport.
	 * 
	 * @param zoomFactor
	 *            The amount of zooming
	 */
	public void setZoomFactor(float zoomFactor) {
		if(viewport == null)
		{
			viewport = Viewport.getInstance();
			viewport.setPlayer(gameEngine.getPlayer());
		}
		viewport.setZoomFactor(zoomFactor);
		viewport.screenHeight = Math.round((getHeight() / zoomFactor));
		viewport.screenWidth = Math.round((getWidth() / zoomFactor));
		matrix.reset();
		matrix.postScale(zoomFactor, zoomFactor);
		viewport.setViewportLimits();
		viewport.updateViewportFirstTime();
	}

	/**
	 * Sets the boolean for the tileBasedMap
	 * 
	 * @param tileBasedMap
	 *            True if a tilebased map is used, otherwise false
	 */
	public void setTileBasedMap(boolean tileBasedMap) {
		this.tileBasedMap = tileBasedMap;
	}

	public void setGameThread(Thread gamethread) {
		this.gameThread = gamethread;
	}

	/**
	 * This method allows android to draw.
	 * 
	 * @param canvas
	 *            The canvas used to draw.
	 */
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(BACKGROUND_COLOR);
		drawBackground(canvas);
		if (Viewport.useViewport) {
			if(viewport == null)
			{
				viewport = Viewport.getInstance();
			}
			canvas.setMatrix(matrix);
			viewport.update();
		}
		for (int i = 0; i < GameEngine.items.size(); i++) {
			GameObject item = GameEngine.items.get(i);
			if (Viewport.useViewport) {
				if (viewport.isInViewport(item)) {
					canvas.save();
					checkZoomed(canvas);
					canvas.translate(
							viewport.getMinX() - viewport.getViewportX(),
							viewport.getMinY() - viewport.getViewportY());
					item.drawGameObject(canvas);
					// View.isHardwareAccelerated();
					canvas.restore();
				}
				canvas.setMatrix(null);
			} else {
				item.drawGameObject(canvas);
			}
		}
		if (Viewport.useViewport) {
			checkZoomed(canvas);
			canvas.translate(-viewport.getViewportX(), -viewport.getViewportY());
		}
		gameEngine.drawTiles();
		canvas.setMatrix(null);
		gameEngine.drawInterface(canvas);
	}
}