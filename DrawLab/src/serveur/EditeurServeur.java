package serveur ;

import java.awt.Color;
import java.io.Serializable ;
import java.net.InetAddress;
import java.rmi.Naming ;
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap ;
import java.util.List;

import communication.EmetteurUnicast;
import main.CreateurDessin;
import main.Profil.ProfilType;

//classe d'éditeur présente sur le serveur :
//- pour pouvoir invoquer des méthodes à distance, elle doit étendre UnicastRemote object ou implémenter l'interface Remote
//- ici elle fait les deux (car l'interface RemoteEditeurServeur étend l'interface Remote)
//- la classe doit également être Serializable si on veut la transmettre sur le réseau
public class EditeurServeur extends UnicastRemoteObject implements RemoteEditeurServeur, Serializable {

	// le nom du serveur
	protected String name ;

	// le port sur lequel est déclaré le serveur
	protected int portRMI ;
	
	// la machine sur laquelle se trouve le serveur
	protected String hostName ;

	// un entier pour générer des noms de dessins différents
	protected int idDessin ;
	
	// un entier pour générer des noms de dessins différents
	protected int portEmission ;
	
	// un diffuseur à une liste d'abonnés
	private List<EmetteurUnicast> emetteurs ;
	
	// une strutcure pour stocker tous les dessins et y accéder facilement 
	private HashMap<String, RemoteDessinServeur> sharedDessins = new HashMap<String, RemoteDessinServeur> () ;

	// une strutcure pour stocker tous les profils et y accéder facilement 
	private HashMap<String, RemoteProfilServeur> sharedProfils = new HashMap<String, RemoteProfilServeur> () ;

	// le constructeur du serveur : il le déclare sur un port rmi de la machine d'exécution
	protected EditeurServeur (String nomServeur, String nomMachineServeur, int portRMIServeur,	int portEmissionUpdate) throws RemoteException {
		this.name = nomServeur ;
		this.hostName = nomMachineServeur ;
		this.portRMI = portRMIServeur ;
		this.portEmission = portEmissionUpdate ;
		emetteurs = new ArrayList<EmetteurUnicast> () ;
		try {
			// attachement sur serveur sur un port identifié de la machine d'exécution
			Naming.rebind ("//" + nomMachineServeur + ":" + portRMIServeur + "/" + nomServeur, this) ;
			System.out.println ("pret pour le service") ;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println ("pb RMICentralManager") ;
		}
	}

	@Override
	public int getRMIPort () {
		return portRMI ;
	}

