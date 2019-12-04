package main ;

public class MainEditeurClient {

	// main permettant de lancer un éditeur local 
	public static void main (String [] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		// le nom de la machine qui héberge le serveur distant
		String serverHostName = "localhost" ;
		// le numro de port sur lequel est déclaré le serveur distant
		int serverRMIPort = 2010 ;
		// le nom du serveur distant
		String sharedWorldName = "EditeurCollaboratif" ;
		// le nom de l'éditeur local (ne sert pas à grand chose, du moins pour le moment)
		String clientName = "editeur" ;
		System.out.println ("Connexion à un serveur avec les caractéristiques :") ;
		System.out.println ("nom du client : " + clientName) ;
		System.out.println ("nom du serveur distant : " + serverHostName) ;
		System.out.println ("port rmi du serveur : " + serverRMIPort) ;
		System.out.println ("nom de l'univers partagé : " + sharedWorldName) ;
		// instanciation d'un client déporté qui fera le lien avec le navigateur
		new Editeur (clientName, sharedWorldName, serverHostName, serverRMIPort) ;

	}

}
