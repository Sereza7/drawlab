package editeurs;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import main.ColourListener;
import main.CreateurRectangle;
import main.Preview;
import main.PreviewListener;
import zonesDeDessin.ZoneDeDessin;
import elementsGraphiques.BarreOutils;
import elementsGraphiques.BarreSuperieure;

public class Editeur extends JFrame {
	
	private Preview preview;
	private static final long serialVersionUID = 1L ;
	
	
	
	// l'élément visuel dans lequel on va manipuler des dessins 
	private ZoneDeDessin zone ;
	
	
	public Editeur(final String clientName, final String serveurName, final String serverHostName, final int serverRMIPort, String titre) {
		super();
		
		zone = new ZoneDeDessin(clientName, serveurName, serverHostName, serverRMIPort);
		zone.setCd(new CreateurRectangle());
		
		
		preview = new Preview();
		this.setSize(1300,800);
		getContentPane().add(zone, BorderLayout.CENTER);
		
		BarreOutils barreOutils= new BarreOutils(zone);
		getContentPane().add(barreOutils, BorderLayout.WEST);
		
		
		
		Box verticalBox_1 = Box.createVerticalBox();
		getContentPane().add(verticalBox_1, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JColorChooser colourChooser = new JColorChooser(Color.blue);
		panel.add(colourChooser, BorderLayout.NORTH);
		colourChooser.setPreviewPanel(new JPanel());
		while(colourChooser.getChooserPanels().length>1) {
			colourChooser.removeChooserPanel(colourChooser.getChooserPanels()[1]);
		}
		
		panel.add(preview, BorderLayout.CENTER);
		
		colourChooser.getSelectionModel().addChangeListener((new ColourListener(colourChooser, zone)));
		zone.addPropertyChangeListener(new PreviewListener(preview));
		
		BarreSuperieure topBar = new BarreSuperieure("What are they drawing?");
		getContentPane().add(topBar, BorderLayout.NORTH);
		
		setVisible(true);
		preview.setDessin(zone.getCd(), zone.getForeground());
		
	}
}