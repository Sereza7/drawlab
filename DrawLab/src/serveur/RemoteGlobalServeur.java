package serveur;

import java.awt.Color;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import main.CreateurDessin;
import main.Profil.ProfilType;

public interface RemoteGlobalServeur extends Remote{

	// méthodes permettant d'ajouter un nouveau dessin dans le système
	RemoteDessinServeur addDessin(int x, int y, int w, int h, CreateurDessin cd, Color color) throws RemoteException;

	// méthodes permettant d'ajouter un nouveau profil dans le système
	RemoteProfilServeur addProfil(int ranking, ProfilType type, String username, Parametres parametres) throws RemoteException;

	// méthodes permettant d'ajouter une nouvelle session dans le système
	RemoteSessionServeur addSession ( RemoteProfilServeur utilisateur)  throws RemoteException;

	// méthode permettant d'accéder à un proxy d'un des dessins
	RemoteDessinServeur getDessin(String name) throws RemoteException;

	// méthode permettant d'accéder à un proxy d'un des profils
	RemoteProfilServeur getProfil(String name) throws RemoteException;

	void supprimerDessin(String name) throws RemoteException;

	void supprimerProfil(String name) throws RemoteException;

	// méthode permettant de récupérer la liste des dessins : utile lorsqu'un éditeur client se connecte 
	ArrayList<RemoteDessinServeur> getSharedDessins() throws RemoteException;

	// méthode permettant de récupérer la liste des profils : utile lorsqu'un client se connecte 
	ArrayList<RemoteProfilServeur> getSharedProfils() throws RemoteException;
	HashMap<String, RemoteProfilServeur> getSharedProfilsHM () throws RemoteException;
	
	ArrayList<RemoteSessionServeur> getSharedSessions () throws RemoteException;
	HashMap<String, RemoteSessionServeur> getSharedSessionsHM () throws RemoteException;

	// méthode indiquant quel est le port d'émission/réception à utiliser pour le client qui rejoint le serveur
	// on utilise une valeur arbitraitre de port qu'on incrémente de 1 à chaque arrivée d'un nouveau client
	// cela permettra d'avoir plusieurs clients sur la même machine, chacun avec un canal de communication distinct
	// sur un port différent des autres clients
	int getPortEmission(InetAddress adresseClient) throws RemoteException;

	// méthode permettant juste de vérifier que le serveur est lancé
	void answer(String question) throws RemoteException;


}