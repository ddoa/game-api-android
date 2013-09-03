package android.gameengine.icadroids.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;
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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

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
public abstract class GameEngine extends Activity implements SensorEventListener {
	
	public static boolean printDebugInfo = true;
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
	private static GameView gameView;
	
	/**
	 * The frame layout that contains both the game view and one
	 * or more dashboards.
	 */
	private static FrameLayout mainView;
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
	 * A vectorlist that holds all the newly created GameObjects during this cycle
	 * of the game loop. At the end of the cycle, all items in this list will be moved
	 * to the items-list and the object become active 
	 */
	public static Vector<GameObject> newItems;
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

	public static GameTiles gameTiles;
	
	/**
	 * The game dashboard. It's an Android LinearLayout (see:
	 * http://developer.android.com/reference/android/widget/LinearLayout.html)
	 * 
	 * You can add new dashboard widgets and views to a dashboard by
	 * calling the addChild method with the widget to add as a parameter.
	 * To manipulate a dashboard widget, either call its run method
	 * if it's one of the widgets in android.gameengine.icadroids.dashboard
	 * or, if it's something you created yourself, create a runnable and run
	 * it on the UI thread using the runOnUiThread method of the
	 * GameEngine object (see:
	 * http://developer.android.com/reference/android/app/Activity.html#runOnUiThread(java.lang.Runnable)
	 * )
	 */
	public LinearLayout dashboard;
	
	/**
	 * It is possible to have multiple dashboards, stacked
	 * on top of each other. this.dashboard always holds the top
	 * level dashboard.
	 */
	public ArrayList<LinearLayout> dashboards = new ArrayList<LinearLayout>();

	/**
	 * The frame layout that contains both the game view and one
	 * or more dashboards.
	 */

	/**
	 * The GameEngine forms the core of the game by controlling the gameloop and
	 * the render operations. All games must extend this base class.
	 * 
	 * @author Edward van Raak, Roel van Bergen, Leon van Kleef, Lex van de
	 *         laak, Bas van der Zandt
	 * @version 1.0, January 10, 2012
	 */
	public GameEngine() {
		items = new Vector<GameObject>();
		newItems = new Vector<GameObject>();
		gameAlarms = new Vector<Alarm>();
	}

	/***
	 * <b>Do NOT call this method.</b>
	 * <p>
	 * Called when activity is started. Initialise GameEngine objects and set
	 * options to be used by the GameEngine.<br />
	 * This method is part of the Android Activity lifecycle which is managed
	 * by GameEngine itself.</p>
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkScreenOrientation();
		printDebugInfo("GameEngine", "oncreate....");
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

		gameView = new GameView(this, gameThread);
		mainView = new FrameLayout(this);
		mainView.addView(gameView);
		addDashboard();
		
		gameloop.setView(gameView);
		


		touch = new TouchInput(gameView);
		screenButtons = new OnScreenButtons();
		GameSound.initSounds(getAppContext());
		
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		MotionSensor.initialize( sensorManager);
		

		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();

		setContentView(mainView);
		gameView.setKeepScreenOn(true);

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
		printDebugInfo("GameEngine", "Intializing...");

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

	/**
	 * Initialize the Listener for the screen (general touch OR screenButtons)
	 */
	protected void intializeTouch() {
		if (TouchInput.use) {
			gameView.setOnTouchListener(touch);
		} else if (OnScreenButtons.use) {
			Log.d("ButtonEnabled", "USING ON SCREEN BUTTONS");
			gameView.setOnTouchListener(screenButtons);
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
			if (items.get(i).isActive()) {
				items.get(i).update();
			}
		}
		for (int i = 0; i < gameAlarms.size(); i++) {
			gameAlarms.get(i).update();
		}
		cleanupObjectlists();
	}
	
