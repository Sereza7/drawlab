package main;

import java.awt.Graphics;
import java.rmi.RemoteException;

import serveur.RemoteDessinServeur;

public class Ellipse extends Dessin {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ellipse() {
		super();
	}
	
	public Ellipse(RemoteDessinServeur proxy) throws RemoteException {
		super(proxy);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawOval(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
	
	
	public boolean contains(int x, int y) {
		boolean insideSelectCorners=(isSelected && ((x>0 && x<15|| -x+getWidth()>0 && (-x+getWidth())<15) && (y>0 && y<15 || -y+getHeight()>0 && (-y+getHeight())<15)));
		x-=getWidth()/2;
		y-=getHeight()/2;
		double a = getWidth()/2;
		double b = getHeight()/2;
		return ((x*x)/(a*a) + (y*y)/(b*b)) <= 1.2 && ((x*x)/(a*a) + (y*y)/(b*b))>=0.8 || insideSelectCorners;
		
	}

}
