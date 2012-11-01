package android.gameengine.icadroids.alarms;

import android.gameengine.icadroids.engine.GameEngine;

/**
 * Set alarms who will trigger after a specified number of updates.
 * 
 * Alarms will trigger a method (specified in IAlarm) after the given time in
 * the specified object.
 * 
 * To make use of alarms, create a alarm object. This alarm will start immediately.
 * 
 * @author Bas van der Zandt
 * 
 */
public class Alarm {
	/**
	 * The ID of the alarm
	 */
	private int alarmID;
	/**
	 * How long the alarm will count
	 */
	private int time;
	/**
	 * Counts how many updates has been passed
	 */
	private int counter = 0;
	/**
	 * The object that needs to be triggered
	 */	
	IAlarm alarmedObject;
	/**
	 * True if the alarm is running, false otherwise
	 */
	private boolean running = false;

	/**
	 * Make a new Alarm with the given time. The alarm will start immediately.
	 * 
	 * @param id
	 *            ID to identify the alarm
	 * @param Time
	 *            How many updates must occur before the alarm goes off
	 * @param alarmedObject
	 *            The object that must be informed when the alarm goes off
	 */
	public Alarm(int id, int Time, IAlarm alarmedObject) {
		alarmID = id;
		this.time = Time;
		this.alarmedObject = alarmedObject;
		GameEngine.addAlarm(this);
	}

	/**
	 * Update will be triggered <b>by the GameEngine</b> every update.
	 *  <b> Don't call this update by yourself! </b>
	 *  
	 *  Update handles the alarm to be triggered an deleted.
	 */
	public final void update() {
		if (running) {
			if (counter >= time) {
				running = false;
				alarmedObject.triggerAlarm(alarmID);
			}
			counter++;
		}
	}

	/**
	 * Pause the alarm until you start it again with startAlarm()
	 */
	public final void pauseAlarm() {
		running = false;
	}

	/**
	 * Starts the alarm when it has paused
	 */
	public final void startAlarm() {
		running = true;
	}

	/**
	 * Restart the alarm
	 */
	public final void restartAlarm() {
		counter = 0;
		running = true;
	}

	/**
	 * Change the time of the alarm
	 * 
	 * @param time
	 *            The time
	 */
	public final void setTime(int time) {
		this.time = time;
	}
	
	/**
	 * Test if this alarm targets the specified Object (that must implement IAlarm)
	 * 
	 * @param ia
	 * 		The object you want want to test
	 * @return
	 * 		true if this Alarm is set for the specified object
	 */
	public boolean targets(IAlarm ia)
	{
		return (ia == alarmedObject );
	}
}
