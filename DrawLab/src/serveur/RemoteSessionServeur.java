package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import communication.EmetteurUnicast;

public interface RemoteSessionServeur extends Remote{

	void setEmetteurs(List<EmetteurUnicast> emetteurs);

	String getName() throws RemoteException;

	HashMap<String, RemoteProfilServeur> getUtilisateurs() throws RemoteException;

	boolean isEnCours() throws RemoteException;

	Parametres getParametres() throws RemoteException;

	void setParametres(Parametres parametres) throws RemoteException;

	void addUtilisateur(ProfilServeur utilisateur) throws RemoteException;
	
	void removeUtilisateur(ProfilServeur utilisateur) throws RemoteException;

	void supprimer() throws RemoteException;

}