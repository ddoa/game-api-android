package android.gameengine.icadroids.alarms;

import android.gameengine.icadroids.engine.GameEngine;

/**
 * Set alarms who will trigger after a specified time. 
 * Time is measured in cycles of the game loop.
 * Alarms will trigger a method (specified in IAlarm) after the given time
 * To make use of alarms, create a alarm object. This alarm will start immediately.
 * 
 * @author Bas van der Zandt
 * 
 */
public class Alarm {
	/**
	 * The ID of the alarm. You can give an alarm an unique ID. In this way you can
	 * distinguish between alarms if you have more than one.
	 */
	private int alarmID;
	
	/**
	 * Duration of the alarm, counted in cycles of the game loop.
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
	 * Create a new Alarm with the given time. The alarm will start immediately.
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
	 * The update does the counting, so <b>don't call this update yourself!</b>
	 *  
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
	 * Pause the alarm, it will stop ticking.
	 * You can restart the alarm with startAlarm().
	 */
	public final void pauseAlarm() {
		running = false;
	}

	/**
	 * Restart an alarm <b>without</b> resetting the clock.
	 * Use this method when an alarm has been paused.
	 */
	public final void startAlarm() {
		running = true;
	}

	/**
	 * Reset the clock to zero and start the alarm 
	 */
	public final void restartAlarm() {
		counter = 0;
		running = true;
	}

	/**
	 * Change the time of the alarm. This will not influence the clock,
	 * when an alarm is already ticking. If you set a time that the counter
	 * has already passed, the alarm will go off immediately.
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
