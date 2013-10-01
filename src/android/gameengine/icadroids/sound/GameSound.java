package android.gameengine.icadroids.sound;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.gameengine.icadroids.engine.GameEngine;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * This class features a number of static methods that can be used to add sound
 * to your game.
 * 
 * This class should be used when you want to play short sound clips, the length
 * of these clips should not be longer than about 5 seconds. For larger files,
 * use the MusicPlayer class.
 * 
 * @author Leon & Lex
 * 
 */
public final class GameSound {

	private static SoundPool soundPool;
	private static Context context;
	private static HashMap<Integer, Integer> soundPoolMap;
	private static HashMap<Integer, Integer> currentSoundMap;
	private static AudioManager audioManager;
	private static boolean hasPlayed = false;
	private static int maxStreams = 20;
	private static ArrayList<Integer> soundList;

	/**
	 * Constructs an instance of this class.
	 */
	public GameSound() {
		initSounds(GameEngine.getAppContext());
	}

	/**
	 * Initializes the storage for the sounds.
	 * 
	 * @param theContext
	 *            The Application context.
	 */
	public static final void initSounds(Context theContext) {
		context = theContext;
		soundList = new ArrayList<Integer>();
		soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap = new HashMap<Integer, Integer>();
		currentSoundMap = new HashMap<Integer, Integer>();
		audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * Adds a new sound to the SoundPool. Call this method to store a sound, you
	 * can then retrieve the sound from the SoundPool with the playSound
	 * function.
	 * 
	 * @param index
	 *            The sound index where the sound will be stored.
	 * @param soundID
	 *            The name of the sound that needs to be stored.
	 */
	public static final void addSound(int index, String soundID) {
		int resID = GameEngine
				.getAppContext()
				.getResources()
				.getIdentifier(soundID, "raw",
						GameEngine.getAppContext().getPackageName());
		soundPoolMap.put(index, soundPool.load(context, resID, 1));
	}

	/**
	 * Plays a sound that has been added to the SoundPool with addSound
	 * 
	 * @param index
	 *            The Index of the Sound to be played, defined by addSound.
	 * @param loopSound
	 *            The amount of times the music should loop, a negative value
	 *            will result in an endless loop.
	 */
	public static final void playSound(int index, int loopSound) {
		float streamVolume = audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int i = soundPool.play(soundPoolMap.get(index), streamVolume,
				streamVolume, 1, loopSound, 1f);
		currentSoundMap.put(index, i);
		soundList.add(i);
		hasPlayed = true;
	}

	/**
	 * Pause a sound, call resumeSound to continue playing the specified sound.
	 * 
	 * @param index
	 *            index of the sound to be paused.
	 */
	public static final void pauseSound(int index) {
		if (currentSoundMap.get(index) != null) {
			soundPool.pause(currentSoundMap.get(index));
		}
	}

	/**
	 * Pauses all sounds, call resumeSounds to continue playing sounds.
	 */
	public static final void pauseSounds() {
		soundPool.autoPause();
	}

	/**
	 * Resumes the specified paused sound.
	 * 
	 * @param index
	 *            index of the sound to be resumed
	 */
	public static final void resumeSound(int index) {
		if (currentSoundMap.get(index) != null) {
			soundPool.resume(currentSoundMap.get(index));
		}
	}

	/**
	 * Resumes all paused sounds.
	 */
	public static final void resumeSounds() {
		soundPool.autoResume();
	}

	/**
	 * Stops a sound
	 * 
	 * @param index
	 *            index of the sound to be stopped
	 */
	public static final void stopSound(int index) {
		if (currentSoundMap.get(index) != null) {
			soundPool.stop(currentSoundMap.get(index));
		}
	}

	/**
	 * Stops all sounds.
	 */
	public static final void stopSounds() {
		if (soundList != null) {
			for (int i = 0; i < soundList.size(); i++) {
				soundPool.stop(soundList.get(i));
			}
		}
	}

	/**
	 * Deallocates the resources and Instance of GameSound.This is called
	 * automatically when the application is be closed.
	 */
	public static final void cleanup() {
		if (hasPlayed) {
			soundPool.release();
			soundPool = null;
			soundPoolMap.clear();
			audioManager.unloadSoundEffects();
		}
	}
}
