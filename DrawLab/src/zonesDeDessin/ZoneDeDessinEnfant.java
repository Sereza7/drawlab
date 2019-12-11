package zonesDeDessin;

import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

import communication.RecepteurUnicast;
import main.Dessin;
import serveur.RemoteDessinServeur;
import serveur.RemoteEditeurServeur;

public class ZoneDeDessinEnfant extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Dessin currentDessin;
	
	// le Thread pour pouvoir recevoir des mises à jour en provenance du serveur
	private Thread threadReceiver ;
	
	// le récepteur de messages diffusés aux abonnés
	private RecepteurUnicast recepteurUnicast ;
	
	// le serveur distant qui centralise toutes les informations
	protected RemoteEditeurServeur serveur ;
	
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
	protected HashMap<String, Dessin> dessins = new HashMap<String, Dessin> () ;
	
	protected PropertyChangeSupport mPcs;
	
	public ZoneDeDessinEnfant(final String clientName, final String serveurName, final String serverHostName, final int serverRMIPort){
		super();
		try {
			// tentative de connexion au serveur distant
			serveur = (RemoteEditeurServeur)Naming.lookup ("//" + serverHostName + ":" + serverRMIPort + "/" + serveurName) ;
			// invocation d'une ptremière méthode juste pour test
			serveur.answer ("hello from " + getName ()) ;
			// récupération de tous les dessins déjà présents sur le serveur
			ArrayList<RemoteDessinServeur> remoteDessins = serveur.getSharedDessins() ;
			// ajout de tous les dessins dans la zone de dessin
			for (RemoteDessinServeur rd : remoteDessins) {
				ajouterDessin (rd, rd.getName (), rd.getX (), rd.getY (), rd.getWidth(), rd. getHeight ()) ;
			}
			for (RemoteDessinServeur rd : remoteDessins) {
				this.setComponentZOrder(dessins.get(rd.getName()), rd.getZ());
			}
		} catch (Exception e) {
			System.out.println ("probleme liaison CentralManager") ;
			e.printStackTrace () ;
			System.exit (1) ;
		}
		try {
			// création d'un récepteur unicast en demandant l'information de numéro port au serveur
			// en même temps on transmet au serveur l'adresse IP de la machine du client au serveur
			// de façon à ce que ce dernier puisse par la suite envoyer des messages de mise à jour à ce récepteur 
			recepteurUnicast = new RecepteurUnicast (InetAddress.getByName (clientName), serveur.getPortEmission (InetAddress.getByName (clientName))) ;
			// on aimerait bien demander automatiquement quel est l'adresse IP de la machine du client,
			// mais le problème est que celle-ci peut avoir plusieurs adresses IP (filaire, wifi, ...)
			// et qu'on ne sait pas laquelle sera retournée par InetAddress.getLocalHost ()...
			//recepteurUnicast = new RecepteurUnicast (serveur.getPortEmission (InetAddress.getLocalHost ())) ;
			recepteurUnicast.setClientLocal (this) ;
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// création d'un Thread pour pouvoir recevoir les messages du serveur en parallèle des interactions avec les dessins
		threadReceiver = new Thread (recepteurUnicast) ;
		// démarrage effectif du Thread
		threadReceiver.start () ;
		
		mPcs = new PropertyChangeSupport(this);
		
		setBackground(Color.white);
		setForeground(Color.blue);
		getForeground();
		setLayout(null);
		setVisible(true);
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
	
	public void supprimerDessin(String name) throws RemoteException {
		this.remove(dessins.get(name));
		this.repaint();
		this.serveur.supprimerDessin(name);
		this.dessins.remove(name);
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
