package android.gameengine.icadroids.input;

import java.util.ArrayList;

import android.GameAPI.ICA_DROID.R;
import android.app.Activity;
import android.content.Context;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

/**
 * This class provides statics and a few function to easily make use of the
 * touch functionality of an android device. This class manages and draws
 * virtual buttons to the screen to detect for input. When a button is enabled (by default)
 * the user can check wether a button is pressed or not, by accessing the static
 * boolean values for each correspodending button.
 * 
 * Do not make an instance of this class yourself. If you want to use the OnScreenButtons
 * in your game, set the static variable <i>use</i> to true.
 * @author Roel
 */
public class OnScreenButtons{

	/** var needed to calculate the screenBlock */
	private int[] screenBlock = new int[2];

	/** the paint required for the opacity **/
	private Paint alphaPaint;

	/**
	 * sprites
	 */
	private Sprite startSprite, selectSprite, dpadSprite, button1Sprite,
			button2Sprite, button3Sprite, button4Sprite, shoulder1Sprite,
			shoulder2Sprite;

	/** lists for sprites and for the buttons */
	private ArrayList<Sprite> drawList = new ArrayList<Sprite>();

	private RectF dPadHitRect;

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
	/** This static var is TRUE when input has been detected on button 4. default sprite is button D. */ buttonY, 
	/** This static var is TRUE when input has been detected on start. */ start, 
	/** This static var is TRUE when input has been detected on select. */ select, 
	/** This static var is TRUE when input has been detected on the right shoulder button */ shoulderR, 
	/** This static var is TRUE when input has been detected on the left shoulder button */ shoulderL;

	/**
	 * wether or not to disable these buttons.
	 */
	public static boolean 
	/** setting this on TRUE will disable button 1. by default the A button! */ disableButton1, 
	/** setting this on TRUE will disable button 2. by default the B button! */ disableButton2, 
	/** setting this on TRUE will disable button 3. by default the C button! */ disableButton3,
	/** setting this on TRUE will disable button 4. by default the D button! */	disableButton4;

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

	/**
	 * configures all the onScreenButtons
	 * you can remove certain configure functions to disable buttons.
	 */
	private void buttonConfig() {

/*		confDpad();
		confButton1();
		confButton2();
		confButton3();
		confButton4();
		confSelect();
		confStart();
		confShoulders();*/

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
		}
	}


	
}