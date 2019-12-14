package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;


import communication.RecepteurUnicast;
import serveur.RemoteDessinServeur;
import serveur.RemoteGlobalServeur;

public class ClientLocal {
	
	// le Thread pour pouvoir recevoir des mises à jour en provenance du serveur
	private Thread threadReceiver ;
	
	// le récepteur de messages diffusés aux abonnés
	private RecepteurUnicast recepteurUnicast ;
	
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
	

	private Session session;
	private Profil profil;
	
	
	public ClientLocal(final String clientName, final String serveurName, final String serverHostName, final int serverRMIPort) {
		super();
		this.profil = null;
		try {
			// tentative de connexion au serveur distant
			serveur = (RemoteGlobalServeur)Naming.lookup ("//" + serverHostName + ":" + serverRMIPort + "/" + serveurName) ;
			// invocation d'une ptremière méthode juste pour test
			serveur.answer ("hello from " + getName()) ;
			// récupération de tous les dessins déjà présents sur le serveur
			
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
		threadReceiver.start ();

		this.session= new Session(serveur);
		
		
		
	}

	// méthode permettant d'ajouter localement un dessin déjà présent sur le serveur
	// - elle sera appelée suite à la réception d'un message diffusé par le serveur
	public synchronized void ajouterDessin (String proxyName, int x, int y, int w, int h) throws RemoteException {
		this.session.ajouterDessin(proxyName, x, y, w, h);
	}

	// méthode d'ajout d'un dessin : factorisation de code
	public void ajouterDessin (RemoteDessinServeur proxy, String proxyName, int x, int y, int w, int h) throws RemoteException {
		this.session.ajouterDessin(proxy, proxyName, x, y, w, h);
	}
	
	
	
	// méthode permettant de mettre à jour les limites d'un dessin
	// - elle sera appelée suite à la réception d'un message diffusé par le serveur  
	public synchronized void dessinUpdateBounds (String objectName, int x, int y, int w, int h) {
		this.session.objectUpdateBounds(objectName, x, y, w, h);
		
	}

	// méthode permettant de mettre à jour la position d'un dessin
	// - elle sera appelée suite à la réception d'un message diffusé par le serveur  
	public synchronized void dessinUpdateLocation (String objectName, int x, int y) {
		this.session.objectUpdateLocation(objectName, x, y);
	}
	
	// méthode permettant de mettre à jour la position d'un dessin
	// - elle sera appelée suite à la réception d'un message diffusé par le serveur  
	public synchronized void dessinUpdateZOrder (String objectName, int z) {
		this.session.objectUpdateZOrder(objectName, z);
	}
	
	
	public void saveImage(){
		this.session.saveImage();
	}


	public synchronized void supprimerDessin(String name) throws RemoteException {
		session.supprimerDessin(name);
		
	}
	public String getName() {
		if (profil!=null) {
			return profil.getUserName();
		}
		return "session1";
	}
	public RemoteGlobalServeur getServeur() {
		return this.serveur;
	}

}
