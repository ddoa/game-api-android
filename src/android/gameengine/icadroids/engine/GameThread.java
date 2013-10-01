package android.gameengine.icadroids.engine;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;

/**
 * GameLoop is the Thread that handles the timing of the GameLogic and the
 * drawing.<br />
 * Generally, game programmers will not make direct use of this class. It
 * handles the internal workings of the GameEngine.
 * 
 * @author Edward & Bas
 * 
 */
public class GameThread extends Thread
{

	/**
	 * The view onto which the game is rendered.
	 */
	private GameView view;
	/**
	 * The currently running GameEngine.
	 */
	private GameEngine gameEngine;
	/**
	 * running is true when the gameloop is running, false otherwise
	 */
	private boolean running = false;
	/**
	 * Max FPS of this Game
	 */
	public static int MAX_FPS = 30;

	/**
	 * Registers the game FPS
	 */
	private GameFPSCounter fps = new GameFPSCounter();

	/**
	 * Canvas that needs to be drawn
	 */
	public Canvas c;

	/**
	 * Intialize the Gameloop
	 * 
	 * @param ge
	 *            GameEngine which the game is currently running on
	 */
	protected GameThread(GameEngine ge, GameView view)
	{
		super();
		setPriority(7);
		Log.d("GameLoop", "Initialising...");
		gameEngine = ge;
		this.view = view;
	}

	/**
	 * Stop the gameloop
	 */
	public final void stopRunning()
	{
		this.running = false;
	}

	/**
	 * Get the current system time in milliseconds
	 * 
	 * @return The current time in milliseconds
	 */
	public final long getCurrentSystemTime()
	{
		return System.currentTimeMillis();
	}

	/***
	 * Main game-loop thread that handles gamelogic and rendering.
	 */
	public void run()
	{
		long ticksPS = 1000 / MAX_FPS;
		long startTime;
		long sleepTime;
		running = true;
		while (running)
		{
			fps.logFrame("Render");
			startTime = getCurrentSystemTime();
			updateGame();
			updateEngine();
			sleepTime = ticksPS - (getCurrentSystemTime() - startTime);
			try
			{
				if (sleepTime > 0)
				{
					Thread.sleep(sleepTime);
				}
			} catch (InterruptedException e)
			{
				Log.wtf("GameLoop", "loop interupted", e);
			}
		}
	}

	/**
	 * Request the device to draw
	 */
	@SuppressLint("WrongCall")
	private void updateEngine()
	{
		try
		{
			startDraw();
			synchronized (view.getHolder())
			{
				view.onDraw(c);
			}
		} finally
		{
			if (c != null)
			{
				endDraw(c);
			}
		}
	}

	/**
	 * Start drawing on the canvas
	 */
	public final void startDraw()
	{
		c = view.getHolder().lockCanvas();
		if (c == null)
			Log.d("GameThread", "Canvas is null");
	}

	/**
	 * Stop Drawing
	 * 
	 * @param c
	 *            Canvas that needs to be stop drawing
	 */
	public final void endDraw(Canvas c)
	{
		view.getHolder().unlockCanvasAndPost(c);
	}

	/**
	 * Allows the game to run logic such as updating the world, checking for
	 * collisions, gathering input, and playing audio.
	 */
	protected void updateGame()
	{

		gameEngine.updateGame();

	}

	/**
	 * Get the running staat of the GameLoop Thread
	 * 
	 * @return True when running, false when stopped
	 */
	public final boolean isRunning()
	{
		return running;
	}

}
