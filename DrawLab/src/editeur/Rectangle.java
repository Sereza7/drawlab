package editeur;

import java.awt.Graphics;
import java.rmi.RemoteException;

import serveur.RemoteDessinServeur;

public class Rectangle extends Dessin{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Rectangle() {
		super();
	}
	
	public Rectangle(RemoteDessinServeur proxy) throws RemoteException {
		super(proxy);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
	
	@Override
	public boolean contains(int x, int y) {
		return ((((x>=-3 && x<=3) || (x>= this.getWidth()-3 && x<= this.getWidth()+3)) && y>=-3 && y<=this.getHeight()+3) || (((y>=-3 && y<=3) || (y>= this.getHeight()-3 && y<= this.getHeight()+3)) && (x>=-3 && x<=this.getWidth()+3)) || (isSelected && ((x>0 && x<15|| -x+getWidth()>0 && (-x+getWidth())<15) && (y>0 && y<15 || -y+getHeight()>0 && (-y+getHeight())<15))));
	}
	
	
}
