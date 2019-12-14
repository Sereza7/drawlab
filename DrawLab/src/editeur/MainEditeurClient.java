package editeur ;

import main.ClientLocal;

public class MainEditeurClient {

	// main permettant de lancer un éditeur local 
	public static void main (String [] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		// le nom de la machine qui héberge le serveur distant
		//String nomMachineServeur = "10.29.227.68" ;
		String nomMachineServeur = "localhost" ; // mettre l'adresse IP de votre serveur ici
		// le numro de port sur lequel est déclaré le serveur distant
		int portRMIServeur = 2010 ;
		// le nom du serveur distant
		String nomEditeurCollaboratif = "EditeurCollaboratif" ;
		// le nom de l'éditeur local (ne sert pas à grand chose, du moins pour le moment)
		//String nomMachineClient = "10.29.227.68" ;
		String nomMachineClient = "localhost" ; // mettre l'adresse IP de votre client ici
		System.out.println ("Connexion à un serveur avec les caractéristiques :") ;
		System.out.println ("machine du client : " + nomMachineClient) ;
		System.out.println ("nom du serveur distant : " + nomMachineServeur) ;
		System.out.println ("port rmi du serveur : " + portRMIServeur) ;
		System.out.println ("nom de l'univers partagé : " + nomEditeurCollaboratif) ;
		// instanciation d'un client déporté qui fera le lien avec le navigateur
		
		new ClientLocal(nomMachineClient, nomEditeurCollaboratif, nomMachineServeur, portRMIServeur);

	}

}
