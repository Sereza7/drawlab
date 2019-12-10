package login;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import communication.RecepteurUnicast;
import main.Dessin;
import main.Profil;
import serveur.ProfilServeur;
import serveur.RemoteEditeurServeur;

import javax.swing.ImageIcon;

public class Login extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font fontLabel = new Font("Arial",Font.PLAIN,32);
	Font fontButton = new Font("Arial",Font.PLAIN,20);
	
	private RemoteEditeurServeur serveur;
	private RecepteurUnicast recepteurUnicast;
	private Thread threadReceiver;
	private HashMap<String, Dessin> profils = new HashMap<String, Dessin> () ;
	private Profil profil;
	
	public Login (final String clientName, final String serveurName, final String serverHostName, final int serverRMIPort) {
		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1034, 636);
		init(clientName, serveurName, serverHostName, serverRMIPort);
		setVisible (true) ;
	}
	
	public void init (final String clientName, final String serveurName, final String serverHostName, final int serverRMIPort) {
		try {
			// tentative de connexion au serveur distant
			serveur = (RemoteEditeurServeur)Naming.lookup ("//" + serverHostName + ":" + serverRMIPort + "/" + serveurName) ;
			// invocation d'une ptremière méthode juste pour test
			serveur.answer ("hello from " + getName ()) ;
			// récupération de tous les dessins déjà présents sur le serveur
			ArrayList<ProfilServeur> remoteProfils = serveur.getSharedProfils() ;
			for (ProfilServeur rd : remoteProfils) {
				ajouterProfil(rd, rd.getName ()) ;
			}
		} catch (Exception e) {
			System.out.println ("probleme liaison CentralManager") ;
			e.printStackTrace () ;
			System.exit (1) ;
		}
		try {
			// création d'un récepteur unicast en demandant l'information de numéro port au serveur
			// en même temps on transmet au serveur l'adresse IP de la machine du client au serveur
			// de façon à ce que ce dernier puisse par la suite envoyer des messages de mise à jour à ce récepteur 
			recepteurUnicast = new RecepteurUnicast (InetAddress.getByName (clientName), serveur.getPortEmission (InetAddress.getByName (clientName))) ;
			// on aimerait bien demander automatiquement quel est l'adresse IP de la machine du client,
			// mais le problème est que celle-ci peut avoir plusieurs adresses IP (filaire, wifi, ...)
			// et qu'on ne sait pas laquelle sera retournée par InetAddress.getLocalHost ()...
			//recepteurUnicast = new RecepteurUnicast (serveur.getPortEmission (InetAddress.getLocalHost ())) ;
			recepteurUnicast.setProfilLocal (this) ;
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// création d'un Thread pour pouvoir recevoir les messages du serveur en parallèle des interactions avec les dessins
		threadReceiver = new Thread (recepteurUnicast) ;
		// démarrage effectif du Thread
		threadReceiver.start () ;
		
		
		
		
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		ImageIcon logo = new ImageIcon("img/logo.png");
		JLabel titleLabel = new JLabel(logo);
		titleLabel.setBounds(265, 40, 542, 180);
		panel.add(titleLabel);

		JLabel usernameLabel = new JLabel("Username :");
		usernameLabel.setBounds(311, 325, 200, 40);
		usernameLabel.setFont(fontLabel);
		panel.add(usernameLabel);
		JLabel passwordLabel = new JLabel("Password :");
		passwordLabel.setBounds(311, 375, 200, 40);
		passwordLabel.setFont(fontLabel);
		panel.add(passwordLabel);
		JTextField username = new JTextField();
		username.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {				
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (username.getText().length() == 0 || username.getText().equals(" ")) {
					JOptionPane.showMessageDialog(username, "Cannot leave username empty");
				}
			}
		});
		username.setBounds(511, 325, 250, 40);
		panel.add(username);
		JPasswordField password = new JPasswordField();
		password.setBounds(511, 375, 250, 40);
		panel.add(password);
		
		JButton register= new JButton("Register");
		register.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ajouterProfil ( username.getText());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		register.setBounds(381, 458, 130, 60);
		register.setFont(fontButton);
		panel.add(register);
		JButton signIn= new JButton("Sign In");
		signIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ajouterProfil ( username.getText());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});		
		signIn.setBounds(559, 458, 130, 60);
		signIn.setFont(fontButton);
		panel.add(signIn);
		
		getContentPane().add(panel);
	}
	
	public static void main (String[] args) {
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
		Login login = new Login (nomMachineClient, nomEditeurCollaboratif, nomMachineServeur, portRMIServeur);
	}
	

		
	public void ajouterProfil (ProfilServeur proxy, String proxyName) throws RemoteException {
		// création effective d'un profil
		if(proxy!=null) {
			this.profil=new Profil(proxy);
		}
	}
	public synchronized void ajouterProfil (String proxyName) throws RemoteException {
		System.out.println("ajout du profil " + proxyName);
		// on ne l'ajoute que s'il n'est pas déjà présent
		// - il pourrait déjà être présent si il avait été créé localement par une interaction dans cet éditeur local
		if (! profils.containsKey(proxyName)) {
			ProfilServeur proxy = null ;
			try {
				// récupération du proxy via une demande au serveur
				proxy = serveur.getProfil (proxyName);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			if (proxy == null) {
				System.out.println("proxy " + proxyName + " null");
			}
			// ajout du dessin
			ajouterProfil (proxy, proxyName) ;
		} else {
			System.out.println ("dessin " + proxyName + " était déjà présent") ;
		}
	};
}