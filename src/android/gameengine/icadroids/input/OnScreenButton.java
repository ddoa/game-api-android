package android.gameengine.icadroids.input;

import android.annotation.TargetApi;
import android.app.SearchManager.OnCancelListener;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class OnScreenButton extends ImageButton {

	public OnScreenButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		intializeButton();
	}
	public OnScreenButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		intializeButton();
	}
	public OnScreenButton(Context context) {
		super(context);
		intializeButton();
	}

	@SuppressWarnings("deprecation") //New implementations are only supported
									// by android 4.0+
	private void intializeButton(){
		setAlpha(125); //255 is max (visible)
		setBackgroundDrawable(null);
		setPadding(0, 0, 0, 0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_UP){
			OnScreenButtons.buttonPressed(getId());
		} else {
			OnScreenButtons.buttonReleased(getId());
		}
		return super.onTouchEvent(event);
	}
}
