package main;

import java.io.Serializable;
import java.rmi.RemoteException;

import editeurs.Dessin;
import serveur.RemoteDessinServeur;

public interface CreateurDessin extends Serializable{
	
	public Dessin creerDessin(RemoteDessinServeur proxy) throws RemoteException;
	
	public Dessin creerDessin();

}
