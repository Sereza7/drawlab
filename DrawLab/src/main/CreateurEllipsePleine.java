package main;

import java.rmi.RemoteException;

import editeurs.Dessin;
import editeurs.FullEllipse;
import serveur.RemoteDessinServeur;

public class CreateurEllipsePleine implements CreateurDessin{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Dessin creerDessin() {
		return new FullEllipse();
	}

	@Override
	public Dessin creerDessin(RemoteDessinServeur proxy) throws RemoteException {
		return new FullEllipse(proxy);
	}

}
