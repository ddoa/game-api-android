package com.android.vissenspel;

import android.gameengine.icadroids.objects.*;

public class Strawberry extends GameObject 
{
	private int points;
    private Vissenkom mygame;
    
	public Strawberry(Vissenkom spel)
	{ 	
		mygame = spel;
		setSprite("strawberry");
	    points = 50;
	}   
		
	public int getPoints()
	{
	    return points;
	}
	
}
