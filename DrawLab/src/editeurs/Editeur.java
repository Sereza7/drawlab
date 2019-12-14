package editeurs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import main.ColourListener;
import main.CreateurDessin;
import main.CreateurEllipse;
import main.CreateurEllipsePleine;
import main.CreateurRectangle;
import main.CreateurRectanglePlein;
import main.Preview;
import main.PreviewListener;
import main.ShapeListener;
import serveur.RemoteGlobalServeur;

import javax.swing.border.BevelBorder;

public class Editeur extends JFrame {
	
	private Preview preview;
	private final ButtonGroup shapeGroup = new ButtonGroup();
	private static final long serialVersionUID = 1L ;
	protected JLabel topText;
	
	
	
	// l'élément visuel dans lequel on va manipuler des dessins 
	private ZoneDeDessin zone ;
	
	
	public Editeur(RemoteGlobalServeur serveur) {
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
		
		JPanel topBar = new JPanel();
		topBar.setBackground(Color.WHITE);
		getContentPane().add(topBar, BorderLayout.NORTH);
		topBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		BufferedImage  image = null;
		Image scaledImage;
		JLabel picLabelLogo;
		
		JPanel logo = new JPanel();
		logo.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
		logo.setBackground(Color.WHITE);
		topBar.add(logo);
		try {
			image = ImageIO.read(new File("logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabelLogo = new JLabel(new ImageIcon(scaledImage));
		picLabelLogo.setBackground(Color.WHITE);
		logo.add(picLabelLogo);
		
		topText = new JLabel("What are they drawing?");
		topText.setFont(new Font("Monotype Corsiva", Font.PLAIN, 36));
		topText.setBackground(Color.WHITE);
		topBar.add(topText);
		
		JLabel rank = new JLabel("Rank: 1");
		rank.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		rank.setBackground(Color.WHITE);
		topBar.add(rank);
		
		JPanel trophy = new JPanel();
		trophy.setBackground(Color.WHITE);
		topBar.add(trophy);
		
		try {
			image = ImageIO.read(new File("trophy.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel picTrophy = new JLabel(new ImageIcon(scaledImage));
		picTrophy.setBackground(Color.WHITE);
		trophy.add(picTrophy);
		
		JPanel soundOrShare = new JPanel();
		soundOrShare.setBackground(Color.WHITE);
		topBar.add(soundOrShare);
		try {
			image = ImageIO.read(new File("soundOrShare.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel picLabelSoundOrShare = new JLabel(new ImageIcon(scaledImage));
		picLabelSoundOrShare.setBackground(Color.WHITE);
		soundOrShare.add(picLabelSoundOrShare);
		
		JPanel logOut = new JPanel();
		logOut.setBackground(Color.WHITE);
		topBar.add(logOut);
		try {
			image = ImageIO.read(new File("logout.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel picLabellogOut = new JLabel(new ImageIcon(scaledImage));
		picLabellogOut.setBackground(Color.WHITE);
		logOut.add(picLabellogOut);
		setVisible(true);
		preview.setDessin(zone.getCd(), zone.getForeground());
		
	}
	public ZoneDeDessin getZdd() {
		return zone;
	}
}