package testGames;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.engine.GameTiles;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.input.TouchInput;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.objects.collisions.BoundingCircle;
import android.gameengine.icadroids.objects.graphics.Sprite;
import android.gameengine.icadroids.renderer.GameView;
import android.gameengine.icadroids.renderer.Viewport;
import android.graphics.Color;
import android.util.Log;

/**
 * Deze test test de functionaliteiten van de viewport, de gametiles, de
 * GameEngine, onscreenButtons en de Sprite.
 * 
 * @author Edward van Raak
 */
public class AndroidCraft_demo extends GameEngine {

	Sprite tile1;
	Sprite tile2;
	Sprite tile3;
	Sprite tile4;
	MoveableGameObject player;

	public AndroidCraft_demo() {
		super();
		GameTiles.debugMode = false;
		player = new Player();
		addPlayer(player, 256, 256);
		player.setY(236);
		player.setX(456);
		player.setSprite("nyan");
		player.setSpeed(0);
		player.setDirection(47);
	}

	@Override
	public void initialize() {
		super.initialize();
		Viewport.useViewport = true;
		setBackground("bg");
		OnScreenButtons.use = true;
		OnScreenButtons.feedback = true;
		OnScreenButtons.opacity = 195;
		tile1 = new Sprite();
		tile2 = new Sprite();
		tile3 = new Sprite();
		tile4 = new Sprite();
		tile1.loadSprite("tile6");
		tile2.loadSprite("tile4");
		tile3.loadSprite("tile5");
		GameView.BACKGROUND_COLOR = Color.BLACK;
		GameTiles.tileTypes = new Sprite[] { tile1, tile2, tile3, tile1, tile1,
				tile1, tile2, tile3, tile1, tile1 };
		GameTiles.tileSize = 64;
		byte Map[][] = {
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 3, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, 3, 3, -1, -1, -1, -1, 3, 3, -1, -1, -1, -1, 3,
						3, 3, 3, 3, -1, -1, -1, 3, -1, 3, -1, -1, 2, 2, 2, 2,
						2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, 3, -1, 3, -1, -1, -1, 3, -1, 3, -1, -1, -1, 3,
						-1, -1, -1, 3, -1, -1, 3, -1, -1, -1, 3, -1, 2, 2, 2,
						2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, 3, -1, -1, 3, -1, -1, 3, -1, -1, 3, -1, -1, 3,
						-1, -1, -1, 3, -1, -1, 3, -1, -1, -1, 3, -1, 2, 2, 2,
						2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, 3, -1, -1, 3, -1, -1, 3, -1, -1, 3, -1, -1, 3,
						-1, -1, -1, 3, -1, -1, 3, 3, 3, 3, 3, -1, 2, 2, 2, 2,
						2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, 3, -1, -1, 3, -1, -1, 3, -1, -1, 3, -1, -1, 3,
						-1, -1, -1, 3, -1, -1, 3, -1, -1, -1, 3, -1, 2, 2, 2,
						2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, 3, -1, 3, -1, -1, -1, 3, -1, 3, -1, -1, -1, 3,
						-1, -1, -1, 3, -1, -1, 3, -1, -1, -1, 3, -1, 2, 2, 2,
						2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, 3, 3, -1, -1, -1, -1, 3, 3, -1, -1, -1, -1, 3,
						3, 3, 3, 3, -1, -1, 3, -1, -1, -1, 3, -1, 2, 2, 2, 2,
						2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
						2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }, };
		addTileMap(Map, 0, 0);
		startGame();
	}

}
