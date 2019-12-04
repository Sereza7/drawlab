package communication;

import java.io.IOException;
import java.io.ByteArrayOutputStream ;
import java.io.ObjectOutputStream ;
import java.io.Serializable ;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.util.HashMap ;

//---------------------------------------------------------------------
//classe permettant d'envoyer des messages diffusés à une adresse (en multicast)

public class MulticastSender implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// le port de diffusion
	private int portDiffusion ;
	
	// l'adresse du groupe de diffusion
	private String nomGroupe ;
	public String getNomGroupe () {
		return nomGroupe ;
	}

	// l'adresse système du groupe de diffusion
	private InetAddress adresseDiffusion ;
	
	// la socket de diffusion
	private transient MulticastSocket socketDiffusion ;

	// le constructeur du diffuseur à partir du nom de groupe et du port de diffusion 
	public MulticastSender (final String ng, final int portDiffusion) throws RemoteException {
		this.portDiffusion = portDiffusion ;
		nomGroupe = ng ;
		System.out.println ("Diffuseur sur le port " + portDiffusion + " a destination du groupe " + nomGroupe) ;
		adresseDiffusion = null ;
		socketDiffusion = null ;
		try {
			// détermination de l'adresse de multicast à partir de la chaine de caractères décrivant ce groupe
			adresseDiffusion = InetAddress.getByName (nomGroupe) ;
			// création d'une socket d'envoi de message, l'adresse de diffusion est indiquée seulement au moment de l'envoi
			socketDiffusion = new MulticastSocket () ;
			// détermination du "time to live" du message : pour "passer" un nombre de routeurs plus ou moins grand :
			// le TTL est diminué de 1 à chaqie passage du message sur un routeur
			socketDiffusion.setTimeToLive (64) ;
			//socketDiffusion.setLoopbackMode (true) ; // peut être utile en cas de problème de diffusion en local
		} catch (IOException e) {
			e.printStackTrace () ;
		}
		System.out.println ("socket : " + socketDiffusion.getLocalPort() + " " + socketDiffusion.getInetAddress ()) ;
	}

	// méthode envoyant un message de haut niveau en le traduisant en flux bas niveau
	public void diffuseMessage (String command, String name, HashMap<String, Object> hm) {
		// déclaration du flux de bas niveau
		ByteArrayOutputStream baos = new ByteArrayOutputStream () ;
		ObjectOutputStream oos ;
		try {
			// association d'un flux de plus haut niveau à ce flux de bas niveau
			oos = new ObjectOutputStream (baos) ;
			// écriture des objets, au format adéquat, dans le flux : il faudra les relire dans le même ordre !!!
			oos.writeObject (command) ;
			oos.writeObject (name) ;
			oos.writeObject (hm) ;
			oos.flush () ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		// création d'un paquet d'information à envoyer, à partir du flux de bas niveau
		DatagramPacket paquet = new DatagramPacket (baos.toByteArray (), baos.toByteArray ().length, adresseDiffusion, portDiffusion) ;
		try {
			// envoi effectif du message
			socketDiffusion.send (paquet) ;
		} catch (IOException e) {
			e.printStackTrace () ;
		}
	}

	public int getPortDiffusion () throws RemoteException {
		return (portDiffusion) ;
	}

	public InetAddress getAdresseDiffusion () throws RemoteException {
		return (adresseDiffusion) ;
	}

}
