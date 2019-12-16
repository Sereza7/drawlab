package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import communication.EmetteurUnicast;

public interface RemoteSessionServeur extends Remote{

	String getName() throws RemoteException;

	HashMap<String, RemoteProfilServeur> getUtilisateurs() throws RemoteException;

	boolean isEnCours() throws RemoteException;

	Parametres getParametres() throws RemoteException;

	void setParametres(Parametres parametres) throws RemoteException;

	void addUtilisateur(RemoteProfilServeur remoteProfilServeur) throws RemoteException;
	
	void removeUtilisateur(RemoteProfilServeur utilisateur) throws RemoteException;

	void supprimer() throws RemoteException;

	void setEnCours(boolean b) throws RemoteException;

}