package main;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;

import editeurs.Editeur;
import serveur.Parametres;
import serveur.RemoteDessinServeur;
import serveur.RemoteGlobalServeur;
import serveur.RemoteProfilServeur;
import serveur.RemoteSessionServeur;

public class Session{
	Editeur editeur;
	private RemoteSessionServeur proxy;
	private HashMap<String, Profil> users;
	boolean enCours = false;
	private Parametres parametres;
	public String name;
	
	private ArrayList<ProfilListener>profilListeners;
	private ClientLocal clientLocal;
	private RemoteProfilServeur profil;
	private JFrame setupWindow;

	private Session() {
		this.editeur=null;
		this.profilListeners= new ArrayList<ProfilListener>();
		this.users= new HashMap<String, Profil>();
	}
	
	public Session(ClientLocal clientLocal, RemoteSessionServeur remoteSession, JFrame setupWindow) {
		this();
		this.clientLocal=clientLocal;
		
		this.setupWindow=setupWindow;
		this.proxy = remoteSession;
		HashMap<String, RemoteProfilServeur> usersServ = null;
		try {
			this.name = proxy.getName();
			usersServ = remoteSession.getUtilisateurs();
			this.parametres = remoteSession.getParametres();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String key : usersServ.keySet()) {
			users.put(key, new Profil(usersServ.get(key)));
		}
	}

	public void addProfilListener(ProfilListener profilListener){
		this.profilListeners.add(profilListener);
	}
	
	public void addUser(Profil profil) {
		//adds locally a user in the session
		System.out.println("Added the user "+profil.username+" to the current Session.");
		this.users.put(profil.getUserName(), profil);
		for (ProfilListener profilListener : profilListeners)
			profilListener.addedUser(profil);

	}
	public void createUser(Profil profil) {
		try {
			this.proxy.addUtilisateur(profil.proxy);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeUser(Profil profil) {
		//removes locally a user from the session
		this.users.remove(profil);
		for (ProfilListener profilListener : profilListeners)
			profilListener.removedUser(profil);
	}
	public ArrayList<Profil> getUsers(){
		ArrayList<Profil>  arrayUsers = new ArrayList<Profil> ();
		for (String key : users.keySet()) {
			arrayUsers.add(users.get(key));
		}
		return arrayUsers;
	}
	
	public void launchEditeurs(ClientLocal clientLocal, RemoteProfilServeur profil) {
		try {
			this.proxy.setEnCours(true);
		} catch (RemoteException e) {e.printStackTrace();}
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
			e.printStackTrace();
		}
		
	}
	public void setEnCours(boolean b) {
		System.out.println("Launching the editors = "+Boolean.toString(b));
		this.enCours=b;
		if (this.enCours) {
			System.out.println("OPENED A NEW EDITOR");
			this.setupWindow.dispose();
			this.editeur = new Editeur(clientLocal.getServeur(), clientLocal);
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
