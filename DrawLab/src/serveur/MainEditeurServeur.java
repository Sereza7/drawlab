package serveur;
import java.rmi.RemoteException ;
import java.rmi.registry.LocateRegistry ;

public class MainEditeurServeur {

	public static void main (String args[]) {
		String nomMachineRMI = "localhost" ;
		int portRMI = 2010 ;
		String nomServeur = "EditeurCollaboratif" ;
		// l'adresse IP de multicast doit appartenir à une plage bien précise : 224.0.0.0 à 239.255.255.255
		String nomGroupeObject = "230.21.11.19" ;
		int portDiffusionObject = 4020 ;
		System.out.println ("Création d'un serveur avec les caractéristiques :") ;
		System.out.println ("nom du serveur : " + nomMachineRMI) ;
		System.out.println ("port du serveur : " + portRMI) ;
		System.out.println ("nom du monde partagé : " + nomServeur) ;
		System.out.println ("nom du groupe de diffusion objets : " + nomGroupeObject) ;
		System.out.println ("port de diffusion objets : " + portDiffusionObject) ;
		try {
			// création d'un registre rmi sur le port rmi choisi, indispensable pour pouvoir attacher ensuite un serveur 
			LocateRegistry.createRegistry (portRMI) ;
			new EditeurServeur (nomServeur, nomMachineRMI, portRMI, nomGroupeObject, portDiffusionObject) ;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