	// méthode permettant d'enregistrer un dessin sur un port rmi sur la machine du serveur :
	// - comme cela on pourra également invoquer directement des méthodes en rmi également sur chaque dessin
	public void registerDessin (RemoteDessinServeur dessin) {
		try {
			Naming.rebind ("//" + hostName + ":" + portRMI + "/" + dessin.getName (), dessin) ;
			System.out.println ("ajout de l'objet " + dessin.getName () + " sur le serveur " + hostName + "/"+ portRMI) ;
			System.out.println ("objet " + dessin.getName () + " enregistré sur le serveur " + hostName + "/"+ portRMI) ;
			System.out.println ("CLIENT/SERVER : objet " + dessin.getName () + " enregistré en x " + dessin.getX () + " et y " + dessin.getY ()) ;
		} catch (Exception e) {
			e.printStackTrace () ;
			try {
				System.out.println ("échec lors de l'ajout de l'objet " + dessin.getName () + " sur le serveur " + hostName + "/"+ portRMI) ;
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	// méthode permettant d'enregistrer un profil sur un port rmi sur la machine du serveur :
		// - comme cela on pourra également invoquer directement des méthodes en rmi également sur chaque profil
		public void registerProfil (RemoteProfilServeur profil) {
			try {
				Naming.rebind ("//" + hostName + ":" + portRMI + "/" + profil.getName (), profil) ;
				System.out.println ("ajout de l'objet " + profil.getName () + " sur le serveur " + hostName + "/"+ portRMI) ;
				System.out.println ("objet " + profil.getName () + " enregistré sur le serveur " + hostName + "/"+ portRMI) ;
				System.out.println ("CLIENT/SERVER : objet " + profil.getName ()) ;
			} catch (Exception e) {
				e.printStackTrace () ;
				try {
					System.out.println ("échec lors de l'ajout de l'objet " + profil.getName () + " sur le serveur " + hostName + "/"+ portRMI) ;
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L ;

	// méthodes permettant d'ajouter un nouveau dessin dans le système
	@Override
	public synchronized RemoteDessinServeur addDessin (int x, int y, int w, int h, CreateurDessin cd, Color color) throws RemoteException {
		// création d'un nouveau nom, unique, destiné à servir de clé d'accès au dessin
		// et création d'un nouveau dessin de ce nom et associé également à un émetteur multicast...
		// attention : la classe Dessin utilisée ici est celle du package serveur (et pas celle du package client)
		RemoteDessinServeur dessin = new DessinServeur ("dessin" + nextId (), emetteurs, cd, color) ;
		// enregistrement du dessin pour accès rmi distant
		registerDessin (dessin) ;
		// ajout du dessin dans la liste des dessins pour accès plus effice au dessin
		sharedDessins.put (dessin.getName (), dessin) ;
		System.out.println ("addDessin : sharedDessins = " + sharedDessins) ;
		// renvoi du dessin à l'éditeur local appelant : l'éditeur local récupèrera seulement un RemoteDessin
		// sur lequel il pourra invoquer des méthodes en rmi et qui seront relayées au référent associé sur le serveur  
		return dessin ;
	}
	
	// méthodes permettant d'ajouter un nouveau profil dans le système
	public synchronized RemoteProfilServeur addProfil ( int ranking, ProfilType type, String username) throws RemoteException {
		// création d'un nouveau nom, unique, destiné à servir de clé d'accès au profil
		// et création d'un nouveau profil de ce nom et associé également à un émetteur multicast...
		// attention : la classe Profil utilisée ici est celle du package serveur (et pas celle du package client)
		RemoteProfilServeur profil = new ProfilServeur ("profil" + nextId (), emetteurs,  ranking,  type, username) ;
		// enregistrement du profil pour accès rmi distant
		registerProfil (profil) ;
		// ajout du profil dans la liste des dessins pour accès plus efficace au dessin
		sharedProfils.put (profil.getName (), profil) ;
		System.out.println ("addProfil : sharedProfils = " + sharedProfils) ;
		// renvoi du dessin à l'éditeur local appelant : l'éditeur local récupèrera seulement un RemoteDessin
		// sur lequel il pourra invoquer des méthodes en rmi et qui seront relayées au référent associé sur le serveur  
		System.out.println(profil);
		return  profil;
	}

	// méthode permettant d'accéder à un proxy d'un des dessins
	@Override
	public synchronized RemoteDessinServeur getDessin (String name) throws RemoteException {
		System.out.println ("getDessin " + name + " dans sharedDessins = " + sharedDessins) ;
		return sharedDessins.get (name) ;
	}
	// méthode permettant d'accéder à un proxy d'un des profils
	@Override
	public synchronized RemoteProfilServeur getProfil (String name) throws RemoteException {
		System.out.println ("getProfil " + name + " dans sharedProfils = " + sharedProfils) ;
		return sharedProfils.get (name) ;
	}
	
	public synchronized void supprimerDessin(String name) throws RemoteException {
		sharedDessins.remove(name);
	}
	
	public synchronized void supprimerProfil(String name) throws RemoteException {
		sharedProfils.remove(name);
	}
	
	// méthode qui incrémente le compteur de dessins pour avoir un id unique pour chaque dessin :
	// dans une version ultérieure avec récupération de dessins à aprtir d'une sauvegarde, il faudra également avoir sauvegardé ce nombre...
	public int nextId () {
		idDessin++ ; 
		return idDessin ; 
	}

	// méthode permettant de récupérer la liste des dessins : utile lorsqu'un éditeur client se connecte 
	@Override
	public synchronized ArrayList<RemoteDessinServeur> getSharedDessins () throws RemoteException {
		return new ArrayList<RemoteDessinServeur> (sharedDessins.values()) ;
	}
	// méthode permettant de récupérer la liste des profils : utile lorsqu'un éditeur client se connecte 
	public synchronized ArrayList<RemoteProfilServeur> getSharedProfils () throws RemoteException {
		return new ArrayList<RemoteProfilServeur> (sharedProfils.values()) ;
	}

	// méthode indiquant quel est le port d'émission/réception à utiliser pour le client qui rejoint le serveur
	// on utilise une valeur arbitraitre de port qu'on incrémente de 1 à chaque arrivée d'un nouveau client
	// cela permettra d'avoir plusieurs clients sur la même machine, chacun avec un canal de communication distinct
	// sur un port différent des autres clients
	@Override
	public int getPortEmission (InetAddress adresseClient) throws RemoteException {
		EmetteurUnicast emetteur = new EmetteurUnicast (adresseClient, portEmission++) ;
		emetteurs.add (emetteur) ;
		return (emetteur.getPortEmission ()) ;
	}

	// méthode permettant juste de vérifier que le serveur est lancé
	@Override
	public void answer (String question) throws RemoteException {
		System.out.println ("SERVER : the question was : " + question) ;   
	}

}
