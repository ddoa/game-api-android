package android.gameengine.icadroids.engine;

import android.util.Log;

/**
 * Logs the FPS of the Game, to use the FPS logger,
 * set USE_FPS_COUNTER to 'true'.
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