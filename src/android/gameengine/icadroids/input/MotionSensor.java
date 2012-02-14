package android.gameengine.icadroids.input;

import android.content.Context;
import android.gameengine.icadroids.engine.GameEngine;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 *Motion Sensor is used to receive certain statics about certain
 *motionEvent note that this only works on real phones and not in
 *emulator. Some of these functions will not work if you do not have
 *the required sensor in your phone.
 *         
 *@author Roel
 *@see http://developer.android.com/reference/android/hardware/SensorEvent.html
 */
public class MotionSensor implements SensorEventListener {

	/** Set this to TRUE if you want to use the MotionSensor and be able to ask for input. 
	 * Note that this variable should be set to TRUE in the constructor of your game, and not in initialize.*/
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

	/** this holds the sensorManager */
	private SensorManager sensorManager;
	/** this holds the accelMeter */
	private Sensor accelMeter;
	/** this holds the magneticMeter */
	private Sensor magneticMeter;

	final float toDegr = (float) (180.0f / Math.PI);

	/** vars for calculation */
	private float[] mGData = new float[3];
	private float[] mMData = new float[3];
	private float[] mR = new float[16];
	private float[] mI = new float[16];
	private float[] orientation = new float[3];

	/**
	 * DO NOT CALL THIS CONSTRUCTOR YOURSELF!
	 * use the static variables to ask any information from the motion Sensor.
	 */
	public MotionSensor() {}
	
	/**
	 * initialize the sensors, this is called by the engine. Do not call this.
	 */
	public void initializeSensors()
	{
		sensorManager = (SensorManager) GameEngine.getAppContext()
				.getSystemService(Context.SENSOR_SERVICE);
		accelMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticMeter = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	/**
	 * default function that comes with the sensorEventListener interface.
	 * This function does nothing.
	 */
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	/**
	 * default function that comes with the sensorEventListener interface.
	 * this function analyses the output of the sensorEvent and translates this to the easy to use variables
	 * It all modifies the z axis to ignore the constant gravity the phone endures.
	 */
	public void onSensorChanged(SensorEvent event) {

		float data[];
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
	private void setAxis(float[] f) {
		xAcceleration = f[0];
		yAcceleration = f[1];
		zAcceleration = f[2] - SensorManager.GRAVITY_EARTH;
	}

	/**
	 * sets the rotation vars and the handy flags
	 */
	private void setRotation(float[] f) {
		yaw = f[0] * toDegr;
		pitch = f[1] * toDegr + 180;
		roll = f[2] * toDegr + 90;
		setTilting();
	}

	/**
	 * Sets the tild variables to describe the current rotation the phone is in.
	 */
	private void setTilting() {
		tiltUp = pitch > 0 && pitch < 70 ? true : false;
		tiltDown = pitch > 110 && pitch < 180 ? true : false;

		tiltLeft = roll > 0 && roll < 70 ? true : false;
		tiltRight = roll > 110 && roll < 180 ? true : false;
	}

	/** 
	 * Register this listener so it will listen for events from the device.
	 * This function is used by engine and should not be used by the student.
	 */
	public void registerListener() {
		sensorManager.registerListener(this, accelMeter,
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this, magneticMeter,
				SensorManager.SENSOR_DELAY_GAME);
	}

	/**
	 * Unregister the listener so the device should no longer check for the output of the sensors.
	 */
	public void unregisterListener() {
		sensorManager.unregisterListener(this);
	}

}
