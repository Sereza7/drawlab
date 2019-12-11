package serveur;

import java.awt.Color;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import main.CreateurDessin;
import main.Profil.ProfilType;

public interface RemoteGlobalServeur extends Remote{

	int getRMIPort();

	// méthode permettant d'enregistrer un dessin sur un port rmi sur la machine du serveur :
	// - comme cela on pourra également invoquer directement des méthodes en rmi également sur chaque dessin
	void registerDessin(RemoteDessinServeur dessin);

	// méthode permettant d'enregistrer un profil sur un port rmi sur la machine du serveur :
	// - comme cela on pourra également invoquer directement des méthodes en rmi également sur chaque profil
	void registerProfil(RemoteProfilServeur profil);

	// méthodes permettant d'ajouter un nouveau dessin dans le système
	RemoteDessinServeur addDessin(int x, int y, int w, int h, CreateurDessin cd, Color color) throws RemoteException;

	// méthodes permettant d'ajouter un nouveau profil dans le système
	RemoteProfilServeur addProfil(int ranking, ProfilType type, String username) throws RemoteException;

	// méthode permettant d'accéder à un proxy d'un des dessins
	RemoteDessinServeur getDessin(String name) throws RemoteException;

	// méthode permettant d'accéder à un proxy d'un des profils
	RemoteProfilServeur getProfil(String name) throws RemoteException;

	void supprimerDessin(String name) throws RemoteException;

	void supprimerProfil(String name) throws RemoteException;

	// méthode qui incrémente le compteur de dessins pour avoir un id unique pour chaque dessin :
	// dans une version ultérieure avec récupération de dessins à aprtir d'une sauvegarde, il faudra également avoir sauvegardé ce nombre...
	int nextId();

	// méthode permettant de récupérer la liste des dessins : utile lorsqu'un éditeur client se connecte 
	ArrayList<RemoteDessinServeur> getSharedDessins() throws RemoteException;

	// méthode permettant de récupérer la liste des profils : utile lorsqu'un éditeur client se connecte 
	ArrayList<RemoteProfilServeur> getSharedProfils() throws RemoteException;

	// méthode indiquant quel est le port d'émission/réception à utiliser pour le client qui rejoint le serveur
	// on utilise une valeur arbitraitre de port qu'on incrémente de 1 à chaque arrivée d'un nouveau client
	// cela permettra d'avoir plusieurs clients sur la même machine, chacun avec un canal de communication distinct
	// sur un port différent des autres clients
	int getPortEmission(InetAddress adresseClient) throws RemoteException;

	// méthode permettant juste de vérifier que le serveur est lancé
	void answer(String question) throws RemoteException;

}