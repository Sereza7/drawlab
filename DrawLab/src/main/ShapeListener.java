package main;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import editeur.ZoneDeDessin;

public class ShapeListener implements ItemListener{
	
	CreateurDessin cd;
	ZoneDeDessin zone;
	
	public ShapeListener(CreateurDessin cd, ZoneDeDessin zone) {
		this.cd = cd;
		this.zone=zone;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if( e.getStateChange() == ItemEvent.SELECTED) {
			zone.setCd(cd);
		}
		
	}
	
	

}
