package android.gameengine.icadroids.engine;

/**
 * Utility to log the FPS (frames per second) of the Game.
 * To use the FPS logger, set USE_FPS_COUNTER to 'true' in the GameEngine.
 * 
 * @author Edward
 * 
 */
public class GameFPSCounter {
	long startTime = System.nanoTime();
	int frames = 0;
	public static boolean USE_FPS_COUNTER = false;

	public void logFrame(String name) {
		if (USE_FPS_COUNTER) {
			frames++;
			if (System.nanoTime() - startTime >= 1000000000) {
				//Log.d(name + " FPS:", "" + frames + " FPS!");
				frames = 0;
				startTime = System.nanoTime();
			}
		}
	}

}