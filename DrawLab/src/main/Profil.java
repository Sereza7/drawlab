package main;

import java.rmi.RemoteException;

import serveur.Parametres;
import serveur.RemoteProfilServeur;

public class Profil {
	
	public enum ProfilType {
		ADULTE, ENFANT;
	}
	ProfilType type;
	int classement;
	String username;
	public RemoteProfilServeur proxy;
	Parametres defaultparameters;
	
	public Profil(RemoteProfilServeur proxy) {
		this.proxy=proxy;
		try {
			username = proxy.getUserName();
			classement = proxy.getClassement();
			type = proxy.getProfilType();
			this.defaultparameters=proxy.getDefaultParameters();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ProfilType getProfilType() {
		return type;
	}

	public String getUserName() {
		return this.username;
	}
	
}
