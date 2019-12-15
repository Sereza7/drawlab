package login;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Accueil;
import main.ClientLocal;
import main.Profil;
import main.Profil.ProfilType;
import serveur.Parametres;
import serveur.RemoteGlobalServeur;
import serveur.RemoteProfilServeur;


public class Login extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font fontLabel = new Font("Arial",Font.PLAIN,32);
	Font fontButton = new Font("Arial",Font.PLAIN,20);
	
	private HashMap<String, Profil> profils = new HashMap<String, Profil> () ;
	private JTextField username;
	private JCheckBox cbParent;
	private ClientLocal clientLocal; 
	
	public Login (ClientLocal clientLocal) {
		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1034, 636);
		init(clientLocal);
		setVisible (true) ;
	}
	
	public void init (ClientLocal clientLocal) {
		this.clientLocal=clientLocal;
		
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
		username = new JTextField();
		username.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {				
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (username.getText().length() == 0 || username.getText().equals(" ")) {
					JOptionPane.showMessageDialog(username, "Cannot leave username empty", "Username is empty", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		username.setBounds(511, 325, 250, 40);
		username.setFont(fontButton);
		panel.add(username);
		
		cbParent = new JCheckBox("Parent");
		cbParent.setBounds(768, 325, 114, 42);
		cbParent.setFont(fontButton);
		panel.add(cbParent);
		
		JPasswordField password = new JPasswordField();
		password.setBounds(511, 375, 250, 40);
		panel.add(password);
		
		JButton register= new JButton("Register");
		register.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				registerAction();
				}
		});
		register.setBounds(381, 458, 130, 60);
		register.setFont(fontButton);
		panel.add(register);
		JButton signIn= new JButton("Sign In");


		signIn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loginAction();
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
		
		new ClientLocal (nomMachineClient, nomEditeurCollaboratif, nomMachineServeur, portRMIServeur);
	}
	

		
	public void ajouterProfil (String proxyName, RemoteProfilServeur proxy) throws RemoteException {
		// création effective d'un profil sur le serveur
		if (proxy!=null) {
			Profil currentProfil = new Profil(proxy);
			profils.put(proxyName, currentProfil);
		}
		else {
			System.out.println("The proxy "+proxyName+" for the profil searched is null.");
		}
		
	}
	public synchronized void ajouterProfil (String proxyName) throws RemoteException {
		System.out.println("ajout du profil " + proxyName);
		// on ne l'ajoute que s'il n'est pas déjà présent
		// - il pourrait déjà être présent si il avait été créé localement par une interaction dans cet éditeur local
		if (! profils.containsKey(proxyName)) {
			System.out.println("proxy " + proxyName + " n'est pas présent en local. On va le chercher sur le serveur.");
			// ajout du dessin
			RemoteProfilServeur proxy=null;
			try {
				proxy = clientLocal.getServeur().getProfil(proxyName);
			}catch(RemoteException e) {
				e.printStackTrace();
			}
			System.out.println(proxy);
			ajouterProfil (proxyName, proxy) ;
			
		} else {
			System.out.println ("profil " + proxyName + " était déjà présent") ;
		}
	}
	public void loginAction() {
		boolean alreadyInUse = false;
		RemoteProfilServeur proxy = null;
		for (Profil profilIter: profils.values()) {
			if (profilIter.getUserName().equals(username.getText())) {
				alreadyInUse=true;
				proxy = profilIter.proxy;
			}
		}
		if (alreadyInUse) {
			try {
				if (!proxy.isLoggedOn()) {
					proxy.setLoggedOn(true);
					
					new Accueil(clientLocal, proxy);
					
					this.dispose();
				}
				else {
					System.out.println("This profile is already used by another client. Please change your profile or close this other client.");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("This username is not registered. Please register before loging in.");
			
		}
	}
	public void registerAction() {
		boolean alreadyInUse = false;
		for (Profil profilIter: profils.values()) {
			if (profilIter.getUserName().equals(username.getText())) {
				alreadyInUse=true;
			}
		}
		if (!alreadyInUse) {
			try {
				clientLocal.getServeur().addProfil(0, cbParent.isSelected() ? ProfilType.ADULTE : ProfilType.ENFANT, username.getText(), new Parametres());
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			System.out.println("This username is already taken.");
		}
	}
}

