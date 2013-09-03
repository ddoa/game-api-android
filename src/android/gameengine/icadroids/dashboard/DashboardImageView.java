package android.gameengine.icadroids.dashboard;

import android.gameengine.icadroids.engine.GameEngine;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * An extension of the default Android ImageView class
 * (see http://developer.android.com/reference/android/widget/ImageView.html)
 * for use as a dashboard widget.
 * 
 * 
 * @author Matthijs de Jonge
 *
 */
public class DashboardImageView extends ImageView {
	protected GameEngine gameEngine;
	
	protected String resourceName;
	protected Bitmap bitmap;
	
	/**
	 * Constructor. Supply the game engine object and the resource name.
	 * Use this constructor if you want to draw your image immediately.
	 * @param gameEngine
	 * 		the game engine object
	 * @param resourceName
	 * 		the resource name
	 */
	public DashboardImageView(GameEngine gameEngine, String resourceName) {
		super(gameEngine);
		this.gameEngine = gameEngine;
		this.resourceName = resourceName;
		this.drawImage();
		
	}
	
	/**
	 * Constructor. Supply the game engine object. Use this constructor if you
	 * want to create a new DashboardImageView but you don't want to set an image
	 * yet.
	 * @param gameEngine
	 * 		the game egine object
	 */
	public DashboardImageView(GameEngine gameEngine) {
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
	 * Set the resource (image) name. The resource name is the name
	 * of the image file, without the file extension. The image file must be
	 * stored in res/drawable.
	 * 
	 * Upon setting the resource name, the image is redrawn immediately.
	 * 
	 * @param resourceName
	 * 		the image resource name
	 */
	public void setResourceName(String resourceName) {
		if((resourceName != this.resourceName)
				&& this.bitmap != null) {
			this.bitmap.recycle();
			
		}
		this.resourceName = resourceName;
		this.drawImage();
	}

	public void drawImage() {
		this.run(new Runnable(){
			public void run() {
				if(resourceName == null) {
					Log.e("DashboardImageView.drawImage", "No resource name set");
					return;
				}
				
				int resID = GameEngine
						.getAppContext()
						.getResources()
						.getIdentifier(resourceName, "drawable",
								GameEngine.getAppContext().getPackageName());
				if(resID == 0) {
					Log.e("DashboardImageView.drawImage", "Unable to load resource");	
					return;
				}
				
				bitmap = BitmapFactory.decodeResource(GameEngine
						.getAppContext().getResources(), resID);
				setImageBitmap(bitmap);
				//Log.d("DashboardImageView.drawImage", "Drawing bitmap");
				
				
				
 
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
		final View widget = this;
		this.run(new Runnable() {
			public void run() {
				LinearLayout.LayoutParams lp = (LayoutParams) widget.getLayoutParams();
				lp.width = newWidth;
				widget.setLayoutParams(lp);
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
		final View widget = this;
		this.run(new Runnable() {
			public void run() {
				LinearLayout.LayoutParams lp = (LayoutParams) widget.getLayoutParams();
				lp.height = newHeight;
				widget.setLayoutParams(lp);
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
	

}
