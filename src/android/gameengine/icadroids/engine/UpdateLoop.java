package android.gameengine.icadroids.engine;

import android.util.Log;

/**
 * <b> EXPERIMENTAL! </b>, runs the gamelogic in a seperate thread. May runs
 * the game faster on newer (dualcore) android devices.
 * 
 * @author Edward & Bas
 * 
 */
public class UpdateLoop extends GameLoop implements Runnable {


	private boolean running = true;
	GameEngine gameEngine;

	private GameFPSCounter fps = new GameFPSCounter();

	public UpdateLoop(GameEngine ge) {
		super(ge);
		Log.d("UpdatLoop", "Initialising...");
		gameEngine = ge;	
	}
	/***
	 * Main game-loop thread that handles gamelogic and rendering. 
	 */
	public void run() {
		long ticksPS = 1000 / MAX_FPS;
		long startTime;
		long sleepTime;
		while (running) 
		{			
			fps.logFrame("update");
			startTime = getCurrentSystemTime();
			updateGame();
			sleepTime = ticksPS - (getCurrentSystemTime() - startTime);
			try {
			if (sleepTime > 0) {
				Thread.sleep(sleepTime);
			}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Allows the game to run logic such as updating the world, checking for
	 * collisions, gathering input, and playing audio.
	 */
	protected void updateGame() {
		gameEngine.updateGame();		
	}

}
