package main;


public class Profil {
	
	public enum ProfilType {
		ADULTE, ENFANT;
	}
	ProfilType type;
	int classement;
	String nom;
	
	public Profil(String name) {
		nom = name;
		
	}
	
	public ProfilType getProfilType() {
		return type;
	}
	
}
