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
	public static boolean feedback = true;
	/** opacity: sets the opacity of all the buttons in a range from 0 to 255 */
	public static int opacity = 100;


	/**
	 * holds the values of wether or not the button has been pressed. Use these
	 * to check for input.
	 */
	public static boolean 
	/** This static var is TRUE when input has been detected on the up key of the dpad. */ dPadUp, 
	/** This static var is TRUE when input has been detected on the down key of the dpad. */ dPadDown, 
	/** This static var is TRUE when input has been detected on the left key of the dpad. */ dPadLeft, 
	/** This static var is TRUE when input has been detected on the right key of the dpad. */ dPadRight, 
	/** This static var is TRUE when input has been detected on button A.  */ buttonA,
	/** This static var is TRUE when input has been detected on button B.  */ buttonB, 
	/** This static var is TRUE when input has been detected on button X.  */ buttonX, 
	/** This static var is TRUE when input has been detected on button Y. */ buttonY,
	/** This static var is TRUE when input has been detected on button 'start'. */ start,
	/** This static var is TRUE when input has been detected on button 'select'. */ select;


	private static View onScreenButtonsView;

	/**
	 * Do not call the constructor yourself this is for the GameEngine.
	 * 
	 * The constructor will load the onScreenButtons.xml view and places it on
	 * top of the canvas.
	 * 
	 * @param gameEngine
	 *            : The context is required by this class so it can receive the
	 *            screen width and height.
	 */
	public OnScreenButtons(Activity gameEngine) {

		onScreenButtonsView = gameEngine.getLayoutInflater().inflate(
				R.layout.onscreenbuttons, null);

		if (onScreenButtonsView != null) {
			LayoutParams lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);

			gameEngine.addContentView(onScreenButtonsView, lp);
		} else {
			Log.wtf("onScreenButtons", "Cannot find onscreenbutton layout!");
		}

		GameEngine.getAppView().setHapticFeedbackEnabled(true);
	}

	/**
	 * Method that will be called by the OnScreenButton to notify the
	 * onscreenbuttons an button has been pressed
	 * 
	 * @param buttonId
	 *            The layout id of the button
	 */
	protected static void buttonPressed(int buttonId) {
		setButtonState(buttonId, true);
	}

	/**
	 * Method that will be called by the OnScreenButton to notify the
	 * onscreenbuttons an button has been pressed
	 * 
	 * @param buttonId
	 */
	protected static void buttonReleased(int buttonId) {
		setButtonState(buttonId, false);
	}

	/**
	 * Set the correct static boolean state for the button.
	 * 
	 * @param buttonId
	 *            The id of the button that has been pressed
	 * @param state
	 *            The state to set it to
	 */
	private static void setButtonState(int buttonId, boolean state) {
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
		case R.id.buttonSelect:
			select = state;
			break;
		case R.id.buttonStart:
			start = state;
			break;

		}
	}

	/**
	 * Disable a onscreenbutton
	 * 
	 * @param buttonId
	 *            The id of the button that needs to be dissabled. you can get
	 *            the button id by using R.id.(button name). <br />
	 *            <b> For example: </b> disableButton(R.id.dpadCenter); <br />
	 *            You also have:
	 *            <p />
	 *            <i> buttonA, buttonB, buttonX, buttonY, dpadUp, dpadDown,
	 *            dpadLeft, dpadRight, buttonSelect and buttonStart </i>
	 * 
	 * 
	 */
	public static void disableButton(int buttonId) {
		OnScreenButton button = (OnScreenButton) onScreenButtonsView
				.findViewById(buttonId);

		if (button != null) {
			button.setVisibility(View.INVISIBLE);
		} else {
			Log.wtf("OnscreenButtons",
					"Cannot find or disable the button with id: " + buttonId);
		}
	}

}