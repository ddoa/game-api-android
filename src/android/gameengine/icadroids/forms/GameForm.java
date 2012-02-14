package android.gameengine.icadroids.forms;

import android.gameengine.icadroids.engine.GameEngine;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * With GameForm you can create (and delete) views on top of the current view.
 * 
 * <b> Always create a GameForm inside a triggerAlarm! Else the GameForm will not be
 * shown! </b>
 * 
 * <b> When you show a view, the GameView will still be running! You must pause
 * this view by yourself. </b>
 * 
 * The layout of the views are made in a android XML layout file. You can use
 * the UI builder from android to make one. Layouts are saved in the /res/layout
 * folder.
 * 
 * To receive touch inputs, you must implement the IFormInput interface.
 * 
 * @author Bas van der Zandt
 * 
 */
public class GameForm {

	/**
	 * The GameEngine where the game is currently running on
	 */
	private GameEngine gameEngine;

	/**
	 * Make a GameForm and show immediately the given Layout
	 * 
	 * @param layoutName
	 *            The layout name given in the res/layout folder. <b>Give the
	 *            name without .XML extension! </b> . So if your layout is
	 *            called 'main.xml', this parameter should be "main" .
	 * @param IformInput
	 *            The IFormInput object that will receive touch inputs of this
	 *            layout
	 * @param gameEngine
	 *            The GameEngine which this game is currently running on.
	 */
	public GameForm(String layoutName, IFormInput IformInput,
			GameEngine gameEngine) {
		this.gameEngine = gameEngine;
		showView(layoutName, IformInput);
	}

	/**
	 * Make a GameForm without adding a new layout.
	 * 
	 * @param gameEngine
	 *            The GameEngine which the game is currently running on
	 */
	public GameForm(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}

	/**
	 * Add a new view (layout) on the top of the current view.
	 * 
	 * @param layoutName
	 *            The layout name given in the res/layout folder. <b>Give the
	 *            name without .XML extension! </b> . So if your layout is
	 *            called 'main.xml', this parameter should be "main" .
	 * @param IformInput
	 *            The IFormInput object that will receive touch inputs of this
	 *            layout
	 */
	public void showView(String layoutName, IFormInput IformInput) {
		int resID = getViewID(layoutName);
		ViewCreator vw = new ViewCreator(gameEngine, resID, IformInput);
		gameEngine.runOnUiThread(vw);
	}

	/**
	 * Get the numeric ID of the layout.
	 * 
	 * @param layoutName
	 *            The layout name without xml extension
	 * @return The numeric ID of the layout
	 */
	private int getViewID(String layoutName) {
		return gameEngine.getResources().getIdentifier(layoutName, "layout",
				gameEngine.getPackageName());
	}

	/**
	 * Get the ID name of an view element, like a button or textbox.
	 * 
	 * @param viewElement
	 * @return The name of the view element. This name is specified in
	 *         android:id in your android layout xml.
	 */
	public String getElementName(View viewElement) {
		return gameEngine.getResources().getResourceEntryName(
				viewElement.getId());
	}

	/**
	 * Toast are little grey boxes with text in it that appear a few seconds on
	 * the screen. You can make a toast with this method.
	 * 
	 * @param text
	 *            The text that will be visible in the toast
	 * @param duration
	 *            The duration of the toast in seconds
	 */
	public void sendToast(String text, int duration) {
		Toast.makeText(GameEngine.getAppContext(), text, duration).show();
	}

	/**
	 * Get text from a textfield.
	 * 
	 * @param textfieldName
	 *            The android:id name of the textfield specified in your android
	 *            layout xml file
	 * @return Text in textfield
	 */
	public String getTextFromTextfield(String textfieldName) {
		int resID = gameEngine.getResources().getIdentifier(textfieldName,
				"id", gameEngine.getPackageName());

		EditText textField = (EditText) gameEngine.findViewById(resID);
		return textField.getText().toString();
	}

	/**
	 * Remove the specified view from the screen.
	 * 
	 * @param layoutName
	 *            The layout name given in the res/layout folder. <b>Give the
	 *            name without .XML extension! </b> . So if your layout is
	 *            called 'main.xml', this parameter should be "main" .
	 */
	public void removeView(String layoutName) {
		int resID = getViewID(layoutName);
		ViewRemover vr = new ViewRemover(resID);
		gameEngine.runOnUiThread(vr);
	}

	/**
	 * Get the view of a elementname
	 * @param idName the id name of the view
	 * @return The coresponding View
	 */
	public View findViewElementByName(String idName) {
		int resID = gameEngine.getResources().getIdentifier(idName, "id",
				gameEngine.getPackageName());
		return gameEngine.findViewById(resID);
	}

}
