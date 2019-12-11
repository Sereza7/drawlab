package main;

import java.rmi.RemoteException;

import serveur.RemoteProfilServeur;

public class Profil {
	
	public enum ProfilType {
		ADULTE, ENFANT;
	}
	ProfilType type;
	int classement;
	String username;
	private RemoteProfilServeur proxy;
	
	public Profil(RemoteProfilServeur proxy) {
		this.proxy=proxy;
		try {
			username = proxy.getUserName();
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
