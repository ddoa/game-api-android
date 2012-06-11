package testGames;

import java.util.List;
import java.util.Random;

import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.tiles.Tile;

/**
 * Deze player test de functies van de OnScreenbuttons en de functies van
 * MoveableGameObject.
 * 
 * @author Edward van Raak
 * 
 */
public class Player extends MoveableGameObject {

	Random rd = new Random();

	@Override
	public void update() {

		super.update();

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
			 setDirectionSpeed(75, 8);
		}
		if (OnScreenButtons.button2) {
			setPosition(150, 150);
			// jumpToStartPosition();
		}
	}

	@Override
	public void collisionOccurred(List<Tile> collidedTiles) {

		// System.out.println(collidedTiles.size() +
		// " collision(s) detected on: "
		// + calculateCollisionSide(collidedTiles.get(0)));

		boolean collisionHorizontal = false;
		boolean collisionVertical = false;

		for (int i = 0; i < collidedTiles.size(); i++) {
			int collisionSide = getCollisionSide(collidedTiles.get(i));

			collidedTiles.get(i).setTileType(1);

			if (collisionSide == 0 || collisionSide == 2) {
				collisionVertical = true;
			} else {
				collisionHorizontal = true;
			}

		}
		if (collisionHorizontal) {

			moveUpToTileSide(collidedTiles.get(0));

			 reverseHorizontalDirection();
		}
		if (collisionVertical) {

			moveUpToTileSide(collidedTiles.get(0));

			 reverseVerticalDirection();
		}

	}

}
