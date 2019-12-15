package elementsGraphiques;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import main.CreateurDessin;
import main.CreateurEllipse;
import main.CreateurEllipsePleine;
import main.CreateurRectangle;
import main.CreateurRectanglePlein;
import main.ShapeListener;
import zonesDeDessin.ZoneDeDessin;

public class BarreOutils extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ButtonGroup shapeGroup = new ButtonGroup();
	
	public BarreOutils(ZoneDeDessin zone) {
		this.setBackground(Color.white);
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBackground(Color.white);
		this.add(verticalBox);
		
		BufferedImage  image = null;
		Image scaledImage;
		
		try {
			image = ImageIO.read(new File("img/rectangle.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
		JRadioButtonMenuItem rdbtnRectangle = new JRadioButtonMenuItem(new ImageIcon(scaledImage));
		rdbtnRectangle.setBackground(Color.white);
		shapeGroup.add(rdbtnRectangle);
		rdbtnRectangle.setSelected(true);
		verticalBox.add(rdbtnRectangle);
		CreateurDessin createurRectangle = new CreateurRectangle();
		ShapeListener rectangleListener = new ShapeListener(createurRectangle, zone);
		rdbtnRectangle.addItemListener(rectangleListener);
		
		try {
			image = ImageIO.read(new File("img/rectangle plein.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
		JRadioButtonMenuItem rdbtnRectanglePlein = new JRadioButtonMenuItem(new ImageIcon(scaledImage));
		rdbtnRectanglePlein.setBackground(Color.white);
		shapeGroup.add(rdbtnRectanglePlein);
		verticalBox.add(rdbtnRectanglePlein);
		CreateurDessin createurRectanglePlein = new CreateurRectanglePlein();
		ShapeListener rectanglePleinListener = new ShapeListener(createurRectanglePlein, zone);
		rdbtnRectanglePlein.addItemListener(rectanglePleinListener);
		
		try {
			image = ImageIO.read(new File("img/ellipse.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
		JRadioButtonMenuItem rdbtnEllipse = new JRadioButtonMenuItem(new ImageIcon(scaledImage));
		rdbtnEllipse.setBackground(Color.white);
		shapeGroup.add(rdbtnEllipse);
		verticalBox.add(rdbtnEllipse);
		CreateurDessin createurEllipse = new CreateurEllipse();
		ShapeListener ellipseListener = new ShapeListener(createurEllipse, zone);
		rdbtnEllipse.addItemListener(ellipseListener);
		
		try {
			image = ImageIO.read(new File("img/ellipse pleine.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
		JRadioButtonMenuItem rdbtnEllipsePleine = new JRadioButtonMenuItem(new ImageIcon(scaledImage));
		rdbtnEllipsePleine.setBackground(Color.white);
		shapeGroup.add(rdbtnEllipsePleine);
		verticalBox.add(rdbtnEllipsePleine);
		CreateurDessin createurEllipsePleine = new CreateurEllipsePleine();
		ShapeListener ellipsePleineListener = new ShapeListener(createurEllipsePleine, zone);
		rdbtnEllipsePleine.addItemListener(ellipsePleineListener);
		
	}

}
