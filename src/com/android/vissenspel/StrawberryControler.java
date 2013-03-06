package com.android.vissenspel;

import android.gameengine.icadroids.alarms.*;
import android.util.Log;

/**
 * Een Controler die met enige regelmaat een aardbei in de Vissenkom dropt.
 * De vis is daar verzot op!
 * 
 * @author Paul Bergervoet
 * @version 1.0
 */

public class StrawberryControler implements IAlarm
{
	private Vissenkom mygame;
    private Alarm myAlarm;


	/**
	 * Maak een Controler
	 * 
	 * @param vg referentie naar het spel zelf
	 */
	public StrawberryControler(Vissenkom vg)
	{ 	
		// referentie onthouden
	    mygame = vg;
	    // timertje zetten voor eerste aardbei
	    myAlarm = new Alarm(2, 1, this);
	    myAlarm.startAlarm();
	}
	
	/**
	 * Als timer afloopt plaatsen we een nieuwe aardbei.
	 * De methode van de AlarmListener interface
	 * 
	 * @see phonegame.IAlarmListener#alarm(int)
	 */
	public void triggerAlarm(int id)
	{	// aardbei maken
		Log.d("StrawberryControler", "Alarm gaat af");
		Strawberry s = new Strawberry(mygame);
	    // random positie kiezen, maar niet op muur, dus findTilesAt moet 0 opleveren!
	    // we gaan door tot dat zo is.
	    int x = 10+(int)(150*Math.random());
	    int y = 10+(int)(250*Math.random());
	    // aardbei plaatsen
	    mygame.addGameObject(s, x, y);
	    // timer voor volgende aardbei
	    myAlarm.setTime(10+(int)(50*Math.random()));
	    myAlarm.restartAlarm();
	}

	
	
    /* 
    do
    {	// x moet minstens aardbei-breedte van de rechterrand blijven
        x = mygame.getRandomX(s.getFrameWidth());
    	y = mygame.getRandomY(s.getFrameHeight());
    }
    while (mygame.findTilesAt(x, y, s.getFrameWidth(), s.getFrameHeight()) != 0);
    // pos invullen
    s.setPosition(x, y);
    */

}
