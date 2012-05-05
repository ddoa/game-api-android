package testGames;

import testGames.gameEngineTest.DebugEngine;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.input.TouchInput;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.objects.collisions.BoundingCircle;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.gameengine.icadroids.renderer.GameView;
import android.gameengine.icadroids.renderer.Viewport;
import android.gameengine.icadroids.tiles.GameTiles;
import android.graphics.Color;
import android.util.Log;

/**
 * Deze test test de Viewport, OnscreenButtons, en GameTiles
 * 
 * @author Edward van Raak
 */
public class testGame extends DebugEngine {

	Sprite tile1;
	Sprite tile2;
	Sprite tile3;
	BoundingCircle bounding1;
	BoundingCircle bounding2;
	MoveableGameObject testObject1;
	MoveableGameObject testObject2;
	MoveableGameObject testObject3;
	MoveableGameObject testObject4;
	GameObject roel;		
	int angle = 90;
	int gravity = 0;
	
	public testGame() {
		super();
		// TouchInput.USE_TOUCH_INPUT = true;
	//	GameTiles.debugMode = false;
		//MoveableGameObject hoi = new MoveableGameObject();	
		testObject2 = new Player();
		testObject3 = new MoveableGameObject();
		testObject1 = new MoveableGameObject();
		//bounding1 = new BoundingCircle(hoi, 300, 0, 0);
		bounding2 = new BoundingCircle(6, 0, 0);		
		//showKeyboard();	
		//hoi5.startAnimate(16);
		//Viewport.useViewport = true;
		//hoi.setDirection(46);
		//;p;
		addPlayer(testObject2, 256, 256);
		testObject2.setY(236);
		testObject2.setX(456);
		testObject2.setSprite("fishframes");
		testObject2.setSpeed(0);
		testObject2.setDirection(47);	
	}

	@Override
	public void initialize() {
		super.initialize();
		setBackground("bg");
		OnScreenButtons.use = true;
		OnScreenButtons.feedback = true;
		OnScreenButtons.opacity = 195; //TODO: Opacity uses A LOT of cpu, I mean A LOOOOOT
		tile1 = new Sprite();
		tile2 = new Sprite();
		tile3 = new Sprite();
		tile1.loadSprite("tile5");
		tile2.loadSprite("tile6");
		tile3.loadSprite("tile4");		
		GameView.BACKGROUND_COLOR = Color.CYAN;

	//	GameTiles.tileTypes = new Sprite[] { tile1, tile2, tile3, tile1, tile1, tile1, tile2, tile3, tile1, tile1};
		//GameTiles.tileSize = 64;
		byte Map[][] = {
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},	
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2, 3,-1,-1,-1,-1,-1,-1,-1, 3,-1,-1,-1,-1, 3,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 2},
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},	
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
				};		
		//addTileMap(Map, 0, 0);
		//GameTiles.changeTile(1, 1, 1);
		startGame();
	}

	@Override
	public void update() {			 
		super.update();
	}
	
}
