package elementsGraphiques;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

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
		Box verticalBox = Box.createVerticalBox();
		this.add(verticalBox);
		
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
		
	}

}
