package android.gameengine.icadroids.input;

import android.content.Context;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.widget.ImageButton;

/**
 * An custom (android) imageButton used in the onScreenButtons. Every button of
 * the OnScreenButtons is an instance of this class. A OnScreenButton can be
 * added programmatically or with the android layout xml editor (under Custom &
 * Library views).
 * 
 * @author Bas
 * 
 */
public class OnScreenButton extends ImageButton {

	/**
	 * Default constructor for an ImageButton
	 */
	public OnScreenButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		intializeButton();
	}

	/**
	 * Default constructor for an ImageButton
	 */
	public OnScreenButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		intializeButton();
	}

	/**
	 * Default constructor for an ImageButton
	 */
	public OnScreenButton(Context context) {
		super(context);
		intializeButton();
	}

	/**
	 * Initialize the button layout, like the opacity and the background.
	 */
	@SuppressWarnings("deprecation")
	// New implementations are only supported
	// by android 4.0+
	private void intializeButton() {
		setAlpha(OnScreenButtons.opacity); // 255 is max (visible)
		setBackgroundDrawable(null);
		setPadding(0, 0, 0, 0);
	}

	/**
	 * DO NOT CALL THIS FUNCTION.<br />
	 * Called by Android device to notify the Game of an user action.
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_UP) {
			OnScreenButtons.buttonPressed(getId());
		} else {
			OnScreenButtons.buttonReleased(getId());
		}

		if (OnScreenButtons.feedback && (event.getAction() == MotionEvent.ACTION_DOWN)) {
			performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		}

		return super.onTouchEvent(event);
	}

}
