package com.android.vissenspel;

import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.objects.collisions.TileCollision;
import android.gameengine.icadroids.tiles.Tile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Description for the class MonsterTest:
 * <p/>
 * Example usage:
 * <p/>
 * <pre>
 *
 * </pre>
 *
 * @author mdkr
 * @version Copyright (c) 2012 HAN University, All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class MonsterTest {
    private Monster monster;

    @Before
    public void setUp()
    {
        MoveableGameObject mockedTarget = mock(MoveableGameObject.class);
        monster = new Monster(mockedTarget);
    }

    @Test
    public void testUpdate() throws Exception {
        int xPos = monster.getX();
        int yPos = monster.getY();

        for (int i=0;i<4;i++)
            monster.update();
        assertNotEquals(monster.getX(), xPos);
        assertNotEquals(monster.getY(), yPos);
    }

    @Test
    public void testCollisionOccurred() throws Exception {
        double ySpeed = monster.getySpeed();
        List<TileCollision> mockedTileCollision = new ArrayList<TileCollision>() {{
            Tile mockTile = mock(Tile.class);
            when(mockTile.getTileY()).thenReturn(10);
            add(new TileCollision(mockTile, 0));
        }};
        monster.collisionOccurred(mockedTileCollision);
        assertEquals(monster.getySpeed(), -ySpeed,0.0);
    }
}
