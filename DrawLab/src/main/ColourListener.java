package main;

import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColourListener implements ChangeListener{
	
	private JColorChooser colourChooser;
	private ZoneDeDessin zone;
	
	public ColourListener(JColorChooser colourChooser, ZoneDeDessin zone) {
		this.colourChooser=colourChooser;
		this.zone=zone;
	}


	@Override
	public void stateChanged(ChangeEvent arg0) {
		Color newColour = colourChooser.getColor();
	    zone.setForeground(newColour);
	}

}
