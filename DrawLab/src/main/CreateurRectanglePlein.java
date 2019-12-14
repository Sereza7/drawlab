package main;

import java.rmi.RemoteException;

import editeur.Dessin;
import editeur.FullRectangle;
import serveur.RemoteDessinServeur;

public class CreateurRectanglePlein implements CreateurDessin{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Dessin creerDessin() {
		return new FullRectangle();
	}

	@Override
	public Dessin creerDessin(RemoteDessinServeur proxy) throws RemoteException {
		return new FullRectangle(proxy);
	}
}
