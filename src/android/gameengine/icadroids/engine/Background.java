package android.gameengine.icadroids.engine;

import android.gameengine.icadroids.objects.graphics.Sprite;
/**
 * Needs futher implementation, not ready for usage
 * 
 * used for managing and drawing backgrounds
 * @author Bas
 *
 */
public class Background {
	
	private String backgroundResource = null;
	Sprite backgroundImage = new Sprite();
	
	boolean backgroundIsStretched = true;

	protected Background(){
		
	}
	
	public void setBackground(String backgroundResource){
		backgroundImage.loadSprite(backgroundResource);
		
	}
	public void drawBackground(){
		
	}
	
	public void setStretch(boolean stretchOn){
		backgroundIsStretched = stretchOn;
	}
}
