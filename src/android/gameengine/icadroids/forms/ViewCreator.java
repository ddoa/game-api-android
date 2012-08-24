package android.gameengine.icadroids.forms;

import java.util.ArrayList;
import java.util.Vector;

import android.gameengine.icadroids.engine.GameEngine;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

/**
 * ViewCreator puts (form)views on top of the GameView. This views are made in
 * Android Layout XML. It also registers any touchable components in the view.
 * 
 * <b> Don't use this class by yourself! It's used by GameForm to correctly add
 * views </b>
 * 
 * <b>Note: This class must run on the UI thread.</b>
 * 
 * @author Bas
 */
public class ViewCreator implements Runnable {

	/**
	 * The GameEngine that is currently running
	 */
	GameEngine gameEngine;
	/**
	 * The IFormInput that must be informed of inputs
	 */
	IFormInput IformInput;
	/**
	 * The ID of the view that must be added
	 */
	int layoutID;
	/**
	 * Vector with all the formViews that are currently availble
	 */
	public static Vector<View> formViews = new Vector<View>();

	/**
	 * Creates a view on top of the GameView that are made in the android XML
	 * layout file
	 * 
	 * @param gameengine
	 * @param layoutID
	 * @param IformInput
	 */
	protected ViewCreator(GameEngine gameengine, int layoutID,
			IFormInput IformInput) {
		this.IformInput = IformInput;
		gameEngine = gameengine;
		this.layoutID = layoutID;
	}

	/**
	 * Add a view on top of the current view and register any touchable
	 * components in the view to a touch-listener so the IFormInput object can
	 * receive touch calls.
	 * 
	 * run() runs on the UI thread for one time
	 */
	public void run() {
		GameEngine.getAppView().setVisibility(View.VISIBLE);

		LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		View formView = gameEngine.getLayoutInflater().inflate(layoutID, null);
		formView.setTag(layoutID);
		gameEngine.getWindow().addContentView(formView, lp);

		ArrayList<View> touchables = formView.getTouchables();

		for (int i = 0; i < touchables.size(); i++) {

			touchables.get(i).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (v instanceof EditText) {
						// Show the Soft-Keyboard. Android does not show
						// the soft-keyboard automatically
						gameEngine.showKeyboard();
					}
					IformInput.formElementClicked(v);
				}
			});

		}
		formViews.add(formView);
	}
}
