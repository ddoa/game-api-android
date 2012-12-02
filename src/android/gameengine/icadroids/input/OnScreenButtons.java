package android.gameengine.icadroids.input;

import java.util.ArrayList;
import android.content.Context;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

/**
 * This class provides statics and a few function to easily make use of the
 * touch functionality of an android device. This class manages and draws
 * virtual buttons to the screen to detect for input. When a button is enabled (by default)
 * the user can check wether a button is pressed or not, by accessing the static
 * boolean values for each correspodending button.
 * 
 * Do not make an instance of this class yourself.
 * @author Roel
 */
public class OnScreenButtons implements OnTouchListener {

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
	private ArrayList<ButtonLocStruct> buttonList = new ArrayList<ButtonLocStruct>();

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
	 * required private vars for each button and its configuration.
	 */
	private ButtonLocStruct dPadStruct, button1Struct, button2Struct,
			button3Struct, button4Struct, startStruct, selectStruct,
			shoulder1Struct, shoulder2Struct;

	/**
	 * holds the values of wether or not the button has been pressed. Use these
	 * to check for input.
	 */
	public static boolean 
	/** This static var is TRUE when input has been detected on the up key of the dpad. */ dPadUp, 
	/** This static var is TRUE when input has been detected on the down key of the dpad. */ dPadDown, 
	/** This static var is TRUE when input has been detected on the left key of the dpad. */ dPadLeft, 
	/** This static var is TRUE when input has been detected on the right key of the dpad. */ dPadRight, 
	/** This static var is TRUE when input has been detected on button 1. default sprite is button A. */ button1,
	/** This static var is TRUE when input has been detected on button 2. default sprite is button B. */ button2, 
	/** This static var is TRUE when input has been detected on button 3. default sprite is button C. */ button3, 
	/** This static var is TRUE when input has been detected on button 4. default sprite is button D. */ button4, 
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
	public OnScreenButtons() {
		Display display;
		display = ((WindowManager) GameEngine.getAppContext()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenBlock[0] = display.getWidth() / 100;
		screenBlock[1] = display.getHeight() / 100;
		initSprites();
		buttonConfig();
		GameEngine.getAppView().setHapticFeedbackEnabled(true);
	}

	/**
	 * configures all the onScreenButtons
	 * you can remove certain configure functions to disable buttons.
	 */
	private void buttonConfig() {

		confDpad();
		confButton1();
		confButton2();
		confButton3();
		confButton4();
		confSelect();
		confStart();
		confShoulders();

	}

	/** configures the dpad */
	private void confDpad() {
		dPadStruct = new ButtonLocStruct(screenBlock[0] * 2,
				screenBlock[1] * 65, screenBlock[0] * 25, screenBlock[0] * 25);

		dPadHitRect = new RectF(0, screenBlock[1] * 55, screenBlock[0] * 30,
				screenBlock[1] * 119);
		drawList.add(dpadSprite);
		buttonList.add(dPadStruct);
	}

	/** configures the button1 */
	private void confButton1() {
		if (!disableButton1) {
			button1Struct = new ButtonLocStruct(screenBlock[0] * 72,
					screenBlock[1] * 80, screenBlock[0] * 10,
					screenBlock[0] * 10);
			drawList.add(button1Sprite);
			buttonList.add(button1Struct);
		}
	}

	/** configures the button2 */
	private void confButton2() {
		if (!disableButton2) {
			button2Struct = new ButtonLocStruct(screenBlock[0] * 85,
					screenBlock[1] * 80, screenBlock[0] * 10,
					screenBlock[0] * 10);
			drawList.add(button2Sprite);
			buttonList.add(button2Struct);
		}
	}

	/** configures the button3 */
	private void confButton3() {
		if (!disableButton3) {
			button3Struct = new ButtonLocStruct(screenBlock[0] * 72,
					screenBlock[1] * 55, screenBlock[0] * 10,
					screenBlock[0] * 10);
			drawList.add(button3Sprite);
			buttonList.add(button3Struct);
		}
	}

	/** configures the button4 */
	private void confButton4() {
		if (!disableButton4) {
			button4Struct = new ButtonLocStruct(screenBlock[0] * 85,
					screenBlock[1] * 55, screenBlock[0] * 10,
					screenBlock[0] * 10);
			drawList.add(button4Sprite);
			buttonList.add(button4Struct);
		}
	}

	/** configures the start button */
	private void confStart() {
		startStruct = new ButtonLocStruct(screenBlock[0] * 32,
				screenBlock[1] * 90, screenBlock[0] * 15, screenBlock[1] * 10);
		drawList.add(startSprite);
		buttonList.add(startStruct);
	}

	/** configures the select button */
	private void confSelect() {
		selectStruct = new ButtonLocStruct(screenBlock[0] * 50,
				screenBlock[1] * 90, screenBlock[0] * 15, screenBlock[1] * 10);
		drawList.add(selectSprite);
		buttonList.add(selectStruct);
	}

	/** configures both shoulder buttons */
	private void confShoulders() {
		shoulder1Struct = new ButtonLocStruct(0, 0, screenBlock[0] * 20,
				screenBlock[1] * 20);
		drawList.add(shoulder1Sprite);
		buttonList.add(shoulder1Struct);

		shoulder2Struct = new ButtonLocStruct(screenBlock[0] * 80, 0,
				screenBlock[0] * 20, screenBlock[1] * 20);
		drawList.add(shoulder2Sprite);
		buttonList.add(shoulder2Struct);
	}

	/**
	 * Performs a short shake that only works on actual devices.
	 * 
	 * @param bool
	 *            wether or not to vibrate
	 */
	private void performFeedback() {
		if (feedback) {
			GameEngine.getAppView().performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		}
	}
	
	/**
	 * Peforms results if the button is released
	 * @param e the event called
	 * @param i the index of the pointer that called the event
	 */
	private void buttonReleased(MotionEvent e,int i){
		if(dPadStruct != null){
			if (dPadHitRect.contains(e.getX(i), e.getY(i))) {
				dPadUp = false;
				dPadDown = false;
				dPadLeft = false;
				dPadRight = false;
				return;
			}
		}

		if (!disableButton1 && button1Struct != null) {
			if (button1Struct.intersects(e.getX(i), e.getY(i))) {
				button1 = false;
				return;
			}
		}

		if (!disableButton2 && button2Struct != null) {
			if (button2Struct.intersects(e.getX(i), e.getY(i))) {
				button2 = false;
				return;
			}
		}

		if (!disableButton3 && button3Struct != null) {
			if (button3Struct.intersects(e.getX(i), e.getY(i))) {
				button3 = false;
				return;
			}
		}

		if (!disableButton4 && button4Struct != null) {
			if (button4Struct.intersects(e.getX(i), e.getY(i))) {
				button4 = false;
				return;
			}
		}

		if(startStruct != null){
			if (startStruct.intersects(e.getX(i), e.getY(i))) {
				start = false;
				return;
			}
		}

		if(selectStruct != null){
			if (selectStruct.intersects(e.getX(i), e.getY(i))) {
				select = false;
				return;
			}
		}

		if(shoulder1Struct != null && shoulder2Struct != null){
			if (shoulder1Struct.intersects(e.getX(i), e.getY(i))) {
				shoulderL = false;
				return;
			}
	
			if (shoulder2Struct.intersects(e.getX(i), e.getY(i))) {
				shoulderR = false;
				return;
			}
		}

	}

	/**
	 * detection for buttonPresses.
	 */
	private void buttonPressed(MotionEvent e) {
		int pCount = e.getPointerCount();
		for (int i = 0; i < pCount; i++) {

			dpadPressed(e, i);
			buttonPressed(e, i);
			startButtonPressed(e,i);
			selectButtonPressed(e, i);
			shoulderButtonPressed(e,i);
		}
	}
	
	/**Performs the checks for pressing the dPad
	 * This function checks if the button is there or not. */
	private void dpadPressed(MotionEvent e, int i)
	{
		if(dPadStruct != null){
			if (dPadHitRect.contains(e.getX(i), e.getY(i))) {
				detectDpadHit(e.getX(i), e.getY(i));
				performFeedback();
			}
		}
	}
	
	/**Performs the checks for pressing any of the A,B,C,D Button
	 * This function checks if the button is there or not. */
	private void buttonPressed(MotionEvent e,int i){
		if (!disableButton1 && button1Struct != null) {
			if (button1Struct.intersects(e.getX(i), e.getY(i))) {
				button1 = true;
				performFeedback();
			}
		}

		if (!disableButton2 && button2Struct != null) {
			if (button2Struct.intersects(e.getX(i), e.getY(i))) {
				button2 = true;
				performFeedback();
			}
		}

		if (!disableButton3 && button3Struct != null) {
			if (button3Struct.intersects(e.getX(i), e.getY(i))) {
				button3 = true;
				performFeedback();
			}
		}

		if (!disableButton4 && button4Struct != null) {
			if (button4Struct.intersects(e.getX(i), e.getY(i))) {
				button4 = true;
				performFeedback();
			}
		}
	}
	
	/**Performs the checks for pressing the select button
	 * This function checks if the select button is there or not. */
	private void startButtonPressed(MotionEvent e, int i){
		if(startStruct != null){
			if (startStruct.intersects(e.getX(i), e.getY(i))) {
				start = true;
				performFeedback();
			}
		}
	}
	
	/**Performs the checks for pressing the select button
	 * This function checks if the select button is there or not. */
	private void selectButtonPressed(MotionEvent e, int i){
		if(selectStruct != null){
			if (selectStruct.intersects(e.getX(i), e.getY(i))) {
				select = true;
				performFeedback();
			}
		}
	}
	
	/**Performs the checks for pressing either shoulder button 
	 * This function checks if the shoulder button is there or not. */
	private void shoulderButtonPressed(MotionEvent e, int i){
		if(shoulder1Struct != null && shoulder2Struct != null){
			if (shoulder1Struct.intersects(e.getX(i), e.getY(i))) {
				shoulderL = true;
				performFeedback();
			}
	
			if (shoulder2Struct.intersects(e.getX(i), e.getY(i))) {
				shoulderR = true;
				performFeedback();
			}
		}
	}

	/**
	 * reupdates the button hit detection when the input moves around.
	 * @param e : the incoming event.
	 */
	private void buttonMoved(MotionEvent e) {
		boolean dPadIntersect = false, b1Intersect = false, b2Intersect = false, b3Intersect = false, b4Intersect = false, startIntersect = false, selectIntersect = false, shoulder1Intersect = false, shoulder2Intersect = false;
		int pCount = e.getPointerCount();
		float[] x = new float[pCount];
		float[] y = new float[pCount];
		for (int i = 0; i < pCount; i++) {

			x[i] = e.getX(i);
			y[i] = e.getY(i);

			if (!dPadIntersect && dPadStruct != null) {
				if (dPadHitRect.contains(x[i], y[i])) {
					dPadIntersect = true;
					detectDpadHit(x[i], y[i]);
					
				} else {
					dPadUp = false;
					dPadDown = false;
					dPadLeft = false;
					dPadRight = false;
				}
			}

			if (!disableButton1 && !b1Intersect && button1Struct != null) {
				if (button1Struct.intersects(x[i], y[i])) {
					b1Intersect = true;
					if (!button1) {
						button1 = true;
					}
				} else {
					button1 = false;
				}
			}

			if (!disableButton2 && !b2Intersect && button2Struct != null) {
				if (button2Struct.intersects(x[i], y[i])) {
					b2Intersect = true;
					if (!button2) {
						button2 = true;
					}
				} else {
					button2 = false;
				}
			}

			if (!disableButton3 && !b3Intersect && button3Struct != null) {
				if (button3Struct.intersects(x[i], y[i])) {
					b3Intersect = true;
					if (!button3) {
						button3 = true;
					}
				} else {
					button3 = false;
				}
			}

			if (!disableButton4 && !b4Intersect && button4Struct != null) {
				if (button4Struct.intersects(x[i], y[i])) {
					b4Intersect = true;
					if (!button4) {
						button4 = true;
					}
				} else {
					button4 = false;
				}
			}

			if (!startIntersect && startStruct != null) {
				if (startStruct.intersects(x[i], y[i])) {
					startIntersect = true;
					if (!start) {
						start = true;
					}
				} else {
					start = false;
				}
			}

			//checks for null
			if (!selectIntersect && selectStruct != null) {
				if (selectStruct.intersects(x[i], y[i])) {
					selectIntersect = true;
					if (!select) {
						select = true;
					}
				} else {
					select = false;
				}
			}
			
			//checks for null, this is used to make this method fail-safe!
			if(shoulder1Struct != null && shoulder2Struct != null){
				if (!shoulder1Intersect) {
					if (shoulder1Struct.intersects(x[i], y[i])) {
						shoulder1Intersect = true;
						if (!shoulderL) {
							shoulderL = true;
						}
					} else {
						shoulderL = false;
					}
				}
	
				if (!shoulder2Intersect) {
					if (shoulder2Struct.intersects(x[i], y[i])) {
						shoulder2Intersect = true;
						if (!shoulderR) {
							shoulderR = true;
						}
					} else {
						shoulderR = false;
					}
				}
			}
		}
			
	}

	/**
	 * detects which side of a dpad has been pressed, or possibly more sides.
	 * 
	 * @param x
	 *            incoming x location of the press or move action
	 * @param y
	 *            incoming y location of the press or move action
	 */
	private void detectDpadHit(float x, float y) {
		float xCenter = dPadHitRect.centerX();
		float yCenter = dPadHitRect.centerY();

		if (y < yCenter - screenBlock[0] * 4) {
			
			dPadLeft = false;
			dPadRight = false;
			dPadDown = false;
			dPadUp = true;
		} else if (y > yCenter + screenBlock[0] * 4) {
			
			dPadUp = false;
			dPadLeft = false;
			dPadRight = false;
			dPadDown = true;
		}
		if (x < xCenter - screenBlock[0] * 4) {
			dPadDown = false;
			dPadRight = false;
			dPadUp = false;
			dPadLeft = true;
			if (y > yCenter + screenBlock[0] * 6) {
				dPadDown = true;
			} else if (y < yCenter - screenBlock[0] * 6) {
				dPadUp = true;
			}
		} else if (x > xCenter + screenBlock[0] * 4) {
			dPadDown = false;
			dPadLeft = false;
			dPadUp = false;
			dPadRight = true;
			if (y > yCenter + screenBlock[0] * 6) {
				dPadDown = true;
			} else if (y < yCenter - screenBlock[0] * 6) {
				dPadUp = true;
			}
		}
	}

	/**
	 * initialize all the required drawing components
	 */
	private void initSprites() {

		alphaPaint = new Paint();
		alphaPaint.setAlpha(opacity);

		startSprite = new Sprite("start");
		selectSprite = new Sprite("select");
		dpadSprite = new Sprite("dpad");
		button1Sprite = new Sprite("buttona");
		button2Sprite = new Sprite("buttonb");
		button3Sprite = new Sprite("buttonx");
		button4Sprite = new Sprite("buttony");
		shoulder1Sprite = new Sprite("shoulder1");
		shoulder2Sprite = new Sprite("shoulder2");
	}

	/**
	 * draws the button interface to the screen.
	 */
	public void drawButtons(Canvas canvas) {
		if (oldOpacity != opacity) {
			alphaPaint.setAlpha(opacity);
		}
		oldOpacity = opacity;
		for (int i = 0; i < drawList.size(); i++) {
			canvas.drawBitmap(drawList.get(i).getSprite(), null,
							buttonList.get(i).rect, alphaPaint);
		}
	}

	/**
	 * used by the touchListener, do not call this function yourself!!!
	 * this checks for touch events from the device and it should call the appropriate function.
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (use) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:			
				buttonPressed(event);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				buttonPressed(event);
				break;
			case MotionEvent.ACTION_MOVE:
				buttonMoved(event);
				break;
			case MotionEvent.ACTION_UP:
				buttonReleased(event,0);
				break;
			case MotionEvent.ACTION_POINTER_UP:
				buttonReleased(event,event.getActionIndex());
				break;
			default:
				break;
			}
		}
		return true;
	}

	/**
	 * buttonStructure class for easier hit detection, and important
	 * information.
	 * This should function exactly as a structure.
	 */
	private class ButtonLocStruct {

		private Rect rect;
		
		/** Constructor for this structure */
		public ButtonLocStruct(int x, int y, int width, int height) {
			rect = new Rect(x, y, x + width, y + height);
		}
		
		/**
		 * checks if specified coordinates intersect with this button.
		 * @param x the x coordinate
		 * @param y the y coorinate
		 * @return TRUE if the coordinates are inside the rectangle
		 */
		public boolean intersects(float x, float y) {

			if (rect.contains((int)x,(int) y)) {
				return true;
			}
			return false;
		}
	}
}