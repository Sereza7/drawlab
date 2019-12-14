package editeurs;

import java.awt.Graphics;
import java.rmi.RemoteException;

import serveur.RemoteDessinServeur;

public class FullRectangle extends Rectangle{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FullRectangle(RemoteDessinServeur proxy) throws RemoteException {
		super(proxy);
	}

	public FullRectangle() {
		super();
	}

	public void paint(Graphics g) {
		super.paint(g);
		if(!isSelected) {
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		else {
			g.fillRect(15, 15, this.getWidth()-30, this.getHeight()-30);
			g.fillRect(15, 0, this.getWidth()-30, 15);
			g.fillRect(15, getHeight()-15, this.getWidth()-30, 15);
			g.fillRect(0, 15, 15, this.getHeight()-30);
			g.fillRect(getWidth()-15, 15, 15, this.getHeight()-30);
		}
		
	}
	
	public boolean contains(int x, int y) {
		return (x>=0 && x<= this.getWidth() && y>=0 && y<= this.getHeight());
	}

}
