package android.gameengine.icadroids.engine;

import java.util.Collection;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.input.MotionSensor;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.input.TouchInput;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.gameengine.icadroids.renderer.GameView;
import android.gameengine.icadroids.sound.GameSound;
import android.gameengine.icadroids.sound.MusicPlayer;
import android.gameengine.icadroids.tiles.GameTiles;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * GameEngine is the core of the game. Extending this class is required to make
 * use of the GameEngine. It contains many methods for GameObjects, Alarms,
 * Sensors, Tiles, etc.
 * 
 * @author Edward van Raak & Roel van Bergen & Leon van Kleef & Bas van der
 *         Zandt & Lex van der Laak
 * 
 * @version 0.9
 */
public abstract class GameEngine extends Activity {
	/**
	 * The player that the viewport follows //
	 */
	private MoveableGameObject player;
	/**
	 * Gameloop is a gameThread that handles the timing of the game
	 */
	private GameLoop gameloop;
	/**
	 * View deals with the proper rendering of the game
	 */
	private static GameView view;
	/**
	 * The width and height of the device
	 */
	private static int screenWidth, screenHeight;
	/**
	 * TouchInput handles input by touching the screen
	 */
	private TouchInput touch;
	/**
	 * OnScreenButtons draws buttons to screen and handles input by touch
	 */
	private OnScreenButtons screenButtons;
	/**
	 * Vibrator holds the methods that handle vibrating functionalities of a
	 * phone.
	 */
	private Vibrator vibrator;
	/**
	 * A vectorlist that holds all the active GameObjects. Can be used if you
	 * mannualy want to delete/change GameObjects. For instance, you could loop
	 * through this list and remove health of every GameObject.
	 */
	public static Vector<GameObject> items;
	/**
	 * A vectorlist that holds all the active alarms. Can be used if you
	 * manually want to delete/change alarms.
	 */
	public static Vector<Alarm> gameAlarms;
	/**
	 * If update loop is set to true, use an experimental UpdateLoop
	 */
	public static final boolean UPDATE_LOOP_ON = false;
	/**
	 * The optional UpdateLoop handles the updates of game items and does not
	 * render
	 */
	private UpdateLoop updateLoop;
	/**
	 * Secondary thread that handles the optional UpdateLoop (Experimental)
	 */
	private Thread uLoop;
	/**
	 * Holds context of the application
	 */
	private static Context appContext;
	/**
	 * The main thread of the gameloop
	 */
	private Thread gameThread;
	/**
	 * Sets the mobile device to landscape view if set to true
	 */
	private boolean landscape = true;
	/**
	 * Motion Sensor is used to receive certain statics about certain
	 * motionEvent note that this only works on real phones and not in emulator.
	 */
	private MotionSensor sensor;

	public static GameTiles gameTiles;

	/**
	 * The GameEngine forms the core of the game by controlling the gameloop and
	 * the render operations. All games must extend this base class.
	 * 
	 * @author Edward van Raak, Roel van Bergen, Leon van Kleef, Lex van de
	 *         laak, Bas van der Zandt
	 * @version 1.0, January 10, 2012
	 */
	public GameEngine() {

		gameTiles = new GameTiles(100);
		items = new Vector<GameObject>();
		gameAlarms = new Vector<Alarm>();
	}

	/***
	 * <b>Do NOT call this method.</b>
	 * <p>
	 * Called when activity is started. Initialise GameEngine objects and set
	 * options to be used by the GameEngine.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkScreenOrientation();
		System.out.println("oncreate....");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED,
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		appContext = getApplicationContext();
		gameloop = new GameLoop(this);
		gameThread = new Thread(gameloop);
		gameThread.setPriority(7);

		view = new GameView(this, gameThread);
		gameloop.setView(view);

		touch = new TouchInput();
		screenButtons = new OnScreenButtons();
		sensor = new MotionSensor();
		GameSound.initSounds(getAppContext());
		if (MotionSensor.use) {
			sensor.initializeSensors();
		}

		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();

		setContentView(view);
		view.setKeepScreenOn(true);

		if (UPDATE_LOOP_ON) {
			updateLoop = new UpdateLoop(this);
			uLoop = new Thread(updateLoop);
			uLoop.setPriority(6);
			uLoop.start();
		}
	}

	/***
	 * Allows the game to perform any initialization it needs to before starting
	 * to run like:
	 * <p>
	 * <ul>
	 * <li>Creating instances of GameObject and Tiletypes.</li>
	 * <li>Calling contructors/initalization of GameObject instances.</li>
	 * <li>Configuring Background sprite, Background color, OnScreenButtons,
	 * Tile size and TileMaps.</li>
	 * <li>Starting the game.</li>
	 * </ul>
	 * <p>
	 * Override this method inside your game base class that extends from
	 * GameEngine. Call super.initialize() at the very start.
	 */
	protected void initialize() {

		System.out.println("Intializing...");

		if (Sprite.loadDelayedSprites != null) {
			for (Sprite sprite : Sprite.loadDelayedSprites) {
				sprite.initialize();
			}
		}
		Sprite.loadDelayedSprites = null;

		for (GameObject item : items) {
			item.intializeGameObject();
		}

	}

