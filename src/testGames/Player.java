package testGames;

import android.gameengine.icadroids.engine.GameTiles;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.objects.MoveableGameObject;

/**
 * Deze player test de functies van de OnScreenbuttons en de functies van
 * MoveableGameObject.
 * 
 * @author Edward van Raak
 * 
 */
public class Player extends MoveableGameObject {

	int health = 0;

	@Override
	public void intialize() {
		super.intialize();
	}

	@Override
	public void update() {
		super.update();
		health++;
		if (OnScreenButtons.dPadUp) {
			movePlayer(0, -15);
			// GameTiles.DebugMode = true;
		}
		if (OnScreenButtons.dPadDown) {
			movePlayer(0, 15);

		}
		if (OnScreenButtons.dPadRight) {
			movePlayer(15, 0);
		}
		if (OnScreenButtons.dPadLeft) {
			movePlayer(-15, 0);
		}
		if (OnScreenButtons.button1) {
			GameTiles.debugMode = false;
		}
		if (OnScreenButtons.button2) {
			GameTiles.debugMode = true;
		}
		if (collidedBottom(1)) {
			moveUpToTileSide(1);
		}
		if (collidedTop(2)) {
			moveUpToTileSide(2);
		}
		if (collidedBottom(2)) {
			moveUpToTileSide(2);
		}
		if (collidedLeft(2)) {
			moveUpToTileSide(2);
		}
		if (collidedRight(2)) {
			moveUpToTileSide(2);
			System.out.println("XE: RIGHT");
		}
	}

}
