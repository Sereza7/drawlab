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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import communication.RecepteurUnicast;
import main.Accueil;
import main.Profil;
import main.Profil.ProfilType;
import serveur.RemoteEditeurServeur;
import serveur.RemoteProfilServeur;

public class Login extends JFrame {
	
	Font fontLabel = new Font("Arial",Font.PLAIN,32);
	Font fontButton = new Font("Arial",Font.PLAIN,20);
	
	public Login () {
		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1034, 636);
		init();
		setVisible (true) ;
	}
	
	public void init (final String clientName, final String serveurName, final String serverHostName, final int serverRMIPort) {
		try {
			// tentative de connexion au serveur distant
			serveur = (RemoteEditeurServeur)Naming.lookup ("//" + serverHostName + ":" + serverRMIPort + "/" + serveurName) ;
			
			// invocation d'une ptremière méthode juste pour test
			serveur.answer ("hello from " + getName ()) ;
			// récupération de tous les dessins déjà présents sur le serveur
			ArrayList<RemoteProfilServeur> remoteProfils = serveur.getSharedProfils() ;
			for (RemoteProfilServeur rd : remoteProfils) {
				ajouterProfil(rd.getName (), rd) ;
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
			recepteurUnicast.setLoginLocal (this) ;
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
					JOptionPane.showMessageDialog(username, "Cannot leave username empty", "Username is empty", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		username.setBounds(511, 325, 250, 40);
		username.setFont(fontButton);
		panel.add(username);
		JPasswordField password = new JPasswordField();
		password.setBounds(511, 375, 250, 40);
		panel.add(password);
		
		JButton register= new JButton("Register");
		register.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean alreadyInUse = usernameIsExisted(username);
				if (!alreadyInUse) {
					try {
						serveur.addProfil(0, cbParent.isSelected() ? ProfilType.ADULTE : ProfilType.ENFANT, username.getText());
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(username, "This username is already taken", "Username is existed", JOptionPane.ERROR_MESSAGE);
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
				boolean alreadyInUse = usernameIsExisted(username);
				Profil profil = getProfilByUsername(username);
				if (alreadyInUse) {
					Accueil accueil = new Accueil(profil.getUserName(), profil.getProfilType());
					dispose();
				} else {
					JOptionPane.showMessageDialog(username, "Please input the correct username", "Username is incorrect", JOptionPane.ERROR_MESSAGE);	
				}
			}
		});
		signIn.setBounds(559, 458, 130, 60);
		signIn.setFont(fontButton);
		panel.add(signIn);
		
		getContentPane().add(panel);
	}
	
	public boolean usernameIsExisted (JTextField username) {
		boolean alreadyInUse = false;
		for (Profil profilIter: profils.values()) {
			if (profilIter.getUserName().equals(username.getText())) {
				alreadyInUse=true;
			}
		}
		return alreadyInUse;
	}
	
	public Profil getProfilByUsername (JTextField username) {
		Profil profil = null;
		for (Profil profilIter: profils.values()) {
			if (profilIter.getUserName().equals(username.getText())) {
				profil = profilIter;
			}
		}
		return profil;
	}
	
	public static void main (String[] args) {
		Login login = new Login ();
	}
}
