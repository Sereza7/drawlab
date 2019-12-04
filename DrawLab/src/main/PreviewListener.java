package main;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class PreviewListener implements PropertyChangeListener{
	
	private Preview preview;
	
	public PreviewListener(Preview preview) {
		this.preview = preview;
	}





	@Override
	public void propertyChange(PropertyChangeEvent e) {
		
		String propertyName = e.getPropertyName();
		if(propertyName.equals("foreground")) {
			System.out.println(e.getPropertyName());
			preview.setDessin(preview.getCd(), (Color) e.getNewValue());
		}
		else if(propertyName.equals("cd")){
			preview.setCd((CreateurDessin) e.getNewValue());
		}
		
	}

}
