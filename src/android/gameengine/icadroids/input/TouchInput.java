/**
 * @author Roel
 */
package android.gameengine.icadroids.input;

import android.gameengine.icadroids.engine.GameView;
import android.graphics.Point;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * TouchInput class that sets and handles the input and put the result in
 * easy-to-use flags. Each flag can statically be accessed, they hold the
 * values.
 * 
 * Do not instantiate this class yourself, all you need are the static values.
 * @author Roel
 */
public class TouchInput implements OnTouchListener {

	/**
	 * Wether or not you want to use the touch input or not , set this flag to
	 * true if you want to use it. Note that only one touch Listener may be
	 * active at the time.
	 */
	public static boolean use;

	/**
	 * Change this value to increase the amount of fingers you can use. defaults
	 * to 10.
	 */
	public static int maxFingers = 10;

	/** TRUE when device receives input ; FALSE otherwise */
	public static boolean onPress;
	/**
	 * TRUE when device has no input/or loses input(ex: user removes his finger)
	 */
	public static boolean onRelease = true;
	/** The X position of the point where the user touches the screen.
	 * 
	 * This position may be different from the position in the game world 
	 * when using a viewport and zooming. Use GameEngine.translateToGamePosition to
	 * calculate the game position from the screen position.
	 * 
	 * @see android.gameengine.icadroids.engine.GameEngine#translateToGamePosition(float, float)
	 */
	public static float xPos = 0;
	/** The Y position of the point where the user touches the screen. 
	 * 
	 * This position may be different from the position in the game world 
	 * when using a viewport and zooming. Use GameEngine.translateToGamePosition to
	 * calculate the game position from the screen position.
	 * 
	 * @see android.gameengine.icadroids.engine.GameEngine#translateToGamePosition(float, float)
	 */
	public static float yPos = 0;

	// multi touch flags start from here.
	/** TRUE when space between two fingers increase. */
	public static boolean zoom;
	/** TRUE when space between two fingers decreases. */
	public static boolean pinch;
	/** gets the scaling factor that applies on zoom and pinch actions */
	public static float scale = 1f;
	/**
	 * Use this when you want to check the locations of multiple touches.
	 * xPointer[0] is the first finger, xPointer[1] is the second etc.
	 */
	public static float[] xPointer = new float[maxFingers];
	/** the Y value of multiple touches. */
	public static float[] yPointer = new float[maxFingers];
	/** the number of touches in this multiple touch event */
	public static int fingerCount;

	private float oldDistance = 0f;
	private float newDistance = 0f;
	private boolean zoomCheck;
	
	public TouchInput()
	{
		super();
	}
	
	/**
	 * DO NOT CALL THIS FUNCTION.<br />
	 * 
	 * This is the standard function android provides for touch events.
	 * That is, Android calls this function to inform the game about the status
	 * of the touch screen.<br />
	 * This class checks for the static values he need to change.
	 * 
	 * @param v
	 *            the View that android requires
	 * @param event
	 *            the event that this function catches.
	 */
	public boolean onTouch(View v, MotionEvent event) {

		// Checks wether the user allows the default touch input or not.
		if (use) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				// Set the right flags
				actionPress(event);
				break;
			case MotionEvent.ACTION_UP:
				// Set the right flags
				actionCancel(event);
				break;
			case MotionEvent.ACTION_CANCEL:
				// Set the right flags
				actionCancel(event);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				actionPress(event);
				oldDistance = spacing(event);
				if (oldDistance > 10f) {
					zoomCheck = true;
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				actionCancel(event);
				zoomCheck = false;
				break;
			case MotionEvent.ACTION_MOVE:
				setPositions(event);
				if (zoomCheck) {
					calculateScaling(event);
				}
				break;
			default:
				break;

			}

		}

		return true;
	}
	
	/** sequence of actions it should perform on a press event 
	 * @param event the incoming MotionEvent 
	 */
	private void actionPress(MotionEvent event){
		onPress = true;
		onRelease = false;
		setPositions(event);
	}
	
	/** sequence of actions it should perform on a canceling event 
	 * @param event the incoming MotionEvent 
	 */
	private void actionCancel(MotionEvent event)
	{
		onPress = false;
		onRelease = true;
		setPositions(event);
	}

	/**
	 * calculates wether the user peforms a zoom or a pinch
	 * 
	 * @param event
	 *            the event from OnTouch
	 */
	private void calculateScaling(MotionEvent event) {
		newDistance = spacing(event);
		if (oldDistance > newDistance) {
			pinch = true;
			zoom = false;
		} else if (oldDistance < newDistance) {
			pinch = false;
			zoom = true;
		}
		scale += (newDistance / oldDistance) - 1f;
		oldDistance = newDistance;
	}

	/**
	 * calculates the space between two fingers/pointers
	 * 
	 * @param event
	 *            the event to pass to this function
	 * 
	 * @return a float that specifies the amount of distance between two fingers
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * updates the positions of all the pointers/fingers.
	 * 
	 * @param e
	 *            the event to pass to this function.
	 */
	private void setPositions(MotionEvent e) {
		// Starting or end location of the touch event.
		fingerCount = 1;			// DEFAULT: set number of touches to 1 
										// (so the number of touches in earlier multi-touch events will be cleared)
		xPos = (int) e.getX();
		yPos = (int) e.getY();
		if (e.getPointerCount() > 1) {
			fingerCount = e.getPointerCount();
			for (int i = 0; i < e.getPointerCount(); i++) {
				xPointer[i] = e.getX(i);
				yPointer[i] = e.getY(i);
			}
		}
	}
}
