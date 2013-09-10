package testGames;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;
import android.gameengine.icadroids.objects.MoveableGameObject;

/**
 * Deze game test alarms en MoveableGameObject.
 * 
 * @author Bas van der Zandt
 * 
 */
public class testGameObjectBas extends MoveableGameObject implements IAlarm {

	Alarm am = new Alarm(1, 80, this);

	public testGameObjectBas() {
		setSprite("kat_01");
		setDirection(180);
		setSpeed(0);
	}

	@Override
	public void outsideWorld() {
		System.out.println("Hello outside world!");
		// reverseHorizontalDirection();
	}

	@Override
	public void update() {
		super.update();
		/*
		 * if(OnScreenButtons.D_PAD_UP){ movePlayer(0, -1); }
		 * if(OnScreenButtons.D_PAD_DOWN){ movePlayer(0, 1); }
		 */
	}

	public void triggerAlarm(int alarmID) {
		System.out.println("TRINGGGG!!!!");
		// deleteThisGameObject();
		am.restartAlarm();

	}

}
