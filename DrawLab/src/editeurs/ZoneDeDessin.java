package editeurs;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.CreateurDessin;
import serveur.RemoteDessinServeur;
import serveur.RemoteGlobalServeur;

public class ZoneDeDessin extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point currentCorner;
	private Point currentEnd;
	private Dessin currentDessin;
	private Dessin selectedDessin;
	private PropertyChangeSupport mPcs = new PropertyChangeSupport(this);
	private CreateurDessin cd;
	private DessinListener aRL;
	private DessinMotionListener aRML;
	private SelectListener aSL;
	
	// le serveur distant qui centralise toutes les informations
	private RemoteGlobalServeur serveur ;
	
	// le nom de l'éditeur local
	protected String name ;
	
	// le port rmi sur lequel est déclaré le serveur distant
	protected int rmiPort ;
	
	// le nom de la machine sur laquelle se trouve le serveur distant :
	// - le système saura calculer l'adresse IP de la machine à partir de son nom
	// - sinon on met directement l'adresse IP du serveur dans cette chaine de caractère 
	protected String hostName ;
	
	
	// une table pour stocker tous les dessins produits :
	// - elle est redondante avec le contenu de la ZoneDe Dessin
	// - mais elle va permettre d'accéder plus rapidement à un Dessin à partir de son nom
	private HashMap<String, Dessin> dessins = new HashMap<String, Dessin> () ;
	
	ZoneDeDessin(RemoteGlobalServeur serveur){
		super();
		this.serveur=serveur;	
		try {
			ArrayList<RemoteDessinServeur>  remoteDessins = serveur.getSharedDessins();
			for (RemoteDessinServeur rd : remoteDessins) {
				ajouterDessin (rd, rd.getName (), rd.getX (), rd.getY (), rd.getWidth(), rd. getHeight ()) ;
			}
			for (RemoteDessinServeur rd : remoteDessins) {
				this.setComponentZOrder(dessins.get(rd.getName()), rd.getZ());
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		setBackground(Color.white);
		setForeground(Color.blue);
		getForeground();
		setLayout(null);
		aRL=new DessinListener();
		aRML=new DessinMotionListener();
		addMouseListener(aRL);
		addMouseMotionListener(aRML);
		aSL=new SelectListener();
		addMouseListener(aSL);
		setVisible(true);
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
	
	protected void beginDessin(Point p, Point dessinLocation) throws RemoteException {
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
	
	protected void motionDessin(Point p, Point dessinLocation) throws RemoteException {
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
	
	
	
	// méthode permettant d'ajouter localement un dessin déjà présent sur le serveur
	// - elle sera appelée suite à la réception d'un message diffusé par le serveur
	public synchronized void ajouterDessin (String proxyName, int x, int y, int w, int h) throws RemoteException {
		System.out.println("ajout du dessin " + proxyName);
		// on ne l'ajoute que s'il n'est pas déjà présent
		// - il pourrait déjà être présent si il avait été créé localement par une interaction dans cet éditeur local
		if (! dessins.containsKey (proxyName)) {
			RemoteDessinServeur proxy = null ;
			try {
				// récupération du proxy via une demande au serveur
				proxy = serveur.getDessin (proxyName);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			if (proxy == null) {
				System.out.println("proxy " + proxyName + " null");
			}
			// ajout du dessin
			ajouterDessin (proxy, proxyName, x, y, w, h) ;
		} else {
			System.out.println ("dessin " + proxyName + " était déjà présent") ;
		}
	}

	// méthode d'ajout d'un dessin : factorisation de code
	public void ajouterDessin (RemoteDessinServeur proxy, String proxyName, int x, int y, int w, int h) throws RemoteException {
		// création effective d'un dessin
		if(proxy!=null) {
			currentDessin = proxy.getCreateurDessin().creerDessin(proxy) ;
			currentDessin.setForeground(proxy.getColor());
			// ajout du dessin dans la liste des dessins
			dessins.put (proxyName, currentDessin) ;
			// initialisation des limites du dessin
			currentDessin.setBounds(x, y, w, h) ;
			// ajout du dessin dans la zone de dessin, au premier plan (grâce au "0" dans le add)
			this.add (currentDessin, 0) ;
			//currentDessin.paint(currentDessin.getGraphics());
		}
	}
	
	
	
	// méthode permettant de mettre à jour les limites d'un dessin
	// - elle sera appelée suite à la réception d'un message diffusé par le serveur  
	public synchronized void objectUpdateBounds (String objectName, int x, int y, int w, int h) {
		// récupération du dessin à partir de son nom
		Dessin dessinToUpdate = dessins.get (objectName) ;
		if (dessinToUpdate != null) {
			dessinToUpdate.setBounds (x, y, w, h) ;
			dessinToUpdate.paint(dessinToUpdate.getGraphics());
		}
		
	}

	// méthode permettant de mettre à jour la position d'un dessin
	// - elle sera appelée suite à la réception d'un message diffusé par le serveur  
	public synchronized void objectUpdateLocation (String objectName, int x, int y) {
		// récupération du dessin à partir de son nom
		Dessin dessinToUpdate = dessins.get (objectName) ;
		if (dessinToUpdate != null) {
			dessinToUpdate.setLocation (x, y) ;
		}		
		dessinToUpdate.paint(dessinToUpdate.getGraphics());
	}
	
	// méthode permettant de mettre à jour la position d'un dessin
	// - elle sera appelée suite à la réception d'un message diffusé par le serveur  
	public synchronized void objectUpdateZOrder (String objectName, int z) {
		// récupération du dessin à partir de son nom
		Dessin dessinToUpdate = dessins.get(objectName) ;
		if (dessinToUpdate != null) {
			this.setComponentZOrder(dessinToUpdate, z);
			try {
				dessinToUpdate.getProxy().setZ(this.getComponentZOrder(dessinToUpdate));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}		
		repaint();
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
	
	public void
    addPropertyChangeListener(PropertyChangeListener listener) {
        mPcs.addPropertyChangeListener(listener);
    }
    
    public void
    removePropertyChangeListener(PropertyChangeListener listener) {
        mPcs.removePropertyChangeListener(listener);
    }
	
	


}
