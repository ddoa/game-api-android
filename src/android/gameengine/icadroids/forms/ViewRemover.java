package android.gameengine.icadroids.forms;

import android.view.ViewManager;

/**
 * Removes views created by the ViewCreator. <b> Don't use this class by
 * yourself! It's used by GameForm to correctly remove views </b>
 * 
 * <b>Note: This class must run on the UI thread.</b>
 * 
 * @author Bas van der Zandt
 * 
 */
public class ViewRemover implements Runnable {

	/**
	 * ID of the view that must be thrown away
	 */
	private int layoutID;

	/**
	 * Remove the given view created by ViewCreator
	 * 
	 * @param layoutID
	 *            the layout id that must be deleted
	 */
	protected ViewRemover(int layoutID) {
		this.layoutID = layoutID;
	}

	/**
	 * run() runs on the UI Thread for one time. It handles the correct deletion
	 * of the view
	 */
	public void run() {
		for (int i = 0; i < ViewCreator.formViews.size(); i++) {
			if (ViewCreator.formViews.get(i).getTag().equals(layoutID)) {
				((ViewManager) ViewCreator.formViews.get(i).getParent())
						.removeView(ViewCreator.formViews.get(i));
				ViewCreator.formViews.remove(i);
			}
		}
	}
}
