package editeurs;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import main.ClientLocal;
import main.ColourListener;
import main.CreateurDessin;
import main.CreateurEllipse;
import main.CreateurEllipsePleine;
import main.CreateurRectangle;
import main.CreateurRectanglePlein;
import main.Preview;
import main.PreviewListener;
import main.Profil;
import main.ShapeListener;
import serveur.RemoteGlobalServeur;
import serveur.RemoteProfilServeur;
import utils.TopBar;


public class Editeur extends JFrame {
	
	private Preview preview;
	private final ButtonGroup shapeGroup = new ButtonGroup();
	private static final long serialVersionUID = 1L ;
	
	
	
	
	// l'élément visuel dans lequel on va manipuler des dessins 
	private ZoneDeDessin zone ;
	protected TopBar topBar;
	private RemoteProfilServeur profil;
	private ClientLocal clientLocal;
	
	
	public Editeur(RemoteGlobalServeur serveur, ClientLocal clientLocal, RemoteProfilServeur profil) {
		super();
		
		zone = new ZoneDeDessin(serveur);
		zone.setCd(new CreateurRectangle());
		
		
		preview = new Preview();
		this.setSize(1300,800);
		getContentPane().add(zone, BorderLayout.CENTER);
		
		Box verticalBox = Box.createVerticalBox();
		getContentPane().add(verticalBox, BorderLayout.WEST);
		
		JRadioButton rdbtnRectangle = new JRadioButton("Rectangle");
		shapeGroup.add(rdbtnRectangle);
		rdbtnRectangle.setSelected(true);
		verticalBox.add(rdbtnRectangle);
		CreateurDessin createurRectangle = new CreateurRectangle();
		ShapeListener rectangleListener = new ShapeListener(createurRectangle, zone);
		rdbtnRectangle.addItemListener(rectangleListener);
		
		JRadioButton rdbtnRectanglePlein = new JRadioButton("Rectangle plein");
		shapeGroup.add(rdbtnRectanglePlein);
		verticalBox.add(rdbtnRectanglePlein);
		CreateurDessin createurRectanglePlein = new CreateurRectanglePlein();
		ShapeListener rectanglePleinListener = new ShapeListener(createurRectanglePlein, zone);
		rdbtnRectanglePlein.addItemListener(rectanglePleinListener);
		
		JRadioButton rdbtnEllipse = new JRadioButton("Ellipse");
		shapeGroup.add(rdbtnEllipse);
		verticalBox.add(rdbtnEllipse);
		CreateurDessin createurEllipse = new CreateurEllipse();
		ShapeListener ellipseListener = new ShapeListener(createurEllipse, zone);
		rdbtnEllipse.addItemListener(ellipseListener);
		
		JRadioButton rdbtnEllipsePleine = new JRadioButton("Ellipse pleine");
		shapeGroup.add(rdbtnEllipsePleine);
		verticalBox.add(rdbtnEllipsePleine);
		CreateurDessin createurEllipsePleine = new CreateurEllipsePleine();
		ShapeListener ellipsePleineListener = new ShapeListener(createurEllipsePleine, zone);
		rdbtnEllipsePleine.addItemListener(ellipsePleineListener);
		
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
		
		topBar = new TopBar(this, clientLocal.getProfil().getProxy(), clientLocal, this);
		getContentPane().add(topBar, BorderLayout.NORTH);
		setVisible(true);
		preview.setDessin(zone.getCd(), zone.getForeground());
		
	}
	public ZoneDeDessin getZdd() {
		return zone;
	}
}