	/**
	 * This method will do the actual removing and adding of GameObjects at the end of
	 * a gameloop pass. 
	 */
	private void cleanupObjectlists()
	{
		Iterator<GameObject> it = items.iterator();
		while ( it.hasNext() )
		{
			GameObject go = it.next();
			if (  !go.isActive() )
			{
				deleteObjectAlarms(go);
				it.remove();
			}
		}
		for ( int i = 0; i < newItems.size(); i++ )
		{
			// insert object 0 in the items-list (for this, object must know layer-info)
			// layer gedoe moet er nog bij....
			items.add(newItems.remove(0));
			// note: always moving the first element of newItems ensures same order
		}
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 * <p>This method is part of the Android sensor mechanism. It is used
	 * by GameEngine itself.</p>
	 */
	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Not used.
	}
	
	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 * <p>This method is part of the Android sensor mechanism. It is used
	 * by GameEngine itself.</p>
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		MotionSensor.handleSensorChange(event);
	}
	
	
	/***
	 * Allows the game to run logic such as updating the world, gathering input
	 * and playing audio.
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

	/**
	 * Add an Alarm to the list of alarms in the GameEngine. 
	 * You must add an Alarm to make it 'tick', but it won't start ticking until you call
	 * startAlarm()!
	 * 
	 * @param a
	 * 		The alarm to be added.
	 */
	public static void addAlarm(Alarm a)
	{
		gameAlarms.add(a);
	}
	
	/**
	 * Remove an alarm from the GameEngine.
	 * It will stop ticking.
	 * 
	 * @param a
	 * 		The alarm to be deleted.
	 */
	public void deleteAlarm(Alarm a)
	{
		gameAlarms.remove(a);
	}
	
	private void deleteObjectAlarms(GameObject go)
	{
		if ( go instanceof IAlarm )
		{		
			Iterator<Alarm> it = gameAlarms.iterator();
			while ( it.hasNext() )
			{			
				if ( (it.next()).targets( ((IAlarm)go) ) )
					it.remove();
			}
		}
	}
	
	/**
	 * Removes all alarm instances
	 */
	public void deleteAllAlarms() {
		gameAlarms.removeAllElements();
	}

	/***
	 * Delete a GameObject from the game.
	 * 
	 * @param gameObject
	 *            The GameObject instance to be removed
	 */
	public final void deleteGameObject(GameObject gameObject) {
		gameObject.clearActive();
	}

	/***
	 * Delete all GameObjects from the game
	 */
	public final void deleteAllGameObjects() {
		// needs update?? removing all elements generally means stopping the game...
		items.removeAllElements();
	}

	/**
	 * Remove all GameObject instances of given class type.
	 * 
	 * @param type
	 *            De class type of the instances to be removed
	 */
	public <T> void deleteAllGameObjectsOfType(Class<T> type) 
	{
		for (int i = 0; i < items.size(); i++)
		{
			GameObject go = items.get(i);
			if ( go.getClass() == type)
			{
				go.clearActive();
			}
		}
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 * <p>This method is part of the Android Activity lifecycle which is managed
	 * by GameEngine itself.</p>
	 */
	@Override
	protected final void onResume() {
		super.onResume();
		//printDebugInfo("GameEngine", "onResume()...");
		gameloop.setRunning(true);
		if (gameThread.getState() == Thread.State.TERMINATED) {
			printDebugInfo("GameEngine", "thread terminated, starting new thread");
			gameThread = new Thread(gameloop);
			gameView.setGameThread(gameThread);
			gameloop.setRunning(true);
		}
		GameSound.resumeSounds();
		MusicPlayer.resumeAll();
		
		MotionSensor.handleOnResume(this);

	}
	
	// TODO
	TextView testTextView;

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 * <p>This method is part of the Android Activity lifecycle which is managed
	 * by GameEngine itself.</p>
	 */
	@Override
	protected final void onDestroy() {
		super.onDestroy();	
		printDebugInfo("GameEngine", "onDestroy...");
		if (UPDATE_LOOP_ON) {
			updateLoop.setRunning(false);
		}
		GameSound.cleanup();
		MusicPlayer.stop();
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 * <p>This method is part of the Android Activity lifecycle which is managed
	 * by GameEngine itself.</p>
	 */
	@Override
	protected final void onPause() {
		super.onPause();
		printDebugInfo("GameEngine", "OnPause...");
		pause();
		GameSound.pauseSounds();
		MusicPlayer.pauseAll();
		
		MotionSensor.handleOnPause(this);
	}

	/**
	 * End the game and close the application.
	 */
	protected final void endGame() {
		gameloop.setRunning(false);
		finish();
	}

	/**
	 * Add a GameObject to the game. New GameObjects will become active in
	 * the next pass of the gameloop.<br />
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
		newItems.add(gameObject);
	}

	/**
	 * Add a GameObject to the game. New GameObjects will become active in
	 * the next pass of the gameloop. <br />
	 * Layerposition may not work very well as yet, still under construction.<br />
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
		// note: store layerposition in Object for later use
		newItems.add( gameObject);
	}

	/**
	 * Add a GameObject to the game. New GameObjects will become active in
	 * the next pass of the gameloop. <br />
	 * Layerposition may not work very well as yet, still under construction.<br />
	 * 
	 * @param gameObject
	 *            The GameObject that will be added to the game. Should have
	 *            either GameObject or MovableGameObject as it's parent.
	 * @param layerposition
	 *            The layerposition when this object is drawed. <b>Between 0 and
	 *            1 (float). </b> 1 front, 0 back
	 */
	public final void addGameObject(GameObject gameObject, float layerposition) {
		//items.add(Math.round(items.size() * layerposition), gameObject);
		// note: store layerposition in Object for later use
		newItems.add(gameObject);
	}

	/**
	 * Add a GameObject to the game. New GameObjects will become active in
	 * the next pass of the gameloop. <br />
	 * 
	 * @param gameObjectThe
	 *            GameObject that will be added to the game. Should have either
	 *            GameObject or MovableGameObject as it's parent.
	 */
	public final void addGameObject(GameObject gameObject) {
		newItems.add(gameObject);
	}

	/**
	 * Add a collection of GameObjects. Useful if you want to add a lot of
	 * objects.
	 * 
	 * @param objectList
	 */
	public final void addListOfObject(Collection<GameObject> objectList) {
		newItems.addAll(objectList);
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
		if ( gameView != null ) {
		    gameView.setTileBasedMap(true);
		}
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
	 * Add a GameObject to the Game in the role of the Player object. Note that
	 * this is relevant only for the viewport. If your game is larger than the screen,
	 * the viewport will follow the GameObject that has been added as player.
	 * <br />
	 * This method should be called during the initializing phase. Only add 1
	 * player.
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
		addGameObject(player, x, y);
		this.player = player;
	}

	/***
	 * Add a GameObject to the Game in the role of the Player object. Note that
	 * this is relevant only for the viewport. If your game is larger than the screen,
	 * the viewport will follow the GameObject that has been added as player.
	 * <br />
	 * This method should be called during the initializing phase. Only add 1
	 * player.
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
		addGameObject(player, x, y, position);
		this.player = player;
	}

	/**
	 * Sets the background image of the current view. You can indicate
	 * if the image must be scaled to fit the screen.<br />
	 * Images must be stored in the res/drawable
	 * folders of your Android project. If you have only one image, store it in
	 * the 'nodpi' folder. If you have various versions (hi-res and lo-res) for
	 * rendering on devices of varying screen sizes, use the lo- & hi-dpi folders.
	 * 
	 * @param backgroundImage
	 *            The name of the background image that will be set.
	 * @param backgroundFit
	 * 				boolean, indicating if the image must be scaled to fit.
	 */
	public final void setBackground(String backgroundImage, boolean backgroundFit) {
		gameView.setBackgroundImage(backgroundImage);
		gameView.setBackgroundFit(backgroundFit);
	}
	
	/**
	 * Sets the background image of the current view. 
	 * The image will not be scaled to fit the screen. The viewport
	 * will scroll over the backgroundimage, just like it does over 
	 * the rest of the game.<br />
	 * Images must be stored in the res/drawable
	 * folders of your Android project. If you have only one image, store it in
	 * the 'nodpi' folder. If you have various versions (hi-res and lo-res) for
	 * rendering on devices of varying screen sizes, use the lo- & hi-dpi folders.
	 * 
	 * @param backgroundImage
	 *            The name of the background image that will be set
	 */
	public final void setBackground(String backgroundImage) {
		gameView.setBackgroundImage(backgroundImage);
		gameView.setBackgroundFit(false);
	}

	/**
	 * Set the zoom factor for the viewport. Always set higher than 1. 2 = 200%.
	 * 
	 * @param zoomFactor
	 */
	public final void setZoomFactor(float zoomFactor) {
		gameView.setZoomFactor(zoomFactor);
	}

	/** 
	 * Clears the background Image so only the background color will show 
	 */
	public final void clearBackgroundImage() {
		gameView.setBackgroundImage(null);
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
		return gameView;
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
		gameView.setGameThread(gameThread);
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
	
	/**
	 * Print information into the Logcat, for debugging purposes.
	 * 
	 * @param tag	The tag (shown in Logcat)
	 * @param msg	The message you want to be displayed
	 */
	public static void printDebugInfo(String tag, String msg){
		if(printDebugInfo){
			Log.d(tag, msg);
		}		
	}
	/**
	 * Get the first (rendered) object that is touched. Useful for checking
	 * if a gameObject is touched.
	 * @param touchSize The size (rectangle) around the finger that will be checked
	 *  for objects.
	 * @return The first (rendered) gameObject that is touched.
	 */
	public final GameObject getGameObjectAtTouchPosition(int touchSize){
		Rect clickedPos = new Rect();
		clickedPos.set((int)TouchInput.xPos - touchSize,(int) TouchInput.yPos - touchSize,
				(int) TouchInput.xPos + touchSize,(int) TouchInput.xPos + touchSize);
		if (!findItemAt(clickedPos).isEmpty()) {
			GameObject gameObject = findItemAt(clickedPos).get(0);
			return gameObject;
	}
		return null;
	}
	
	/**
	 * Add a new dashboard. The new dashboard will be placed in
	 * front of all other items on the screen.
	 */
	public void addDashboard() {
		
		LinearLayout newDashboard = new LinearLayout(this);
		newDashboard.setLayoutParams(
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		newDashboard.setOrientation(LinearLayout.HORIZONTAL);
		
		mainView.addView(newDashboard);
		dashboards.add(newDashboard);
		dashboard = newDashboard;
	}
	
	/**
	 * Add an item to the dashboard that's currently on top.
	 * @param v the view to add to the dashboard.
	 */
	public void addToDashboard(View v) {
		LinearLayout dashboard = this.dashboard;
		addToDashboard(dashboard, v);
	}

	/**
	 * Add an item to a specific dashboard.
	 * @param dashboard
	 * @param v
	 */
	public void addToDashboard(LinearLayout dashboard, View v) {
		final LinearLayout dashboardToUse = dashboard;
		final View viewToUse = v;
		runOnUiThread(new Runnable(){
			public void run() {
				dashboardToUse.addView(viewToUse);
			}
		});
	}
	
	/*
	public void addToDashboard(DashboardTextView dashboardTextView) {
		View v = (View) dashboardTextView
	}
	*/
}
