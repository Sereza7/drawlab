package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import main.Profil.ProfilType;

public interface RemoteProfilServeur extends Remote{

	String getName() throws RemoteException;

	String getUserName() throws RemoteException;
	
	Parametres getDefaultParameters() throws RemoteException;

	int getClassement() throws RemoteException;

	ProfilType getProfilType() throws RemoteException;

	void setRanking(int rank) throws RemoteException;

	void supprimer() throws RemoteException;

}