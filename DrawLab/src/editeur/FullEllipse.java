package editeur;

import java.awt.Graphics;
import java.rmi.RemoteException;

import serveur.RemoteDessinServeur;

public class FullEllipse extends Ellipse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FullEllipse(RemoteDessinServeur proxy) throws RemoteException {
		super(proxy);
	}

	public FullEllipse() {
		super();
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.fillOval(0, 0, this.getWidth(), this.getHeight());
	}
	
	public boolean contains(int x, int y) {
		boolean insideSelectCorners=(isSelected && ((x>0 && x<15|| -x+getWidth()>0 && (-x+getWidth())<15) && (y>0 && y<15 || -y+getHeight()>0 && (-y+getHeight())<15)));
		x-=getWidth()/2;
		y-=getHeight()/2;
		double a = getWidth()/2;
		double b = getHeight()/2;
		return ((x*x)/(a*a) + (y*y)/(b*b)) <=1 || insideSelectCorners;
	}

}
