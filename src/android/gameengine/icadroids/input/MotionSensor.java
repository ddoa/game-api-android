package android.gameengine.icadroids.input;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Motion Sensor is used to receive certain statics about certain
 * motionEvent note that this only works on real phones and not in
 * emulator. Some of these functions will not work if you do not have
 * the required sensor in your phone.<br />
 * Use of MotionSensor in your game: use the static variables to read
 * the state of the sensors. All other methods are for communication
 * between game engine and android device.
 *         
 * @author Roel
 * See http://developer.android.com/reference/android/hardware/SensorEvent.html
 */
public class MotionSensor {

	
	
	/** Set this to TRUE if you want to use the MotionSensor and be able to ask for input. 
	 */
	public static boolean use;

	/** This variable contains the x Acceleration of the phone. */
	public static float xAcceleration;
	/** This variable contains the y Acceleration of the phone. */
	public static float yAcceleration;
	/** This variable contains the Z Acceleration of the phone , minus the gravity */
	public static float zAcceleration;

	/** the x rotation of the phone */
	public static float yaw;
	/** the y rotation of the phone */
	public static float pitch;
	/** the z rotation of the phone */
	public static float roll;

	/** this var is TRUE when the phone has been tilted Up*/
	public static boolean tiltUp;
	/** this var is TRUE when the phone has been tilted Down*/
	public static boolean tiltDown;
	/** this var is TRUE when the phone has been tilted Left*/
	public static boolean tiltLeft;
	/** this var is TRUE when the phone has been tilted Right*/
	public static boolean tiltRight;

	/** vars for calculation */
	// Raw accelerometer data.
	private static float[] mGData = new float[3];
	// Raw magnetic field data.
	private static float[] mMData = new float[3];
	// Rotation matrix.
	private static float[] mR = new float[16];
	// Inclination matrix.
	private static float[] mI = new float[16];
	// Processed orientation data.
	private static float[] orientation = new float[3];

	private static SensorManager sensorManager;
	
	/**
	 * Start the MotionSensor. This will be done by the GameEngine, game programmers
	 * do NOT have to this themselves!
	 * 
	 * @param newSensorManager
	 */
	public static void initialize(SensorManager newSensorManager) {
		sensorManager = newSensorManager;
	}
	
	
	/**
	 * <b>DO NOT CALL THIS METHOD.</b>
	 * <p>This method is called by the game engine to handle
	 * listening to sensor events.</p>
	 * @param listener
	 */
	public static void handleOnResume(SensorEventListener listener) {
		sensorManager.registerListener(listener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(listener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
	}
	/**
	 * <b>DO NOT CALL THIS METHOD.</b>
	 * <p>This method is called by the game engine to handle
	 * listening to sensor events.</p>
	 * @param listener
	 */
	public static void handleOnPause(SensorEventListener listener) {
		sensorManager.unregisterListener(listener);
	}
	
	/**
	 * <b>DO NOT CALL THIS METHOD.</b>
	 * <p>This method is called by the game engine to handle
	 * sensor events.</p>
	 * @param event
	 */
	public static void handleSensorChange(SensorEvent event) {
		if(use == false) {
			return;
		}
		//Log.d(this.getClass().toString(), "Received sensor event " + String.valueOf(Sensor))
		float data[] = new float[3];
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			data = mGData;
			setAxis(event.values);
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			data = mMData;
		} else {
			// function should end here if an other sensor activates this event.
			return;
		}

		for (int i = 0; i < 3; i++) {
			data[i] = event.values[i];
		}

		SensorManager.getRotationMatrix(mR, mI, mGData, mMData);
		SensorManager.getOrientation(mR, orientation);

		setRotation(orientation);

	}

	/** sets the axis */
	private static void setAxis(float[] f) {
		xAcceleration = f[0];
		yAcceleration = f[1];
		zAcceleration = f[2] - SensorManager.GRAVITY_EARTH;
	}

	/**
	 * sets the rotation vars and the handy flags
	 */
	private static void setRotation(float[] f) {
		yaw = (float) Math.toDegrees(f[0]);
		pitch = (float) Math.toDegrees(f[1]) + 180;
		roll = (float) Math.toDegrees(f[2]) + 90;
		setTilting();
	}

	/**
	 * Sets the tild variables to describe the current rotation the phone is in.
	 */
	private static void setTilting() {
		
		tiltUp = pitch > 180 && pitch < 270 ? true : false;
		tiltDown = pitch > 110 && pitch < 180 ? true : false;

		tiltLeft = roll > 0 && roll < 70 ? true : false;
		tiltRight = roll > 110 && roll < 180 ? true : false;
	}

}
