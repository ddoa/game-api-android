package testGames.gameEngineTest;

import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.objects.GameObject;
import android.gameengine.icadroids.objects.MoveableGameObject;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * DebugObject is responsible the rendering of the (textual) debuginfo.
 *
 * @author Bas van der Zandt
 */
public class debugObject extends GameObject {
    Paint pt = new Paint();
    Paint vierkanten = new Paint();
    public boolean renderGameObjects = false;
    public boolean renderTimers = false;
    public boolean renderObjectInfo = false;
    public MoveableGameObject gob;

    public debugObject() {
        pt.setColor(Color.BLACK);
        pt.setTextSize(18);
        vierkanten.setColor(Color.BLUE);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void drawGameObject(Canvas canvas) {
        super.drawGameObject(canvas);
        if (renderGameObjects) {
            canvas.drawText("number of objects: " + GameEngine.items.size(),
                    100, 80, pt);
            for (int i = 0; i < GameEngine.items.size(); i++) {
                canvas.drawText("Object: " + GameEngine.items.get(i), 100,
                        100 + (i * 20), pt);
            }
        }
        if (renderTimers) {
            canvas.drawText(
                    "Number of Alarms: " + GameEngine.gameAlarms.size(), 120,
                    60, pt);
            for (int i = 0; i < GameEngine.gameAlarms.size(); i++) {
                canvas.drawText("Alarm: " + GameEngine.gameAlarms.get(i), 150,
                        100 + (i * 20), pt);
            }
        }
        if (renderObjectInfo && gob != null) {

            canvas.drawRect(gob.getX() - 10, gob.getY() - 10,
                    gob.getX() + gob.getFrameWidth() + 20,
                    gob.getY() + gob.getFrameHeight() + 20, vierkanten);

            canvas.drawText("clicked GameObject: " + gob, 140, 60, pt);

            if (gob instanceof MoveableGameObject) {
                canvas.drawText("speed: " + gob.getSpeed(), 140, 80, pt);
                canvas.drawText("x: " + gob.getX() + " y: " + gob.getY(), 140,
                        100, pt);
                canvas.drawText("xSpeed: " + gob.getxSpeed(), 140, 120, pt);
                canvas.drawText("ySpeed: " + gob.getySpeed(), 140, 140, pt);
                canvas.drawText("direction: " + gob.getDirection(), 140, 160,
                        pt);
                canvas.drawText("friction: " + gob.getFriction(), 140, 180, pt);
                canvas.drawText("frameWidth: " + gob.getFrameWidth(), 140, 200,
                        pt);
                canvas.drawText("frameHeight: " + gob.getFrameHeight(), 140,
                        220, pt);
            }

        }
    }

}
