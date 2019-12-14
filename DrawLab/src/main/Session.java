package main;

import java.rmi.RemoteException;

import editeurs.Editeur;
import serveur.RemoteDessinServeur;
import serveur.RemoteGlobalServeur;

public class Session {
	private Editeur editeur;
	public Session(RemoteGlobalServeur serveur) {
		this.editeur= new Editeur(serveur);
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
