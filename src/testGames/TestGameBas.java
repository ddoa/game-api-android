package testGames;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.forms.GameForm;
import android.gameengine.icadroids.forms.IFormInput;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.view.View;
import android.widget.Button;

/**
 * Deze test test GameObject, MoveableGameObject, Forms, Alarms, AnimatedSprite
 * en OnscreenButtons.
 * 
 * @author Bas van der Zandt
 * 
 */
public class TestGameBas extends GameEngine implements IAlarm, IFormInput {

	private MoveableGameObject testObject = new MoveableGameObject();
	private GameObject testObject2 = new GameObject();
	GameForm gf;
	Alarm am = new Alarm(1, 300, this);
	testGameObjectBas testObjectBas = new testGameObjectBas();

	public TestGameBas() {

		testObjectBas.setSprite("fishframes");

		testObjectBas.startAnimate(36);
		testObjectBas.setAnimationSpeed(1);
		testObjectBas.setSpeed(0);
		testObject.setSprite("tile6");
		testObject.startAnimate(10);

		addGameObject(testObject, 10, 10);
		OnScreenButtons.use = true;
		OnScreenButtons.feedback = true;
		setScreenLandscape(true);
	}

	@Override
	public void initialize() {
		super.initialize();

		startGame();

	}

	@Override
	public void update() {

		// Log.d("update", "een update");
		if (OnScreenButtons.dPadUp) {
			testObject.setSpeed(5);
			testObject.setDirection(0);
			testObject.setFriction(0.1);
		}
		if (OnScreenButtons.dPadDown) {
			testObject.setDirectionSpeed(180, 5);
			testObject.setFriction(0.1);
		}
		if (OnScreenButtons.dPadRight) {
			testObject.setDirectionSpeed(90, 5);
			testObject.setFriction(0.2);
		}
		if (OnScreenButtons.dPadLeft) {
			testObject.setDirectionSpeed(270, 5);
			testObject.setFriction(0.2);
		}
		if (OnScreenButtons.button1) {
			testObject.jumpToStartPosition();
		}

	}

	public void triggerAlarm(int alarmID) {
		gf = new GameForm("testform", this, this);

	}

	public void buttonClicked(Button button) {

	}

	public void formElementClicked(View touchedElement) {
		System.out.println("geklikt!");
		// gf.sendToast("geklikt!", 1);

		if (gf.getElementName(touchedElement).equals("Pointless")) {
			System.out.println("Pointless");
			gf.sendToast(gf.getTextFromTextfield("tekstVeldje"), 3);
		}
		if (gf.getElementName(touchedElement).equals("hoi")) {
			gf.sendToast("Hallo", 5);
		}

	}

}
