package testGames.gameEngineTest;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.engine.GameFPSCounter;
import android.gameengine.icadroids.engine.GameTiles;
import android.gameengine.icadroids.forms.GameForm;
import android.gameengine.icadroids.forms.IFormInput;
import android.gameengine.icadroids.input.TouchInput;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.sound.MusicPlayer;
import android.graphics.RectF;
import android.view.View;
import android.widget.Button;

/**
 * An engine that extends GameEngine that displays a debug menu for debugging
 * objects, alarms, sounds, tiles, life cycle and more.
 * 
 * @author Bas van der Zandt
 * 
 */
public abstract class DebugEngine extends GameEngine implements IFormInput,
		IAlarm {

	public debugObject DO = new debugObject();
	Alarm alm = new Alarm(12345, 10, this);
	GameForm gf;
	boolean paused = false;
	boolean useGameObjectDebug = false;
	RectF clickedPos = new RectF(0, 0, 0, 0);

	public DebugEngine() {
		addGameObject(DO, 0, 0);
	}

	public void formElementClicked(View touchedElement) {
		if (gf.getElementName(touchedElement).equals("opendebugmenu")) {
			gf.showView("debuglayout", this);
			gf.removeView("debugmenuopener");
		}

		if (gf.getElementName(touchedElement).equals("pauseButton")) {
			Button bt = (Button) touchedElement;
			if (!paused) {
				pause();
				bt.setText("Resume");
				paused = true;
			} else {
				resume();
				bt.setText("Pause");
				paused = false;
			}
		}
		if (gf.getElementName(touchedElement).equals("tilesButton")) {
			GameTiles.debugMode = !GameTiles.debugMode;
		}
		if (gf.getElementName(touchedElement).equals("soundButton")) {
			MusicPlayer.play("lucas", false);
		}
		if (gf.getElementName(touchedElement).equals("FPSButton")) {
			GameFPSCounter.USE_FPS_COUNTER = !GameFPSCounter.USE_FPS_COUNTER;
		}
		if (gf.getElementName(touchedElement).equals("objectsButton")) {
			DO.renderGameObjects = !DO.renderGameObjects;
		}
		if (gf.getElementName(touchedElement).equals("timersButton")) {
			DO.renderTimers = !DO.renderTimers;
		}
		if (gf.getElementName(touchedElement).equals("backButton")) {
			gf.removeView("debuglayout");
			gf.showView("debugmenuopener", this);
		}
		if (gf.getElementName(touchedElement).equals("buttonDebugGameObject")) {
			useGameObjectDebug = !useGameObjectDebug;
			Button btn = (Button) touchedElement;
			if (useGameObjectDebug) {
				btn.setText("Debug clicked GameObject is on");
			} else {
				btn.setText("Debug clicked GameObject is off");
			}
		}

	}

	@Override
	public void update() {
		super.update();
		if (useGameObjectDebug) {
			if (TouchInput.onPress) {
				clickedPos.set(TouchInput.xPos - 15, TouchInput.yPos - 15,
						TouchInput.xPos + 15, TouchInput.xPos + 15);
				if (!findItemAt(clickedPos).isEmpty()) {
					GameObject GOjbect = findItemAt(clickedPos).get(0);
					DO.gob = (MoveableGameObject) GOjbect;
					DO.renderObjectInfo = true;
				}
			}
		} else {
			DO.gob = null;
			DO.renderObjectInfo = false;
		}
	}

	public void triggerAlarm(int alarmID) {
		if (alarmID == 12345) {
			gf = new GameForm("debugmenuopener", this, this);
		}

	}

}
