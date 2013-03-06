package android.gameengine.icadroids.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.gameengine.icadroids.engine.GameEngine;

/**
 * Class for user persistence. With this Class you can save Strings in a File to
 * your internal storage. Both real devices and emulators can use the internal
 * storage.
 * 
 * You can use the game persistance for example to save variables or,
 * highscores. You are allowed to use multiple game persistances, each
 * additional persistance saves an additional file on your system if the
 * filename name is different.
 * 
 * @author Roel
 */
public class GamePersistence {

	private String filename;

	/**
	 * The constructor allows you to specify the filename the internal storage
	 * will use.
	 * 
	 * @param filename
	 *            The name of the file that will be used for this persistance.
	 */
	public GamePersistence(String filename) {
		this.filename = filename;
	}

	/**
	 * Saves the data to the earlier specified file. All previous saved data
	 * will be overwritten. You can get your previous data by loading it first
	 * and then appending your new data to the String.
	 * 
	 * @param data
	 *            The data that should be saved to the file, the data must be
	 *            one entire String.
	 */
	public void saveData(String data) {
		FileOutputStream fos;
		try {
			GameEngine.getAppContext();
			fos = GameEngine.getAppContext().openFileOutput(filename,
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write(data);
			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the full data (in String format) that is stored in the file. This
	 * function will return the data or an empty String if the file does not
	 * exist, or the file is empty.
	 * 
	 * @return String The data saved earlier.
	 */
	public String loadData() {
		String content = "";
		File file = new File(GameEngine.getAppContext().getFilesDir(), filename);
		if (file != null) {
			int buffersize = (int) file.length();
			char[] buffer = new char[buffersize];
			try {
				FileInputStream fIn = GameEngine.getAppContext().openFileInput(
						filename);
				InputStreamReader isr = new InputStreamReader(fIn);
				isr.read(buffer);
				isr.close();
				fIn.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			content = new String(buffer);
		}

		return content;
	}

}
