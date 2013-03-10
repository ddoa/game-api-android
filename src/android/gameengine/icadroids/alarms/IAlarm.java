package android.gameengine.icadroids.alarms;

/**
 * Interface for receiving alarm events through the method triggerAlarm(id)
 * Any class that wants to set alarms will need to implement this interface
 * to receive the 'wake up call'. 
 * 
 * @author Bas van der Zandt
 * 
 */
public interface IAlarm {
	/**
	 * This method will be triggered when the alarm goes off.
	 * 
	 * @param alarmID
	 *            The given ID of the alarm
	 */
	void triggerAlarm(int alarmID);
}
