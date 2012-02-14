package testGames.gameEngineTest;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;
import android.gameengine.icadroids.objects.MoveableGameObject;

/**
 * Dit GameObject test Animated sprites en Alarms.
 * 
 * @author Bas van der Zandt
 * 
 */
public class randomObject extends MoveableGameObject implements IAlarm {
	Alarm am = new Alarm(1, 300, this);
	Alarm am2 = new Alarm(2, 60, this);
	int timesAlarmTriggerd = 0;

	public randomObject() {
		setSprite("fishframes");
		getSprite().nextFrame();
		startAnimate(36);
		setAnimationSpeed(10);
	}

	public void triggerAlarm(int alarmID) {
		if (alarmID == 1) {
			deleteThisGameObject();
		}
		if (alarmID == 2) {

			timesAlarmTriggerd++;
			// System.out.println("Times alarm triggerd: " +
			// timesAlarmTriggerd);
			am2.restartAlarm();
		}
	}
}
