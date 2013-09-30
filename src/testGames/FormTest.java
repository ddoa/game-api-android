package testGames;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.forms.GameForm;
import android.gameengine.icadroids.forms.IFormInput;
import android.view.View;

public class FormTest extends GameEngine implements IFormInput {
	
	GameForm gf;
	public FormTest() {


	}
	@Override
	protected void initialize() {
		gf = new GameForm(this);
		gf.showView("testformpje", this);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void formElementClicked(View touchedElement) {
		if(gf.getElementName(touchedElement).equals("knop")){
			gf.sendToast("Hoi!", 5);
		}
		if(gf.getElementName(touchedElement).equals("tekstveldje")){
			gf.sendToast(gf.getTextFromTextfield("tekstveldje"), 5);
		}
		
	}
	
	

}
