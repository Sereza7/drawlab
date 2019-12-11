package serveur ;

import java.io.Serializable ;
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject ;
import java.util.HashMap ;
import java.util.List;

import communication.EmetteurUnicast;
import main.Profil.ProfilType;

// classe de Dessin présente sur le serveur :
// - pour pouvoir invoquer des méthodes à distance, elle doit étendre UnicastRemote object ou implémenter l'interface Remote
// - ici elle fait les deux (car l'interface RemoteDessin étend l'interface Remote)
// - la classe doit également être Serializable si on veut la transmettre sur le réseau
public class ProfilServeur extends UnicastRemoteObject implements RemoteProfilServeur, Serializable {

	// les attributs minimaux d'un Profil
	ProfilType type;
	int classement;
	String username;
	String name;

	// un attribut permettant au Dessin de diffuser directement ses mises à jour, sans passer par le serveur associé
	// - cet attribut n'est pas Serializable, du coup on le déclare transient pour qu'il ne soit pas inclu dans la sérialisation
	protected transient List<EmetteurUnicast> emetteurs ;

	
	public void setEmetteurs (List<EmetteurUnicast> emetteurs) {
		this.emetteurs = emetteurs ;
	}

	private static final long serialVersionUID = 1L ;

	// constructeur du Dessin sur le serveur : il diffuse alors qu'il faut créer un nouveau dessin sur tous les clients 
	public ProfilServeur (String name, List<EmetteurUnicast> senders, int ranking, ProfilType type, String username) throws RemoteException {
		this.emetteurs = senders ;
		this.type = type ;
		this.name=name;
		this.username = username;
		this.classement = ranking;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("type", this.type) ;
		hm.put ("classement", new Integer(this.classement)) ;
		
		for (EmetteurUnicast sender : senders) {
			sender.diffuseMessage ("Profil", getName(), hm) ;
		}
	}

	/* (non-Javadoc)
	 * @see serveur.RemoteProfilServeur#getName()
	 */
	@Override
	public String getName () throws RemoteException {
		return name ;
	}
	/* (non-Javadoc)
	 * @see serveur.RemoteProfilServeur#getUserName()
	 */
	@Override
	public String getUserName() throws RemoteException{
		return username;
	}
	
	/* (non-Javadoc)
	 * @see serveur.RemoteProfilServeur#getClassement()
	 */
	@Override
	public int getClassement() throws RemoteException{
		return this.classement;
	}
	
	/* (non-Javadoc)
	 * @see serveur.RemoteProfilServeur#getProfilType()
	 */
	@Override
	public ProfilType getProfilType() throws RemoteException{
		return this.type;
	}

	
	/* (non-Javadoc)
	 * @see serveur.RemoteProfilServeur#setRanking(int)
	 */
	@Override
	public void setRanking (int rank) throws RemoteException {
		//System.out.println (getName() + " setBounds : " + x + " " + y + " " + w + " " + h) ;
		this.classement = rank ;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("classement", new Integer (rank)) ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("Classement", getName (), hm) ;
		}
	}

	/* (non-Javadoc)
	 * @see serveur.RemoteProfilServeur#supprimer()
	 */
	@Override
	public void supprimer() throws RemoteException {
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("SupprimerProfil", getName (), hm) ;
		}
		
	}


	

	

}