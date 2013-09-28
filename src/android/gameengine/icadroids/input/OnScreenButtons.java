package android.gameengine.icadroids.input;

import android.GameAPI.ICA_DROID.R;
import android.app.Activity;
import android.gameengine.icadroids.engine.GameEngine;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * This class provides statics and a few function to easily make use of the
 * touch functionality of an android device. This class manages and draws
 * virtual buttons to the screen to detect for input. When a button is enabled (by default)
 * the user can check wether a button is pressed or not, by accessing the static
 * boolean values for each correspodending button.
 * 
 * Do not make an instance of this class yourself. If you want to use the OnScreenButtons
 * in your game, set the static variable <i>use</i> to true.
 * @author Roel & Bas
 */
public class OnScreenButtons{

	// general settings
	/** use : wether or not to actually use these buttons */
	public static boolean use;
	/** feedback: wether or not to allow feedback */
	public static boolean feedback;
	/** opacity: sets the opacity of all the buttons in a range from 0 to 255 */
	public static int opacity = 100;
	private int oldOpacity;


	/**
	 * holds the values of wether or not the button has been pressed. Use these
	 * to check for input.
	 */
	public static boolean 
	/** This static var is TRUE when input has been detected on the up key of the dpad. */ dPadUp, 
	/** This static var is TRUE when input has been detected on the down key of the dpad. */ dPadDown, 
	/** This static var is TRUE when input has been detected on the left key of the dpad. */ dPadLeft, 
	/** This static var is TRUE when input has been detected on the right key of the dpad. */ dPadRight, 
	/** This static var is TRUE when input has been detected on button 1. default sprite is button A. */ buttonA,
	/** This static var is TRUE when input has been detected on button 2. default sprite is button B. */ buttonB, 
	/** This static var is TRUE when input has been detected on button 3. default sprite is button C. */ buttonX, 
	/** This static var is TRUE when input has been detected on button 4. default sprite is button D. */ buttonY;

	/**
	 * Do not call the constructor yourself this is for the GameEngine
	 * 
	 * @param context
	 *            : The context is required by this class so it can receive the
	 *            screen width and height.
	 */
	public OnScreenButtons(Activity gameEngine) {
		
		View onScreenButtonsView = gameEngine.getLayoutInflater().inflate(R.layout.onscreenbuttons, null);	
		
		if(onScreenButtonsView != null){
			LayoutParams lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			
			
			gameEngine.addContentView(onScreenButtonsView, lp);
		} else {
			Log.wtf("onScreenButtons", "Cannot find onscreenbutton layout!");
		}
		
		GameEngine.getAppView().setHapticFeedbackEnabled(true);
	}

	
	protected static void buttonPressed(int buttonId){
		setButtonState(buttonId, true);
	}
	
	protected static void buttonReleased(int buttonId){
		setButtonState(buttonId, false);
	}
	
	private static void setButtonState(int buttonId, boolean state){
		switch (buttonId) {
		case R.id.buttonA:
			buttonA = state;
			break;
		case R.id.buttonB:
			buttonB = state;
			break;
		case R.id.buttonX:
			buttonX = state;
			break;
		case R.id.buttonY:
			buttonY = state;
			break;
		case R.id.dpadUp:
			dPadUp = state;
			break;
		case R.id.dpadDown:
			dPadDown = state;
			break;
		case R.id.dpadLeft:
			dPadLeft = state;
			break;
		case R.id.dpadRight:
			dPadRight = state;
			break;
			
		}
	}
}