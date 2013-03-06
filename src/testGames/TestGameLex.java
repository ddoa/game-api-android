package testGames;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.renderer.GameView;
import android.gameengine.icadroids.renderer.Viewport;
import android.gameengine.icadroids.sound.GameSound;
import android.gameengine.icadroids.sound.MusicPlayer;

/**
 * Deze game test de sounds en viewport.
 * 
 * @author Lex van de Laak en Leon van Kleef
 * 
 */
public class TestGameLex extends GameEngine {

	private MoveableGameObject testObj = new MoveableGameObject();
	private GameObject testObject2 = new GameObject();
	Viewport view = Viewport.getInstance();

	public TestGameLex() {

		addPlayer(testObj, 100, 100, .9f);
		testObj.setSprite("kat_01");
		addGameObject(testObject2, 200, 200, 0.2f);
		testObject2.setSprite("kat_01");
	}

	@Override
	public void initialize() {
		super.initialize();
		OnScreenButtons.use = true;
		OnScreenButtons.feedback = true;
		setBackground("kat_01");
		setZoomFactor(.2f);
		GameView.MAP_HEIGHT = 3000;
		GameView.MAP_WIDTH = 4000;
		GameSound.addSound(0, "lucas");
		GameSound.addSound(1, "ding");
	}

	@Override
	public void update() {

		if (OnScreenButtons.dPadUp) {
			testObj.setSpeed(0);
			testObj.movePlayer(0, -5);
		}
		if (OnScreenButtons.dPadDown) {
			testObj.setSpeed(0);
			testObj.movePlayer(0, 5);
		}
		if (OnScreenButtons.dPadRight) {
			testObj.movePlayer(5, 0);
		}
		if (OnScreenButtons.dPadLeft) {
			testObj.movePlayer(-5, 0);
		}
		if (OnScreenButtons.select) {
			// GameSound.stopSound(1);
			// GameSound.pauseSound(0);
			// MusicPlayer.play("landing");
			GameSound.playSound(0, 0);
		}

		if (OnScreenButtons.start) {
			// GameSound.playSound(0, 0);
			MusicPlayer.stop();
			GameSound.stopSound(1);
		}

		if (OnScreenButtons.button1) {
			// GameSound.resumeSounds();
			GameSound.playSound(1, 5);
			MusicPlayer.play("lucas", true);
		}

		if (OnScreenButtons.button2) {
			GameSound.pauseSounds();
			setZoomFactor(.5f);
		}

		if (OnScreenButtons.button3) {
			GameSound.resumeSounds();
			setZoomFactor(1f);
		}

		if (OnScreenButtons.button4) {
			GameSound.stopSounds();
			view.setPlayerPositionTolerance(.5f, .5f);
		}
	}
}