	protected void intializeTouch() {
		if (TouchInput.use) {
			view.setOnTouchListener(touch);
		} else if (OnScreenButtons.use) {
			Log.d("ButtonEnabled", "USING ON SCREEN BUTTONS");
			view.setOnTouchListener(screenButtons);
		}
	}

	/**
	 * <b>Do NOT call this method.</b>
	 * <p>
	 * Call the update for every GameObject added to the list.
	 */
	protected final void updateGame() {
		update();
		for (int i = 0; i < items.size(); i++) {
			items.get(i).update();

			if (!items.get(i).active) {
				items.remove(i);
			}
		}
		for (int i = 0; i < gameAlarms.size(); i++) {
			gameAlarms.get(i).update();
		}
	}

	/***
	 * Allows the game to run logic such as updating the world, checking for
	 * collisions, gathering input, and playing audio.
	 * <p>
	 * Override this method inside your game base class that extends from
	 * GameEngine. Every gameloop this method is called once.
	 * <p>
	 * GameObjects that were added to the game (by either addPlayer or
	 * addGameObject) should NOT be updated here. Classes that extend from
	 * GameObject or MovableGameObject have their own update() that should be
	 * overwritten.
	 * 
	 * @see android.gameengine.icadroids.objects.MoveableGameObject
	 * @see android.gameengine.icadroids.objects.GameObject
	 */
	public void update() {

	}

	/***
	 * Delete a GameObject. Including it's instance.
	 * 
	 * @param gameObject
	 *            The GameObject instance to be removed
	 */
	public final void deleteGameObject(GameObject gameObject) {
		items.removeElement(gameObject);
	}

	/***
	 * Delete all GameObjects. Included instances.
	 */
	public final void deleteAllGameObjects() {
		items.removeAllElements();
	}

	/**
	 * Removes all alarm instances
	 */
	public void deleteAllAlarms() {
		gameAlarms.removeAllElements();
	}

