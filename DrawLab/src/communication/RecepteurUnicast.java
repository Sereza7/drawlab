package communication;


import java.io.ByteArrayInputStream ;
import java.io.ObjectInputStream ;
import java.net.DatagramPacket ;
import java.net.DatagramSocket;
import java.net.InetAddress ;
import java.rmi.RemoteException;
import java.util.HashMap ;

import editeurs.ZoneDeDessin;
import login.Login;
import main.ClientLocal;
import main.Profil;
import main.Profil.ProfilType;
import serveur.Parametres;
import serveur.RemoteProfilServeur;

//---------------------------------------------------------------------
// classe permettant de recevoir des messages diffusés à une adresse (en multicast)

public class RecepteurUnicast extends Thread implements Runnable {

	// la socket sur laquelle on va lire les messages
	private transient DatagramSocket socketReception ;

	// l'éditeur local qu'on préviendra suite aux messages reçus
	private ClientLocal clientLocal ;
	private Login loginLocal;

	// les données à récupérer conformément au format des données envoyées :
	// - une chaine de caractère pour décrire l'action à réaliser
	private String command = new String () ;
	// - une chaine de caractère pour déterminer l'objet cible du message
	private String name = new String () ;
	// - une HashMap dans laquelle on récupèrera les paramètres nécessaires aux actions à effectuer
	private HashMap<String, Object> hm = new HashMap<String, Object> () ;

	public RecepteurUnicast (final InetAddress adresseReception, final int portReception) {
		socketReception = null ;
		try {
			// création d'une socket de réception des messages adressés à ce groupe de diffusion
			socketReception = new DatagramSocket (portReception, adresseReception) ;
			System.out.println ("socket réception : " + socketReception.getLocalPort() + " " + socketReception.getInetAddress ()) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public void setClientLocal (ClientLocal clientLocal) {
		this.clientLocal = clientLocal ;
		System.out.println(clientLocal);
	}
	public void setLoginLocal (Login login) {
		this.loginLocal = login ;
		System.out.println(login);
	}

	// méthode de réception du message : on extrait d'un flux de très bas niveau des informations formatées correctement 
	@SuppressWarnings ("unchecked")
	public void recevoir () {
		try {
			// réception d'un paquet bas niveau
			byte [] message = new byte [1024] ;
			DatagramPacket paquet = new DatagramPacket (message, message.length) ;
			socketReception.receive (paquet) ;
			// extraction des informations au format type à partir du paquet
			ByteArrayInputStream bais = new ByteArrayInputStream (paquet.getData ()) ;
			ObjectInputStream ois = new ObjectInputStream (bais) ;
			command = (String)ois.readObject () ;
			name = (String)ois.readObject () ;
			hm = (HashMap<String, Object>)ois.readObject () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}

	// méthode qui permet de recevoir des messages en parralèle des interactions utilisateur
	public void run () {
		while (true) {
			// réception effective du message et décodage dans command, name et hm
			recevoir () ;
			// traitement du message en fonction de son intitulé
			if (command.equals ("Bounds")) {
				//System.out.println ("received Bounds") ;
				// mise à jour des limites d'un dessin
				clientLocal.dessinUpdateBounds(name, (int)hm.get ("x"), (int)hm.get ("y"), (int)hm.get ("w"), (int)hm.get ("h"));
			} else if (command.equals ("Location")) {
				//System.out.println ("received Location") ;
				// mise à jour de la positiuon d'un dessin
				clientLocal.dessinUpdateLocation (name, (int)hm.get ("x"), (int)hm.get ("y"));
			} else if (command.equals ("Dessin")) {
				//System.out.println ("received Dessin") ;
				// ajout d'un dessin
				try {
					clientLocal.ajouterDessin (name, (int)hm.get ("x"), (int)hm.get ("y"), (int)hm.get ("w"), (int)hm.get ("h"));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else if (command.equals("ZOrder")) {
				clientLocal.dessinUpdateZOrder(name, (int)hm.get("z"));
			}
			else if (command.equals("SupprimerDessin")) {
				try {
					clientLocal.supprimerDessin(name);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			else if (command.equals ("Profil")) {
				try {
					loginLocal.ajouterProfil (name);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else if (command.equals("Classement")) {
				/*
				profilLocal.profilUpdateClassement(name, (int)hm.get("classement"));
				*/
			}
			else if (command.equals("ProfilState")) {
				try {
					loginLocal.updateProfilState(name, (boolean)hm.get("state"));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			else if (command.equals("SupprimerProfil")) {
				/*
				try {
					profilLocal.supprimerProfil(name);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				*/
			}
			else if (command.equals("Session")) {
				//ADD somthing here for updating the sessions in realtime
			}
			else if (command.equals("AddUserSession")) {
				if (((String)hm.get("session")).equals(clientLocal.getSession().name)) {
					clientLocal.getSession().addUser(new Profil((RemoteProfilServeur)hm.get("utilisateur")));
				}
				else {
					System.out.println("The user added wasn't for this session.");
				}
			}
			else if (command.equals("enCours")) {
				System.out.println("Launch the sessions.");
				clientLocal.getSession().setEnCours((boolean)hm.get("encours"));
			}
			
		}
	}

}

//---------------------------------------------------------------------
