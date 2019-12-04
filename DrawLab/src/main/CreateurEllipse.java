package main;

import java.rmi.RemoteException;

import serveur.RemoteDessinServeur;

public class CreateurEllipse implements CreateurDessin{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Dessin creerDessin() {
		return new Ellipse();
	}

	@Override
	public Dessin creerDessin(RemoteDessinServeur proxy) throws RemoteException {
		return new Ellipse(proxy);
	}

}
