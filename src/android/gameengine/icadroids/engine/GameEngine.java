package android.gameengine.icadroids.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
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
import android.gameengine.icadroids.sound.GameSound;
import android.gameengine.icadroids.sound.MusicPlayer;
import android.gameengine.icadroids.tiles.GameTiles;
import android.graphics.Point;
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
	 * The player that the viewport follows
	 */
	private MoveableGameObject player;

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
	 * The width and height of the game world
	 */
	private int mapWidth, mapHeight;

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
	 * A vectorlist that holds all the newly created GameObjects during this
	 * cycle of the game loop. At the end of the cycle, all items in this list
	 * will be moved to the items-list and the object become active
	 */
	public static Vector<GameObject> newItems;
	/**
	 * A vectorlist that holds all the active alarms. Can be used if you
	 * manually want to delete/change alarms.
	 */
	public static Vector<Alarm> gameAlarms;
	/**
	 * Holds context of the application
	 */
	private static Context appContext;
	/**
	 * The main thread of the game, containing the game loop
	 */
	private GameThread gameThread;
	/**
	 * Sets the mobile device to landscape view if set to true
	 */
	private boolean landscape = true;

	/**
	 * boolean indicating if the game world has a tilemap.
	 */
	private boolean tileBasedMap = false;

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
	 * Random generator, used for generating random positions in the game world
	 */
	private Random random = new Random();

	/**
	 * The GameEngine forms the core of the game by controlling the gameloop and
	 * the render operations. All games must extend this base class.
	 * <br />
	 * You can override the constructor in your game, to create java-objects like
	 * ArrayLists and initialize data-variables here. <em>Do not initialize your game
	 * here (GameObjects, tiles, etc)</em>. These GameObjects use Android
	 * resources which are not yet available at start up.<br />
	 * Initialize your game by overriding the initialize()-method.
	 * 
	 * @see android.gameengine.icadroids.engine.GameEngine#initialize()
	 */
	public GameEngine() {
		items = new Vector<GameObject>();
		newItems = new Vector<GameObject>();
		gameAlarms = new Vector<Alarm>();
		tileBasedMap = false;
	}

	/***
	 * <b>Do NOT call this method.</b>
	 * <p>
	 * Called when activity is started. Initialise GameEngine objects and set
	 * options to be used by the GameEngine.<br />
	 * This method is part of the Android Activity lifecycle which is managed by
	 * GameEngine itself.
	 * </p>
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

		gameView = new GameView(this);
		mainView = new FrameLayout(this);
		mainView.addView(gameView);
		addDashboard();

		// create GameThread in one place: the startThread method

		touch = new TouchInput();
		

		GameSound.initSounds(getAppContext());


		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		MotionSensor.initialize( sensorManager); 

		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();
		// default Dimensions of the game world: size of screen
		setMapDimensions(getScreenWidth(), getScreenHeight());

		setContentView(mainView);
		gameView.setKeepScreenOn(true);


	}
	
	/**
	 * <b> Don't override this method, use initialize() instead</b>
	 * This method is called by the GameView to notify
	 * the gameEngine to start initializing. It will
	 * call the initialize, beforeInitialize and afterInitialize
	 * method.
	 */
	protected void initializeGameEngine(){
		beforeInitialize();
		initialize();
		afterInitialize();
	}


	/**
	 * Method called directly before the initialization
	 */
	private void beforeInitialize() {
		if (Sprite.loadDelayedSprites != null) {
			for (Sprite sprite : Sprite.loadDelayedSprites) {
				sprite.initialize();
			}
		}
		Sprite.loadDelayedSprites = null;
	}

	/**
	 * This method is like a 'postponed constructor'. It is called automatically by the 
	 * GameEngine when all necessary Android resources are ready. At that point
	 * you can set up your game.<br />
	 * You must override this method inside your game class that extends GameEngine.<br /> 
	 * Don't call this method yourself (or everything will be done twice!)<br />
	 * You can perform any initialization the game needs to before starting
	 * to run, like:
	 * <p />
	 * <ul>
	 * <li>Create instances of GameObjects.</li>
	 * <li>Create a tile environment</li>
	 * <li>Set viewport options</li>
	 * <li>Configure background image, background color, OnScreenButtons, TouchInput</li>
	 * <li>etc.</li>
	 * </ul>
	 * <p />
	 */
	protected abstract void initialize();
	
	/**
	 * Method called direct after intialization.
	 */
	private void afterInitialize() {
	
		for (GameObject item : items) {
			item.intializeGameObject();
		}
		intializeInput();		
	}

	/**
	 * Initialize the touch and/or the onScreenButtons 
	 */
	public void intializeInput() {
		if (TouchInput.use) {
			gameView.setOnTouchListener(touch);
		}
		if(OnScreenButtons.use){
			screenButtons = new OnScreenButtons(this);
		}		
	}

	/**
	 * Start the game loop.
	 */
	void startThread() {
		if ( gameThread != null ) {
			if ( gameThread.isRunning() ) {
				return;
			}
		}
		gameThread = new GameThread(this, gameView);
		gameThread.start();
		Log.d("GameThread", "Thread started");
	}

	/**
	 * <b>Do NOT call this method.</b>
	 * <p>
	 * Call the update for every GameObject added to the list.
	 */
	protected final void updateGame() {
		update();
		for (int i = 0; i < items.size(); i++) {
			if ( items.get(i).isActive() ) {
				items.get(i).update();
				calculateOutsideWorld(items.get(i));
			}
		}
		for (int i = 0; i < gameAlarms.size(); i++) {
			gameAlarms.get(i).update();
		}
		cleanupObjectlists();
	}

	/**
	 * Calculates if a GameObject is outside the world or not.
	 */
	private void calculateOutsideWorld(GameObject go) {
		if (go instanceof MoveableGameObject) {
			if (!go.isActive()) {
				return;
			}
			if (go.getX() - go.getFrameWidth() < 0 || go.getX() > mapWidth
					|| go.getY() - go.getFrameHeight() < 0
					|| go.getY() > mapHeight) {
				((MoveableGameObject) go).outsideWorld();
			}
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

	/**
	 * This method will do the actual removing and adding of GameObjects at the
	 * end of a game loop pass.
	 */
	private void cleanupObjectlists() {
		Iterator<GameObject> it = items.iterator();
		while (it.hasNext()) {
			GameObject go = it.next();
			if (!go.isActive()) {
				deleteObjectAlarms(go);
				it.remove();
			}
		}
		for (int i = 0; i < newItems.size(); i++) {
			// note: always moving the first element of newItems ensures same
			// order
			GameObject item = newItems.remove(0);
			if (item.getDepth() > 0) {
				float d = item.getDepth();
				// move index to position of first element having smaller depth
				int index = 0;
				while (index < items.size()) {
					if (items.get(index).getDepth() < d) {
						break;
					}
					index++;
				}
				items.add(index, item);
			} else {
				// just add to the back of the list
				items.add(item);
			}
		}
	}

	/***
	 * Allows the game to run logic such as updating the world, gathering input
	 * and playing audio.
	 * <p>
	 * Override this method inside your game base class that extends from
	 * GameEngine. Every pass of the game loop this method is called once.
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
	 * Add an Alarm to the list of alarms in the GameEngine. You must add an
	 * Alarm to make it 'tick', but it won't start ticking until you call
	 * startAlarm()!
	 * 
	 * @param a
	 *            The alarm to be added.
	 */
	public static void addAlarm(Alarm a) {
		gameAlarms.add(a);
	}

	/**
	 * Remove an alarm from the GameEngine. It will stop ticking.
	 * 
	 * @param a
	 *            The alarm to be deleted.
	 */
	public void deleteAlarm(Alarm a) {
		gameAlarms.remove(a);
	}

	/**
	 * Remove all Alarms for the specified GameObject, this will be called
	 * when the GameObject is removed from the Game
	 * 
	 * @param go the GameObject for which Alarms are deleted
	 */
	private void deleteObjectAlarms(GameObject go) {
		if (go instanceof IAlarm) {
			Iterator<Alarm> it = gameAlarms.iterator();
			while (it.hasNext()) {
				if ((it.next()).targets(((IAlarm) go)))
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
		// needs update?? removing all elements generally means stopping the
		// game...
		items.removeAllElements();
	}

	/**
	 * Remove all GameObject instances of given class type.
	 * 
	 * @param type
	 *            De class type of the instances to be removed
	 */
	public <T> void deleteAllGameObjectsOfType(Class<T> type) {
		for (int i = 0; i < items.size(); i++) {
			GameObject go = items.get(i);
			if (go.getClass() == type) {
				go.clearActive();
			}
		}
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 * <p>
	 * This method is part of the Android Activity lifecycle which is managed by
	 * GameEngine itself.
	 * </p>
	 */
	@Override
	protected final void onResume() {
		super.onResume();
		printDebugInfo("GameEngine", "onResume()...");
		// Note: only restart thread after a real pause, not at startup
		if ( gameThread != null ) {
			if ( gameThread.getState() == Thread.State.TERMINATED ) {
				startThread();
			}
		}	
		GameSound.resumeSounds();
		MusicPlayer.resumeAll();

		MotionSensor.handleOnResume(this); 
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 * <p>
	 * This method is part of the Android Activity lifecycle which is managed by
	 * GameEngine itself.
	 * </p>
	 */
	@Override
	protected final void onDestroy() {
		super.onDestroy();
		printDebugInfo("GameEngine", "onDestroy...");
		GameSound.cleanup();
		MusicPlayer.stop();
	}

	/**
	 * <b>DO NOT CALL THIS METHOD</b>
	 * <p>
	 * This method is part of the Android Activity lifecycle which is managed by
	 * GameEngine itself.
	 * </p>
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
		gameThread.stopRunning();
		finish();
	}

	/**
	 * Add a GameObject to the game. New GameObjects will become active in the
	 * next pass of the gameloop.<br />
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
	 * Add a GameObject to the game. New GameObjects will become active in the
	 * next pass of the gameloop. <br />
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
		gameObject.setDepth(layerposition);
		newItems.add(gameObject);
	}

	/**
	 * Add a GameObject to the game. New GameObjects will become active in the
	 * next pass of the gameloop. <br />
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
		gameObject.setDepth(layerposition);
		newItems.add(gameObject);
	}

	/**
	 * Add a GameObject to the game. New GameObjects will become active in the
	 * next pass of the gameloop. <br />
	 * 
	 * @param gameObject
	 *            The GameObject that will be added to the game. Should have either
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
	 * Set a GameTiles object as the current GameTile map.<br />
	 * Do not make a map that has parts sticking out.
	 * Use -1 to create an empty space. <br/>
	 * <b>Note<b/>: You can switch tilemaps, for instance when you move from
	 * one level to the next. Therefore the dimensions of the world are not set.
	 * Set the world dimensions yourself, to values that will make all of your
	 * tilemaps fit.
	 * 
	 * @param gameTiles
	 *            the current gameTiles object.
	 */
	protected void setTileMap(GameTiles gameTiles) {
		GameEngine.gameTiles = gameTiles;
		tileBasedMap = true;
	}

	/**
	 * Ask whether this game has a tilemap
	 * 
	 * @return boolean, true if there are tiles.
	 */
	public boolean isTileBasedMap() {
		return tileBasedMap;
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
	 * <b> DO NOT CALL THIS METHOD </b><br>
	 * 
	 * @return the gameloop
	 */
	/*public final GameThread getGameloop() {
	return gameloop;
    }*/

	/**
	 * Return the player instance.
	 * 
	 * @return the GameObject that has been set to be the playerobject
	 */
	public final MoveableGameObject getPlayer() {
		return player;
	}

	/***
	 * Name a GameObject in the role of the Player object. Note that
	 * this is relevant only for the viewport. If your game is larger than the
	 * screen, the viewport will follow the GameObject that has been set as
	 * player. <br />
	 * You have to add the GameObject to the game before assigning the player role.<br />
	 * Only one GameObject can be the player. If you call this method for a second
	 * time, the player role will switch to the newly specified object. 
	 * player.
	 * 
	 * @param player
	 *            The player that should be added to the game. The location of
	 *            this player is used by the viewport.
	 */
	public final void setPlayer(MoveableGameObject player) {
		this.player = player;
		if ( Viewport.useViewport ) {
			Viewport vp = Viewport.getInstance();
			vp.setPlayer(player);
		}
	}

	/**
	 * Sets the background image of the current view. You can indicate if the
	 * image must be scaled to fit the screen.<br />
	 * Images must be stored in the res/drawable folders of your Android
	 * project. If you have only one image, store it in the 'nodpi' folder. If
	 * you have various versions (hi-res and lo-res) for rendering on devices of
	 * varying screen sizes, use the lo- & hi-dpi folders.
	 * 
	 * @param backgroundImage
	 *            The name of the background image that will be set.
	 * @param backgroundFit
	 *            boolean, indicating if the image must be scaled to fit.
	 */
	public final void setBackground(String backgroundImage,
			boolean backgroundFit) {
		gameView.setBackgroundImage(backgroundImage);
		gameView.setBackgroundFit(backgroundFit);
	}

	/**
	 * Sets the background image of the current view. The image will not be
	 * scaled to fit the screen. The viewport will scroll over the
	 * backgroundimage, just like it does over the rest of the game.<br />
	 * Images must be stored in the res/drawable folders of your Android
	 * project. If you have only one image, store it in the 'nodpi' folder. If
	 * you have various versions (hi-res and lo-res) for rendering on devices of
	 * varying screen sizes, use the lo- & hi-dpi folders.
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
	 * <br />
	 * Zooming only works when the viewport is being used.
	 * 
	 * @param zoomFactor a float, the factor by which the view must be enlarged.
	 */
	public final void setZoomFactor(float zoomFactor) {
		// this method will pass through GameView (adjustment of the Matrix of the Canvas)
		gameView.setZoomFactor(zoomFactor);
	}

	/**
	 * When using a viewport, set the position of the player on the screen.<br />
	 * In many games, the game
	 * world is bigger than the screen. When you specify the player position,
	 * you can make the screen (viewport) move along with the player. The
	 * parameters tell how this must be done. <br />
	 * For the vpositioning (vertical adjustment) you can use
	 * Viewport.PLAYER_TOP, Viewport.PLAYER_CENTER, Viewport.PLAYER_BOTTOM or
	 * Viewport.PLAYER_NOADJUST, if you don't want adjustment of the player
	 * position in the vertical direction.<br />
	 * For the hpositioning (horizontal adjustment) you can use
	 * Viewport.PLAYER_LEFT, Viewport.PLAYER_CENTER, Viewport.PLAYER_RIGHT or
	 * Viewport.PLAYER_NOADJUST, if you don't want adjustment of the player
	 * position in the horizontal direction.<br />
	 * Note: if you position the player at one of the edges of the screen, like
	 * BOTTOM, this means that you can not move out of the screen that way, but
	 * you can move up a bit. How much that is, is specified by
	 * setPlayerPositionTolerance.
	 * 
	 * @param hpositioning
	 *            the horizontal postioning, or Viewport.PLAYER_NOADJUST if there is no horizontal
	 *            adjustment of the viewport
	 * @param vpositioning
	 *            the vertical postioning, or Viewport.PLAYER_NOADJUST if there is no vertical
	 *            adjustment of the viewport
	 */
	public final void setPlayerPositionOnScreen(int hpositioning, int vpositioning) {
		if ( Viewport.useViewport ) {
			Viewport vp = Viewport.getInstance();
			vp.setPlayerPositionOnScreen(hpositioning, vpositioning);
		}
	}

	/**
	 * Set the tolerance of the positioning of the player. When tolerance is zero, the
	 * viewport moves immediately when the player moves. When tolerance is 1,
	 * you can move to the edge of the screen before the viewport moves. Values
	 * in between result in a smaller or bigger delay before the viewport moves. <br/>
	 * Example: In a left-to right platform game, you may position the player at
	 * LEFT, CENTER. If you set the horizontal tolerance at 0.3, you may move to
	 * the right 30% of the screen before the viewport moves along. If you set
	 * vertical tolerance at 0.8, you can move 80% of the way up, before the
	 * viewport moves up also.<br />
	 * This method only has effect when you are using the viewport.
	 * 
	 * @param ht
	 *            horizontal tolerance, a value between 0 and 1
	 * @param vt
	 *            vertical tolerance, a value between 0 and 1
	 */
	public final void setPlayerPositionTolerance(double ht, double vt) {
		if ( Viewport.useViewport ) {
			Viewport vp = Viewport.getInstance();
			vp.setPlayerPositionTolerance(ht, vt);
		}
	}
	
    /**
     * Translates a given screen position to a position in the game world, taking into
     * consideration the viewport location and the zoom factor.<br />
     * Note: The x and y values are returned in a Point object, therefore they are
     * returned as ints. You can access the individual values using .x and .y.
     * 
     * @param x the screen x
     * @param y the screen y
     * @return Point, containing the x,y-position in the game world
     */
    public Point translateToGamePosition(float x, float y) {
    	if ( Viewport.useViewport ) {
    		Viewport vp = Viewport.getInstance();
    		return vp.translateToGamePosition(x, y);
    	} else {
    		return new Point((int)x, (int)y);
    	}
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
			if (Rect.intersects(rectangle, (items.get(i).position))) {
				foundItems.add(items.get(i));
			}
		}
		return foundItems;
	}

	/**
	 * Get the width of the game world
	 * 
	 * @return width, an int
	 */
	public int getMapWidth() {
		return mapWidth;
	}

	/**
	 * Get the height of the game world
	 * 
	 * @return height, an int
	 */
	public int getMapHeight() {
		return mapHeight;
	}

	/**
	 * Set the dimensions of the game world.<br />
	 * By default the dimensions will be set to the size of the screen. If you want to play
	 * a larger game world, you can set the dimensions calleing this method in initialize().<br />
	 * <b>Note</b>: You also need to set the world dimensions when you use a tile map.
	 * 
	 * @param mapWidth an int specifying the width of the game world
	 * @param mapHeight an int specifying the height of the game world
	 */
	public void setMapDimensions(int mapWidth, int mapHeight) {
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		if ( Viewport.useViewport ) {
			Viewport vp = Viewport.getInstance();
			vp.setBounds(0, 0, mapWidth, mapHeight);
		}
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
	 * @return The GameView object
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
		if ( gameThread != null ) {
			gameThread.stopRunning();
		}
	}

	/**
	 * Resume the game when it had been paused.
	 */
	public void resume() {
		startThread(); 
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
	 * @param tag
	 *            The tag (shown in Logcat)
	 * @param msg
	 *            The message you want to be displayed
	 */
	public static void printDebugInfo(String tag, String msg) {
		if (printDebugInfo) {
			Log.d(tag, msg);
		}
	}

	/**
	 * Get the first (rendered) object that is touched. Useful for checking if a
	 * gameObject is touched.
	 * 
	 * @param touchSize
	 *            The size (rectangle) around the finger that will be checked
	 *            for objects.
	 * @return The first (rendered) gameObject that is touched.
	 */
	public final GameObject getGameObjectAtTouchPosition(int touchSize) {
		Rect clickedPos = new Rect();
		clickedPos.set((int) TouchInput.xPos - touchSize, (int) TouchInput.yPos
				- touchSize, (int) TouchInput.xPos + touchSize,
				(int) TouchInput.yPos + touchSize);
		if (!findItemAt(clickedPos).isEmpty()) {
			GameObject gameObject = findItemAt(clickedPos).get(0);
			return gameObject;
		}
		return null;
	}
	/**
	 * Generates a random integer between 0 (zero) and the given range, not
	 * including range.
	 * 
	 * @param range
	 *            the maximum number that may be returned
	 * @return a pseudo-random integer
	 */
	private int random(int range) {
		return (random.nextInt() << 1 >>> 1) % range;
	}

	/**
	 * Get random x between left edge of world and right edge minus the
	 * specified width. In this way you can find a random position for an item
	 * of the given width and the item will fit completely into the world.
	 * 
	 * @param width
	 * @return a random x-position
	 */
	public int getRandomX(int width) {
		return  random(mapWidth - width);
	}

	/**
	 * Get random y between top edge of world and bottom edge minus the
	 * specified height. In this way you can find a random position for an item
	 * of the given height and the item will fit completely into the world.
	 * 
	 * @param height
	 * @return a random y-position
	 */
	public int getRandomY(int height) {
		return random(mapHeight - height);
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

}
