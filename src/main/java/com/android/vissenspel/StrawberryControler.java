package com.android.vissenspel;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;

/**
 * A Controler that generates strawberries Vissenkom and puts them into the game
 * randomly. Is not physically present in the game, so does not extend
 * GameObject.
 *
 * @author Paul Bergervoet
 */

public class StrawberryControler implements IAlarm {
    private Vissenkom mygame;
    private Alarm myAlarm;

    /**
     * Create a Controler and set the first Alarm
     *
     * @param vk reference to the game,
     */
    public StrawberryControler(Vissenkom vk) {
        mygame = vk;
        myAlarm = new Alarm(2, 1, this);
        myAlarm.startAlarm();
    }

    /**
     * When Alarm rings, create a strawberry and add it.
     * Set Alarm for next strawberry.
     *
     * @see android.gameengine.icadroids.alarms.IAlarm#triggerAlarm(int)
     */
    public void triggerAlarm(int id) { // aardbei maken
        //Log.d("StrawberryControler", "Alarm gaat af");
        Strawberry s = new Strawberry(mygame);
        // world size has not been fixed, put it in a block of 600*400 pixels
        int x = 10 + (int) (570 * Math.random());
        int y = 10 + (int) (370 * Math.random());
        mygame.addGameObject(s, x, y);
        myAlarm.setTime(10 + (int) (50 * Math.random()));
        myAlarm.restartAlarm();
    }

}
