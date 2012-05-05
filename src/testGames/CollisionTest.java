package testGames;

import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import testGames.gameEngineTest.DebugEngine;


public class CollisionTest extends DebugEngine {
	MoveableGameObject go = new MoveableGameObject();
	
	public CollisionTest() {
		go.setSprite("nyan");
		addGameObject(go, 100, 100);
	}

}
