package testGames;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.util.Log;

/**
 * Deze game test het trillen van de telefoon en OnscreenButtons.
 * 
 * @author Roel van Bergen
 * 
 */
public class TestGameRoel extends GameEngine {

	private static final String TAG = "ButtonEnabled";
	private MoveableGameObject player;
	private final long[] pattern = { 200, 500, 200, 800, 200, 500, 200 };

	public TestGameRoel() {
		super();
		player = new MoveableGameObject();
		addPlayer(player, 0, 0);

	}

	@Override
	public void initialize() {
		super.initialize();

		// Must be set before calling start game
		OnScreenButtons.use = true;
		OnScreenButtons.feedback = true;
		OnScreenButtons.opacity = 255;
	}

	@Override
	public void update() {
		if (OnScreenButtons.dPadUp)
			Log.d(TAG, "DPADUP has been pressed");
		if (OnScreenButtons.dPadDown)
			Log.d(TAG, "DPADDOWN has been pressed");
		if (OnScreenButtons.dPadLeft)
			Log.d(TAG, "DPADLEFT has been pressed");
		if (OnScreenButtons.dPadRight)
			Log.d(TAG, "DPADRIGHT has been pressed");
		if (OnScreenButtons.start)
			vibrate(pattern);
		if (OnScreenButtons.select)
			Log.d(TAG, "SELECT has been pressed");
		if (OnScreenButtons.shoulderR)
			Log.d(TAG, "SHOULDER1 has been pressed");
		if (OnScreenButtons.shoulderL)
			Log.d(TAG, "SHOULDER2 has been pressed");
		if (OnScreenButtons.button1)
			Log.d(TAG, "BUTTON1 has been pressed");
		if (OnScreenButtons.button2)
			Log.d(TAG, "BUTTON2 has been pressed");
	}

}
