package serveur ;

import java.io.Serializable ;
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject ;
import java.util.HashMap ;
import java.util.List;

import communication.EmetteurUnicast;

// classe de Dessin présente sur le serveur :
// - pour pouvoir invoquer des méthodes à distance, elle doit étendre UnicastRemote object ou implémenter l'interface Remote
// - ici elle fait les deux (car l'interface RemoteDessin étend l'interface Remote)
// - la classe doit également être Serializable si on veut la transmettre sur le réseau
public class SessionServeur extends UnicastRemoteObject implements Serializable, RemoteSessionServeur {

	// les attributs minimaux d'un Profil
	String name;
	// une strutcure pour stocker tous les profils et y accéder facilement 
	private HashMap<String, RemoteProfilServeur> utilisateurs = new HashMap<String, RemoteProfilServeur> () ;
	Parametres parametres;
	boolean enCours;
	

	// un attribut permettant au Dessin de diffuser directement ses mises à jour, sans passer par le serveur associé
	// - cet attribut n'est pas Serializable, du coup on le déclare transient pour qu'il ne soit pas inclu dans la sérialisation
	protected transient List<EmetteurUnicast> emetteurs ;

	
	/* (non-Javadoc)
	 * @see serveur.RemoteSessionServeur#setEmetteurs(java.util.List)
	 */
	@Override
	public void setEmetteurs (List<EmetteurUnicast> emetteurs) {
		this.emetteurs = emetteurs ;
	}

	private static final long serialVersionUID = 1L ;

	// constructeur du Dessin sur le serveur : il diffuse alors qu'il faut créer un nouveau dessin sur tous les clients 
	public SessionServeur (String name, List<EmetteurUnicast> senders, Parametres parametres, RemoteProfilServeur utilisateur) throws RemoteException {
		this.emetteurs = senders ;
		this.parametres = parametres ;
		this.name=name;
		this.enCours=false;
		this.utilisateurs= new HashMap<String, RemoteProfilServeur>();
		this.utilisateurs.put(utilisateur.getName(),utilisateur);
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("encours", this.enCours) ;
		hm.put ("utilisateurs", this.utilisateurs) ;
		hm.put ("parametres", this.parametres) ;
		
		for (EmetteurUnicast sender : senders) {
			sender.diffuseMessage ("Session", getName(), hm) ;
		}
	}

	/* (non-Javadoc)
	 * @see serveur.RemoteSessionServeur#getName()
	 */
	@Override
	public String getName () throws RemoteException {
		return name ;
	}

	/* (non-Javadoc)
	 * @see serveur.RemoteSessionServeur#getUtilisateurs()
	 */
	@Override
	public HashMap<String, RemoteProfilServeur> getUtilisateurs () throws RemoteException {
		return utilisateurs ;
	}
	/* (non-Javadoc)
	 * @see serveur.RemoteSessionServeur#isEnCours()
	 */
	@Override
	public boolean isEnCours () throws RemoteException {
		return enCours ;
	}
	/* (non-Javadoc)
	 * @see serveur.RemoteSessionServeur#getParametres()
	 */
	@Override
	public Parametres getParametres () throws RemoteException {
		return parametres ;
	}
	
	/* (non-Javadoc)
	 * @see serveur.RemoteSessionServeur#setParametres(serveur.Parametres)
	 */
	@Override
	public void setParametres (Parametres parametres) throws RemoteException {
		//System.out.println (getName() + " setBounds : " + x + " " + y + " " + w + " " + h) ;
		this.parametres = parametres ;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("parametres", parametres) ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("ParametresSession", getName (), hm) ;
		}
	}
	/* (non-Javadoc)
	 * @see serveur.RemoteSessionServeur#addUtilisateur(serveur.ProfilServeur)
	 */
	@Override
	public void addUtilisateur (ProfilServeur utilisateur) throws RemoteException {
		this.utilisateurs.put(utilisateur.getName(),utilisateur);
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("utilisateur", utilisateur) ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("AddUserSession", getName (), hm) ;
		}
	}
	
	public void removeUtilisateur (ProfilServeur utilisateur) throws RemoteException {
		this.utilisateurs.remove(utilisateur.getName());
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put("session", this);
		hm.put ("utilisateur", utilisateur) ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("RemoveUserSession", getName (), hm) ;
		}
	}

	
	/* (non-Javadoc)
	 * @see serveur.RemoteSessionServeur#supprimer()
	 */
	@Override
	public void supprimer() throws RemoteException {
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("SupprimerSession", getName (), hm) ;
		}
		
	}


	

	

}