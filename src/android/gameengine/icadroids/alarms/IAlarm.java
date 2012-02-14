package android.gameengine.icadroids.alarms;

/**
 * Interface that every class needs to implement for receiving alarms events and
 * correctly deleting alarms
 * 
 * @author Bas
 * 
 */
public interface IAlarm {
	/**
	 * Return false before the IAlarm object is destroyed for correct garbage
	 * collection of the alarms.
	 * 
	 * When the IAlarm object is destroyed, the alarms will still exists. This
	 * causes a problem because the alarms prevent the garbage collector to
	 * clear up the IAlarm.
	 * 
	 * When you want to delete a IAlarm, let this method return 'false' before
	 * you delete the IAlarm.
	 * 
	 * <b> NOTE: GameObjects will handle this by itself! If you really want your
	 * own implementation, override this method. </b>
	 * 
	 * @return False if the alarms can be deleted.
	 */
	public boolean alarmsActiveForThisObject();

	/**
	 * This method will be triggered when the alarm goes off.
	 * 
	 * @param alarmID
	 *            The given ID of the alarm
	 */
	void triggerAlarm(int alarmID);
}
