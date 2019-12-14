package main;

import java.rmi.RemoteException;

import editeur.Dessin;
import editeur.Rectangle;
import serveur.RemoteDessinServeur;

public class CreateurRectangle implements CreateurDessin{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Dessin creerDessin() {
		return new Rectangle();
	}

	@Override
	public Dessin creerDessin(RemoteDessinServeur proxy) throws RemoteException {
		return new Rectangle(proxy);
	}

}
