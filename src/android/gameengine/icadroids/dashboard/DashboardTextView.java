package android.gameengine.icadroids.dashboard;

import android.gameengine.icadroids.engine.GameEngine;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * An extension of the default Android TextView class
 * (see http://developer.android.com/reference/android/widget/TextView.html)
 * for use as a dashboard widget.
 * 
 * Interesting methods include setText(String text), setTextColor(int textColor)
 * and setTextSize(int unit, float size). Unit for text size can be one of:
 * - TypedValue.COMPLEX_UNIT_DIP : device independent pixels
 * - TypedValue.COMPLEX_UNIT_PT : points
 * - TypedValue.COMPLEX_UNIT_PX : pixels
 * 
 * @author Matthijs de Jonge
 *
 */
public class DashboardTextView extends TextView {

	protected GameEngine gameEngine;
	
	/**
	 * Instantiate a new dashboard text view. Supply the game
	 * engine object or your own subclass of it.
	 * 
	 * @param gameEngine
	 * 		the game engine object
	 */
	public DashboardTextView(GameEngine gameEngine) {
		super(gameEngine);
		this.gameEngine = gameEngine;
	}
	
	/**
	 * Execute a piece of code, presumably something that
	 * has to do with this dashboard widget. <b>This is the
	 * only way to perform actions on a dashboard widget.</b>
	 * Some widget methods, such as setting the color, can be
	 * called directly but most can't, especially if they cause
	 * the widget to be redrawn in its entirety, such as text changes
	 * or position changes.
	 * 
	 * @param runnable
	 * 		the runnable to execute
	 */
	public void run(Runnable runnable) {
		this.gameEngine.runOnUiThread(runnable);
	}
	
	/**
	 * Set the text to be displayed in this widget.
	 * @param textString
	 * 		the text to display
	 */
	public void setTextString(String textString) {
		final String textToDisplay = textString;
		this.run(new Runnable() {
			public void run() {
				setText(textToDisplay);
			}
		});
	}
	
	/**
	 * Set the width of the widget (in pixels). If you don't use this
	 * method, the widget will take up as much space as it needs.
	 * @param width
	 * 		the width of the widget
	 */
	public void setWidgetWidth(int width) {
		final int newWidth = width;
		this.run(new Runnable() {
			public void run() {
				setWidth(newWidth);
			}
		});
	}
	
	/**
	 * Set the height of the widget (in pixels). If you don't use this
	 * method, the widget will take up as much space as it needs.
	 * @param height
	 * 		the height of the widget
	 */
	public void setWidgetHeight(int height) {
		final int newHeight = height;
		this.run(new Runnable() {
			public void run() {
				setHeight(newHeight);
			}
		});
	}
	
	/**
	 * Set the x position of the widget (in pixels).
	 * @param x
	 * 		the x position of the widget
	 */
	public void setWidgetX(int x) {
		final int newX = x;
		final View widget = this;
		this.run(new Runnable() {
			public void run() {
				LinearLayout.LayoutParams lp = (LayoutParams) widget.getLayoutParams();
				lp.leftMargin = newX;
				widget.setLayoutParams(lp);
			}
		});
	}
	/**
	 * Set the y position of the widget (in pixels).
	 * @param y
	 * 		the y position of the widget
	 */
	public void setWidgetY(int y) {
		final int newY = y;
		final View widget = this;
		this.run(new Runnable() {
			public void run() {
				LinearLayout.LayoutParams lp = (LayoutParams) widget.getLayoutParams();
				lp.topMargin = newY;
				widget.setLayoutParams(lp);
			}
		});
	}
	
	/**
	 * Set the background color for the widget. Use the Android
	 * Color class to determine the values to use (see
	 * http://developer.android.com/reference/android/graphics/Color.html)
	 * 
	 * @param color
	 * 		the background color to use
	 */
	public void setWidgetBackgroundColor(int color) {
		final int newColor = color;
		this.run(new Runnable() {
			public void run() {
				setBackgroundColor(newColor);
			}
		});
	}
	
	/*
	public void setWidgetPadding(int pixels) {
		final int newPadding = pixels;
		final View widget = this;
		this.run(new Runnable() {
			public void run() {
				LinearLayout.LayoutParams lp = (LayoutParams) widget.getLayoutParams();
				widget.setPadding(left, top, right, bottom)
				widget.setLayoutParams(lp);
			}
		});
	}
	*/
	
	
}
