package serveur ;

import java.awt.Color;
import java.io.Serializable ;
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject ;
import java.util.HashMap ;
import java.util.List;

import communication.EmetteurUnicast;
import main.CreateurDessin;

// classe de Dessin présente sur le serveur :
// - pour pouvoir invoquer des méthodes à distance, elle doit étendre UnicastRemote object ou implémenter l'interface Remote
// - ici elle fait les deux (car l'interface RemoteDessin étend l'interface Remote)
// - la classe doit également être Serializable si on veut la transmettre sur le réseau
public class DessinServeur extends UnicastRemoteObject implements RemoteDessinServeur, Serializable {

	// les attrribus minimaux d'un Dessin
	private int x ;
	private int y ;
	private int z;
	private int w ;
	private int h ;
	private CreateurDessin cd;
	private Color color;
	private String name ;

	// un attribut permettant au Dessin de diffuser directement ses mises à jour, sans passer par le serveur associé
	// - cet attribut n'est pas Serializable, du coup on le déclare transient pour qu'il ne soit pas inclu dans la sérialisation
	protected transient List<EmetteurUnicast> emetteurs ;
	public void setEmetteurs (List<EmetteurUnicast> emetteurs) {
		this.emetteurs = emetteurs ;
	}

	private static final long serialVersionUID = 1L ;

	// constructeur du Dessin sur le serveur : il diffuse alors qu'il faut créer un nouveau dessin sur tous les clients 
	public DessinServeur (String name, List<EmetteurUnicast> senders, CreateurDessin cd, Color color) throws RemoteException {
		this.emetteurs = senders ;
		this.name = name ;
		this.cd = cd;
		this.color = color;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("x", new Integer (0)) ;
		hm.put ("y", new Integer (0)) ;
		hm.put ("z", new Integer (0)) ;
		hm.put ("w", new Integer (0)) ;
		hm.put ("h", new Integer (0)) ;
		hm.put ("color", color) ;
		hm.put ("cd", cd);
		for (EmetteurUnicast sender : senders) {
			sender.diffuseMessage ("Dessin", getName (), hm) ;
		}
	}

	public String getName () throws RemoteException {
		return name ;
	}
	
	public CreateurDessin getCreateurDessin() {
		return this.cd;
	}

	// méthode qui met à jour les limites du Dessin, qui diffuse ensuite ce changement à tous les éditeurs clients
	public void setBounds (int x, int y, int w, int h) throws RemoteException {
		//System.out.println (getName() + " setBounds : " + x + " " + y + " " + w + " " + h) ;
		this.x = x ;
		this.y = y ;
		this.w = w ;
		this.h = h ;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("x", new Integer (x)) ;
		hm.put ("y", new Integer (y)) ;
		hm.put ("w", new Integer (w)) ;
		hm.put ("h", new Integer (h)) ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("Bounds", getName (), hm) ;
		}
	}

	// méthode qui met à jour la position du Dessin, qui diffuse ensuite ce changement à tous les éditeurs clients
	public void setLocation (int x, int y) throws RemoteException {
		this.x = x ;
		this.y = y ;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("x", new Integer (x)) ;
		hm.put ("y", new Integer (y)) ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("Location", getName (), hm) ;
		}
	}
	
	
	public void setZOrder(int currentOrder) throws RemoteException {
		this.z = currentOrder;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("z", new Integer (z)) ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("ZOrder", getName (), hm) ;
		}
	}
	
	@Override
	public void supprimer() throws RemoteException {
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("Supprimer", getName (), hm) ;
		}
		
	}
	

	@Override
	public int getX() throws RemoteException {
		return x ;
	}

	@Override
	public int getY() throws RemoteException {
		return y ;
	}
	
	@Override
	public int getZ() throws RemoteException {
		return z ;
	}
	
	@Override
	public void setZ(int z) throws RemoteException {
		this.z = z;
	}

	@Override
	public int getWidth() throws RemoteException {
		return w ;
	}

	@Override
	public int getHeight() throws RemoteException {
		return h ;
	}
	
	public Color getColor() throws RemoteException {
		return this.color;
	}

	

	

}