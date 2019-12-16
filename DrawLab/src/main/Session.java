package main;

import java.rmi.RemoteException;
import java.util.ArrayList;

import editeurs.Editeur;
import serveur.RemoteDessinServeur;
import serveur.RemoteGlobalServeur;
import serveur.RemoteProfilServeur;

public class Session{
	private Editeur editeur;
	private RemoteGlobalServeur serveur;
	private ArrayList<Profil> users;
	private boolean enCours;
	private ArrayList<String> wordList;
	private int seconds;
	
	private ArrayList<ProfilListener>profilListeners;

	
	public Session(RemoteGlobalServeur serveur, Profil profil) {
		this.serveur=serveur;
		this.editeur=null;
		this.users= new ArrayList<Profil>();
		this.users.add(profil);
		this.profilListeners= new ArrayList<ProfilListener>();
		this.wordList = new ArrayList<String>();
		this.seconds = 0;
	}
	
	public void addProfilListener(ProfilListener profilListener){
		this.profilListeners.add(profilListener);
	}
	
	public void addUser(Profil profil) {
		//adds locally a user in the session
		this.users.add(profil);
		for (ProfilListener profilListener : profilListeners)
			profilListener.addedUser(profil);
	}
	public void removeUser(Profil profil) {
		//removes locally a user from the session
		this.users.remove(profil);
		for (ProfilListener profilListener : profilListeners)
			profilListener.removedUser(profil);
	}
	public ArrayList<Profil> getUsers(){
		return users;
	}
	
	public void setParameters(ClientLocal clientLocal, RemoteProfilServeur profil, ArrayList<String> wordList, int seconds ) {
		this.wordList = wordList;
		this.seconds = seconds;
	}
	
	public void launchEditeur(ClientLocal clientLocal, RemoteProfilServeur profil) {
		this.editeur= new Editeur(serveur, clientLocal, profil);
		this.setEnCours(true);
	}
	
	private void setEnCours(boolean b) {
		this.enCours=b;
		
	}

	public void saveImage() {
		// TODO Auto-generated method stub
		this.editeur.getZdd().saveImage();
	}

	public void ajouterDessin(RemoteDessinServeur proxy, String proxyName, int x, int y, int w, int h) {
		try {
			this.editeur.getZdd().ajouterDessin(proxy, proxyName, x,y, w,h);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ajouterDessin(String proxyName, int x, int y, int w, int h) {
		try {
			System.out.println(this.editeur);
			this.editeur.getZdd().ajouterDessin(proxyName, x,y, w,h);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void supprimerDessin(String name) {
		try {
			this.editeur.getZdd().supprimerDessin(name);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void objectUpdateZOrder(String objectName, int z) {
		this.editeur.getZdd().objectUpdateZOrder(objectName, z);
		
	}

	public void objectUpdateLocation(String objectName, int x, int y) {
		this.editeur.getZdd().objectUpdateLocation(objectName, x, y);
	}

	public void objectUpdateBounds(String objectName, int x, int y, int w, int h) {
		this.editeur.getZdd().objectUpdateBounds(objectName, x, y, w, h);
	}

}