	/**
	 * Remove all GameObject instances of given class type.
	 * 
	 * @param type
	 *            De class type of the instances to be removed
	 */
	public <T> void deleteAllGameObjectsOfType(Class<T> type) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getClass() == type) {
				items.remove(i);
			}
		}
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 */
	@Override
	protected final void onResume() {
		super.onResume();
		System.out.println("onResume()...");
		registerMotionSensor();
		gameloop.setRunning(true);
		if (gameThread.getState() == Thread.State.TERMINATED) {
			System.out.println("thread terminated, starting new thread");
			gameThread = new Thread(gameloop);
			view.setGameThread(gameThread);
			gameloop.setRunning(true);
		}
		GameSound.resumeSounds();
		MusicPlayer.resumeAll();
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 */
	@Override
	protected final void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroy...");
		if (UPDATE_LOOP_ON) {
			updateLoop.setRunning(false);
		}
		GameSound.cleanup();
		MusicPlayer.stop();
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 */
	@Override
	protected final void onPause() {
		super.onPause();
		System.out.println("OnPause...");
		unregisterMotionSensor();
		pause();
		GameSound.pauseSounds();
		MusicPlayer.pauseAll();
	}

	/**
	 * Start the gameloop thread. Should be called during the initializing
	 * phase.
	 */
	@Deprecated
	protected void startGame() {

	}

	/**
	 * End the game and close the application.
	 */
	protected final void endGame() {
		gameloop.setRunning(false);
		finish();
	}

	/**
	 * Add a GameObject to the GameObjectList. Items added to this list are
	 * updated accordingly.<br>
	 * 
	 * @param gameObject
	 *            The GameObject that will be added to the game. Should have
	 *            either GameObject or MovableGameObject as it's parent.
	 * @param x
	 *            The X spawnlocation when this object is created
	 * @param y
	 *            The Y spawnlocation when this object is created
	 */
	public final void addGameObject(GameObject gameObject, int x, int y) {
		gameObject.setStartPosition(x, y);
		gameObject.jumpToStartPosition();
		items.add(gameObject);
	}

	/**
	 * Add a GameObject to the GameObjectList. Items added to this list are
	 * updated accordingly.<br>
	 * 
	 * @param gameObject
	 *            The GameObject that will be added to the game. Should have
	 *            either GameObject or MovableGameObject as it's parent.
	 * @param x
	 *            The X spawnlocation when this object is created
	 * @param y
	 *            The Y spawnlocation when this object is created
	 * @param layerposition
	 *            The layerposition when this object is drawed. <b>Between 0 and
	 *            1 (float). </b> 1 front, 0 back
	 */
	public final void addGameObject(GameObject gameObject, int x, int y,
			float layerposition) {
		gameObject.setStartPosition(x, y);
		gameObject.jumpToStartPosition();
		items.add(Math.round(items.size() * layerposition), gameObject);
	}

	/**
	 * Add a GameObject to the GameObjectList.
	 * 
	 * @param gameObject
	 *            The GameObject that will be added to the game. Should have
	 *            either GameObject or MovableGameObject as it's parent.
	 * @param layerposition
	 *            The layerposition when this object is drawed. <b>Between 0 and
	 *            1 (float). </b> 1 front, 0 back
	 */
	public final void addGameObject(GameObject gameObject, float layerposition) {
		items.add(Math.round(items.size() * layerposition), gameObject);
	}

	/**
	 * Add a GameObject to the GameObjectList
	 * 
	 * @param gameObjectThe
	 *            GameObject that will be added to the game. Should have either
	 *            GameObject or MovableGameObject as it's parent.
	 */
	public final void addGameObject(GameObject gameObject) {
		items.add(gameObject);
	}

	/**
	 * Add a collection of GameObjects. Usefull if you want to add a lot of
	 * objects.
	 * 
	 * @param objectList
	 */
	public final void addListOfObject(Collection<GameObject> objectList) {
		items.addAll(objectList);
	}

	/**
	 * Set a GameTiles object as the current GameTile map.
	 * 
	 * Width and Height of the map is automatically calculated. Do not make a
	 * map that has parts sticking out. Use -1 to create an empty space. <br/>
	 * 
	 * @param set
	 *            the current gameTiles object.
	 */
	protected void setTileMap(GameTiles gameTiles) {
		GameEngine.gameTiles = gameTiles;
	}

	/**
	 * Vibrates the phone in the specified pattern.
	 * 
	 * @param pattern
	 *            the pattern is an array of milliseconds. the first value in
	 *            the pattern is the amount of time to wait before turning the
	 *            vibration on the second value is how long it should actually
	 *            vibrate this is repeated trough out:
	 *            gap-vibration-gap-vibration etc.. example SOS: long[] pattern
	 *            = {200,500,200,800,200,500,200}; in this number 200 is
	 *            represented as a short gap.
	 */
	public final void vibrate(long[] pattern) {
		vibrator.vibrate(pattern, -1);
	}

	/**
	 * vibrates the phone for the amount of milliseconds specified.
	 * 
	 * @param milliseconds
	 *            the amount of milliseconds
	 */
	public final void vibrate(int milliseconds) {
		vibrator.vibrate(milliseconds);
	}

	/**
	 * draws the interface if the screen buttons are enabled.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 */
	public final void drawInterface(Canvas canvas) {
		if (OnScreenButtons.use) {
			screenButtons.drawButtons(canvas);
		}
	}

	/**
	 * <b> DO NOT CALL THIS METHOD </b><br>
	 * 
	 * @return the gameloop
	 */
	public final GameLoop getGameloop() {
		return gameloop;
	}

	/**
	 * Return the player instance.
	 * 
	 * @return
	 */
	public final MoveableGameObject getPlayer() {
		return player;
	}

	/***
	 * Add a Player to the GameObjectList. Items added to this list are updated
	 * accordingly.<br>
	 * This method should be called during the initializing phase. Only add 1
	 * player at a time.
	 * 
	 * @param player
	 *            The player that should be added to the game. The location of
	 *            this player is used by the viewport.
	 * @param x
	 *            The X spawnlocation when this object is created
	 * @param y
	 *            The Y spawnlocation when this object is created
	 */
	public final void addPlayer(MoveableGameObject player, int x, int y) {
		player.setStartPosition(x, y);
		player.jumpToStartPosition();
		items.add(player);
		this.player = player;
	}

	/***
	 * Add a GameObject to the GameObjectList. Items added to this list are
	 * updated accordingly.<br>
	 * This method should be called during the initializing phase.
	 * 
	 * @param player
	 *            The player that should be added to the game. The location of
	 *            this player is used by the viewport.
	 * @param x
	 *            The X spawnlocation when this object is created
	 * @param y
	 *            The Y spawnlocation when this object is created
	 * @param layerposition
	 *            The layerposition when this object is drawed. <b>Between 0 and
	 *            1 (float). </b> 1 front, 0 back
	 */
	public final void addPlayer(MoveableGameObject player, int x, int y,
			float position) {
		player.setStartPosition(x, y);
		player.jumpToStartPosition();
		items.add(Math.round(items.size() * position), player);
		this.player = player;
	}

	/**
	 * Sets the background image of the current view.
	 * 
	 * @param backgroundImage
	 *            The name of the background image that will be set
	 */
	public final void setBackground(String backgroundImage) {
		view.setBackgroundImage(backgroundImage);
	}

	/**
	 * Set the zoom factor for the viewport. Always set higher than 1. 2 = 200%.
	 * 0.3=30%.
	 * 
	 * @param zoomFactor
	 */
	public final void setZoomFactor(float zoomFactor) {
		view.setZoomFactor(zoomFactor);
	}

	/** clears the background Image so only the back ground color will show */
	public final void clearBackgroundImage() {
		view.setBackgroundImage(null);
	}

	/**
	 * Get all instances of the specified type that are currently in the game
	 * 
	 * @param type
	 *            the class name with .class behind it.
	 * @return an arraylist containing all gameObjects of the specified type.
	 */
	public final <T> Vector<GameObject> getItemsOfType(Class<T> type) {
		Vector<GameObject> gameobjects = new Vector<GameObject>();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getClass() == type) {
				gameobjects.add(items.get(i));
			}
		}
		return gameobjects;
	}

	/**
	 * This function finds gameObjects inside a given rectangle.
	 * 
	 * @param rectangle
	 *            the rectangle specified in a left, top to bottom,right
	 *            location
	 * @return the list of items it found at the location the size is zero when
	 *         nothing is found.
	 */
	public final Vector<GameObject> findItemAt(Rect rectangle) {
		Vector<GameObject> foundItems = new Vector<GameObject>();

		for (int i = 0; i < items.size(); i++) {
			if (rectangle.intersect((items.get(i).position))) {
				foundItems.add(items.get(i));
			}
		}
		return foundItems;
	}

	/**
	 * Gets the screen Width of the telephone this value may change when
	 * flipping the telephone.
	 * 
	 * @return the screen width.
	 */
	public static final int getScreenWidth() {
		return screenWidth;

	}

	/**
	 * Gets the screen height of the telephone this value may change when
	 * flipping the telephone.
	 * 
	 * @return the screen height.
	 */
	public static final int getScreenHeight() {

		return screenHeight;

	}

	private final void checkScreenOrientation() {
		if (OnScreenButtons.use) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			if (landscape) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
	}

	/**
	 * Set landscape mode or normal mode on android device
	 * 
	 * @param landscape
	 *            true if landscape screen is required, false if not
	 */
	public final void setScreenLandscape(boolean landscape) {
		this.landscape = landscape;
	}

	/**
	 * Get Interface to global information about an application environment.
	 * This is an abstract class whose implementation is provided by the Android
	 * system. It allows access to application-specific resources and classes,
	 * as well as up-calls for application-level operations such as launching
	 * activities, broadcasting and receiving intents, etc.
	 * 
	 * @return appcontext
	 */
	public static Context getAppContext() {
		return appContext;
	}

	/**
	 * <b> DO NOT CALL THIS METHOD </b>
	 * 
	 * @return
	 */
	public static View getAppView() {
		return view;
	}

	private void registerMotionSensor() {
		if (MotionSensor.use) {
			sensor.registerListener();
		}
	}

	private void unregisterMotionSensor() {
		if (MotionSensor.use) {
			sensor.unregisterListener();
		}
	}

	/**
	 * Set the background color. Use Color class from Android. Example:
	 * Color.Red/Color.Blue/Color.Cyan.
	 * 
	 * @param backgroundColor
	 */
	public void setBackgroundColor(int backgroundColor) {
		GameView.BACKGROUND_COLOR = backgroundColor;
	}

	/**
	 * Return false before the IAlarm object is destroyed for correct garbage
	 * collection of the alarms.
	 * 
	 * When the IAlarm object is destroyed, the alarms will still exists. This
	 * causes a problem because the alarms prevent the garbage collector to
	 * clear up the IAlarm.
	 * <p>
	 * When you want to delete a IAlarm, let this method return 'false' before
	 * you delete the IAlarm.
	 * <p>
	 * <b> NOTE: GameObjects will handle this by itself! If you really want your
	 * own implementation, override this method. </b>
	 * 
	 * @return False if the alarms can be deleted.
	 */
	public boolean alarmsActiveForThisObject() {
		return true;
	}

	/**
	 * Pause the game (stop the gamethread)
	 */
	public void pause() {
		gameloop.setRunning(false);
	}

	/**
	 * Resume the game when it had been paused.
	 */
	public void resume() {
		gameThread = new Thread(gameloop);
		view.setGameThread(gameThread);
		gameThread.start();
		gameloop.setRunning(true);
	}

	/**
	 * Shows the onscreen keyboard. <b>Function is unstable around devices! Use
	 * with care!</b>
	 */
	public void showKeyboard() {
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

}
