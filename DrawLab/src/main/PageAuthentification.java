package main;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import communication.RecepteurUnicast;
import serveur.ProfilServeur;
import serveur.RemoteDessinServeur;
import serveur.RemoteEditeurServeur;

public class PageAuthentification extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RemoteEditeurServeur serveur;
	private RecepteurUnicast recepteurUnicast;
	private Thread threadReceiver;

	public PageAuthentification(final String clientName, final String serveurName, final String serverHostName, final int serverRMIPort) {
		super();
		try {
			// tentative de connexion au serveur distant
			serveur = (RemoteEditeurServeur)Naming.lookup ("//" + serverHostName + ":" + serverRMIPort + "/" + serveurName) ;
			// invocation d'une ptremière méthode juste pour test
			serveur.answer ("hello from " + getName ()) ;
			// récupération de tous les dessins déjà présents sur le serveur
			ArrayList<ProfilServeur> remoteDessins = serveur.getSharedProfils() ;
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
			recepteurUnicast.setProfilLocal (this) ;
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// création d'un Thread pour pouvoir recevoir les messages du serveur en parallèle des interactions avec les dessins
		threadReceiver = new Thread (recepteurUnicast) ;
		// démarrage effectif du Thread
		threadReceiver.start () ;
		
		
		this.setSize(1300,800);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
	}
		
	
}
