package com.android.vissenspel;

import java.util.ArrayList;
import java.util.List;

import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.input.TouchInput;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.gameengine.icadroids.objects.collisions.ICollision;
import android.gameengine.icadroids.objects.collisions.TileCollision;
import android.util.Log;

public class Vis extends MoveableGameObject implements ICollision {

	private int score;
	
	public Vis() {
		setSprite("fishframes");
		startAnimate(36);
		
		setSpeed(6);
		setDirection(90);
		
		score = 0;
		
		TouchInput.use = true;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		
		ArrayList<GameObject> gebotst = getCollidedObjects();
		if ( gebotst != null )
		{
			for ( GameObject g: gebotst )
			{
				if ( g instanceof Strawberry )
				{
					score = score + ((Strawberry)g).getPoints();
					Log.d("hapje!!!", "score is nu "+score);
					g.deleteThisGameObject();
				}
			}
		}
	
		if(TouchInput.onPress){
			setSpeed(6);
			moveTowardsAPoint(TouchInput.xPos, TouchInput.yPos);
		}
	}
	
	@Override
	public void collisionOccurred(List<TileCollision> collidedTiles)
	{
		moveUpToTileSide(collidedTiles.get(0));
		//setSpeed(0);
	}
	
	
	
	/*
	System.out.println("--------");
	for (TileCollision tc: collidedTiles )
	{
		System.out.println("side: "+tc.collisionSide+", x,y: "+tc.theTile.getTileNumberX()+", "+tc.theTile.getTileNumberY()
				+" type: "+tc.theTile.getTileType());
	}
	*/

}
