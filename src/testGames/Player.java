package testGames;

import java.util.List;
import java.util.Random;

import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.input.TouchInput;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.objects.collisions.ICollision;
import android.gameengine.icadroids.objects.collisions.TileCollision;
import android.gameengine.icadroids.tiles.Tile;

/**
 * Deze player test de functies van de OnScreenbuttons en de functies van
 * MoveableGameObject.
 * 
 * @author Edward van Raak
 * 
 */
public class Player extends MoveableGameObject implements ICollision{

	Random rd = new Random();

	@Override
	public void update() {

		super.update();

		/*
		if (OnScreenButtons.dPadUp) {
			movePlayer(0, -15);
			// GameTiles.DebugMode = true;
			// setDirectionSpeed(0, 5);
		}
		if (OnScreenButtons.dPadDown) {
			movePlayer(0, 15);
			// setDirectionSpeed(180, 5);
		}
		if (OnScreenButtons.dPadRight) {
			movePlayer(15, 0);
			// setDirectionSpeed(90, 5);
		}
		if (OnScreenButtons.dPadLeft) {
			movePlayer(-15, 0);
<<<<<<< HEAD
			// setDirectionSpeed(270, 5);
=======
			// setDirectionSpeed(75, 32);
>>>>>>> a291106... Unused Tiles are now Null, debug info can be switched off
		}
		if (OnScreenButtons.button2) {
			setPosition(150, 150);
			// jumpToStartPosition();
		}*/
		if(TouchInput.onPress){
			setSpeed(12);
			// no correction for translate to game Pos
			moveTowardsAPoint(TouchInput.xPos, TouchInput.yPos);
		}
	}

	@Override
	public void collisionOccurred(List<TileCollision> collidedTiles) {
		
		moveUpToTileSide(collidedTiles.get(0));
		
		for (TileCollision tileCollision : collidedTiles) {
			tileCollision.theTile.setTileType(1);
		}
		
		for (TileCollision tc: collidedTiles )
		{
			System.out.println("side: "+tc.collisionSide+", x,y: "+tc.theTile.getTileNumberX()+", "+tc.theTile.getTileNumberY()
					+" type: "+tc.theTile.getTileType());
		}

		Tile firsttile = collidedTiles.get(0).theTile;
		firsttile.setTileType(1);
		bounce(collidedTiles.get(0));
		
	}

}
