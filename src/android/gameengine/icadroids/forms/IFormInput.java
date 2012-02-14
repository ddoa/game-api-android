package android.gameengine.icadroids.forms;

import android.view.View;

/**
 * Interface for receiving inputs from touchable form elements.
 * 
 * @author Bas van der Zandt
 * 
 */
public interface IFormInput {
	/**
	 * When a touchable element has been clicked on a form, this method will be
	 * Triggered.
	 * 
	 * @param touchedElement
	 *            The view element that has been touched
	 */
	void formElementClicked(View touchedElement);
}
