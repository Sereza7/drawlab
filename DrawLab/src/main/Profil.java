package main;

import java.rmi.RemoteException;

import serveur.ProfilServeur;

public class Profil {
	
	public enum ProfilType {
		ADULTE, ENFANT;
	}
	ProfilType type;
	int classement;
	String nom;
	private ProfilServeur proxy;
	
	public Profil(ProfilServeur proxy) {
		this.proxy=proxy;
		try {
			nom = proxy.getName();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ProfilType getProfilType() {
		return type;
	}
	
}
