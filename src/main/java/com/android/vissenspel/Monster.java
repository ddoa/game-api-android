package com.android.vissenspel;

import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.objects.collisions.ICollision;
import android.gameengine.icadroids.objects.collisions.TileCollision;

import java.util.List;

/**
 * Monster is a MoveableGameObject that can chase any other MoveableGameObject
 * In 'Vissenkom', it will chase 'Vis'.
 *
 * @author Paul Bergervoet
 */
public class Monster extends MoveableGameObject implements ICollision {

    /**
     * counts time (that is calls on update()). Using the counter, we can create
     * behaviour at certain updates only, instead of always.
     */
    private int timeCounter;

    /**
     * The MoveableGameObject to be chased
     */
    private MoveableGameObject target;

    /**
     * Create a Monster
     *
     * @param target the MoveableGameObject to be chased
     */
    public Monster(MoveableGameObject target) {
        this.target = target;
        setSprite("alien");
        this.timeCounter = 0;
        setDirectionSpeed(225, 4);
    }

    /**
     * update: change direction to target every 4th step only.
     *
     * @see android.gameengine.icadroids.objects.MoveableGameObject#update()
     */
    @Override
    public void update() {
        super.update();

        timeCounter++;
        if (timeCounter % 4 == 0) {
            this.moveTowardsAPoint(target.getCenterX(), target.getCenterY());
        }
    }

    /**
     * Tile collision: monster bounces off all tiles, so use first collision
     *
     * @see android.gameengine.icadroids.objects.collisions.ICollision#collisionOccurred(java.util.List)
     */
    @Override
    public void collisionOccurred(List<TileCollision> collidedTiles) {
        bounce(collidedTiles.get(0));
    }

}
