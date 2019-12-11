package zonesDeDessin;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.imageio.ImageIO;

import main.CreateurDessin;
import main.Dessin;
import serveur.RemoteDessinServeur;

public class ZoneDeDessin extends ZoneDeDessinEnfant{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point currentCorner;
	private Point currentEnd;
	private Dessin selectedDessin;
	private CreateurDessin cd;
	private DessinListener aRL;
	private DessinMotionListener aRML;
	private SelectListener aSL;
	
	
	public ZoneDeDessin(final String clientName, final String serveurName, final String serverHostName, final int serverRMIPort){
		super(clientName, serveurName, serverHostName, serverRMIPort);
		aRL=new DessinListener();
		aRML=new DessinMotionListener();
		addMouseListener(aRL);
		addMouseMotionListener(aRML);
		aSL=new SelectListener();
		addMouseListener(aSL);
		
	}
	
	

	public CreateurDessin getCd() {
		return this.cd;
	}
	
	public void setCd(CreateurDessin cd) {
		CreateurDessin oldCd = this.cd;
		this.cd=cd;
		mPcs.firePropertyChange("cd", oldCd, cd);
	}
	
	public Color getForeground() {
		Color fg = super.getForeground();
		return fg;
	}
	
	public void setForeground(Color fg) {
		Color oldFg = this.getForeground();
		super.setForeground(fg);
		System.out.println(mPcs);
		if(oldFg!=null) {
			mPcs.firePropertyChange("foreground", oldFg, this.getForeground());
		}
	}
	
	
	public void mouseReleased (MouseEvent e) {
		endDessin (e.getPoint ()) ;
	}
	
	public Collection<Dessin> getDessins(){
		return this.dessins.values();
	}
	
	public void setSelectedDessin(Dessin d){
		if(this.selectedDessin!=null && this.selectedDessin!=d) {
			this.selectedDessin.deselect();
		}
		this.selectedDessin=d;
	}


	private void beginDessin(Point p) throws RemoteException {
		currentCorner = p;
		currentEnd = p;
		RemoteDessinServeur proxy = serveur.addDessin(p.x, p.y, 0, 0, this.getCd(), this.getForeground());
		String proxyName = proxy.getName () ;
		if (! dessins.containsKey (proxyName)) {
			dessins.put (proxyName, currentDessin) ;
		} else {
			System.out.println ("dessin " + proxyName + " était déjà créé") ;
		}
		if(currentDessin!=null) {
			currentDessin.setProxyZOrder(0);
		}
	}
	
	public void beginDessin(Point p, Point dessinLocation) throws RemoteException {
		int x=p.x+dessinLocation.x;
		int y=p.y+dessinLocation.y;
		this.beginDessin(new Point(x,y));
	}
	
	private void endDessin(Point p) {
		currentDessin=null;
		for(Dessin d: dessins.values()) {
			try {
				d.getProxy().setZ(this.getComponentZOrder(d));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void motionDessin(Point p) {
		if(currentDessin!=null) {
			int dx = Math.abs(currentCorner.x-currentEnd.x);
			int dy = Math.abs(currentCorner.y-currentEnd.y);
			currentEnd = p;
			currentDessin.setProxyBounds(Math.min(currentCorner.x, currentEnd.x),Math.min(currentCorner.y, currentEnd.y),dx,dy);
			currentDessin.paint(currentDessin.getGraphics());
		}
		
		
	}
	
	public void motionDessin(Point p, Point dessinLocation) throws RemoteException {
		int x=p.x+dessinLocation.x;
		int y=p.y+dessinLocation.y;
		this.motionDessin(new Point(x,y));
	}
	
	
	
	public void supprimerDessin(String name) throws RemoteException {
		this.remove(dessins.get(name));
		this.repaint();
		if(this.selectedDessin!=null) {
			this.selectedDessin=null;
		}
		this.serveur.supprimerDessin(name);
		this.dessins.remove(name);
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void saveImage(){
		Dessin tempDessin = null;
		if(selectedDessin!=null) {
			tempDessin=selectedDessin;
			selectedDessin.deselect();
		}
	    BufferedImage imagebuf=null;
	    try {
	        imagebuf = new Robot().createScreenCapture(this.bounds());
	    } catch (AWTException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }  
	     Graphics2D graphics2D = imagebuf.createGraphics();
	     this.paint(graphics2D);
	     try {
	    	Integer i=1;
	    	while(new File("save"+i.toString()+".png").exists()) {
	    		i+=1;
	    	}
	        ImageIO.write(imagebuf,"png", new File("save"+i.toString()+".png"));
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        System.out.println("error");
	    }
	     if(tempDessin!=null) {
	    	 selectedDessin=tempDessin;
	    	 selectedDessin.select();
	     }
	     System.out.println("fichier enregistré");
	}
	
	
	
	
	
	
	
	private class DessinListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(selectedDessin==null) {
				try {
					beginDessin(e.getPoint());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(selectedDessin==null) {
				endDessin(e.getPoint());
			}
		}
		
	}
	
	private class DessinMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			if(selectedDessin==null) {
				motionDessin(e.getPoint());
			}

			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}
		
	}
	
	
	
	private class SelectListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(selectedDessin==null || selectedDessin.contains(e.getX()-selectedDessin.getX(), e.getY()-selectedDessin.getY())) {
				return;
			}
			else {
				selectedDessin.deselect();
				selectedDessin=null;
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			
		}
		
	}
	
	


}
