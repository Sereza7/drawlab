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
	boolean isLoggedOn = false;
	public RemoteProfilServeur proxy;
	Parametres defaultparameters;
	
	public Profil(RemoteProfilServeur proxy) {
		this.proxy=proxy;
		try {
			username = proxy.getUserName();
			classement = proxy.getClassement();
			type = proxy.getProfilType();
			isLoggedOn = proxy.isLoggedOn();
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
	public void isLoggedOn(boolean b) {
		this.isLoggedOn=b;
	}
}
