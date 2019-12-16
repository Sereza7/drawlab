package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Profil.ProfilType;
import serveur.RemoteGlobalServeur;
import serveur.RemoteProfilServeur;
import utils.TopBar;

public class Accueil extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private ProfilType type;
	private Font fontLabel = new Font("Arial", Font.PLAIN, 40);
	private RemoteProfilServeur profil;
	private ClientLocal clientLocal;
	
	public Accueil (ClientLocal clientLocal, RemoteProfilServeur profil) {
		this.clientLocal = clientLocal;
		this.profil = profil;
		try {
			this.username = profil.getUserName();
			this.type = profil.getProfilType();
		} catch (RemoteException e) {
			e.printStackTrace();
		}


		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1034, 636);
		setLayout(null);
		init();
		setVisible (true) ;
	}
	
	public void init () {
		getContentPane().setLayout(new BorderLayout());
		
		TopBar topBar = new TopBar(this, this.profil, this.clientLocal, this);
		topBar.setTopText("Bienvenue à toi  "+username+" !");
		getContentPane().add(topBar, BorderLayout.NORTH);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridBagLayout());
		buttons.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		getContentPane().add(buttons, BorderLayout.CENTER);
		
		GridBagConstraints constraints = new GridBagConstraints();
		if (this.type==ProfilType.ADULTE) {
			JButton newRoom = new JButton("New Room", new ImageIcon("img/plus.jpg"));
			newRoom.setFont(fontLabel);
			newRoom.setHorizontalTextPosition(JButton.CENTER); 
			newRoom.setVerticalTextPosition(JButton.BOTTOM);
			newRoom.addActionListener(new newRoomActionListener());
			
			
			
			constraints.fill = GridBagConstraints.VERTICAL;
			constraints.weighty = 1.0;
			constraints.insets = new Insets(10, 25, 10, 25);
			constraints.gridx = 0;
			constraints.gridy = 0;
			buttons.add(newRoom,constraints);
			
		}
		JButton joinRoom = new JButton("Join Room", new ImageIcon("img/users.png"));
		joinRoom.setFont(fontLabel);
		joinRoom.setHorizontalTextPosition(JButton.CENTER); 
		joinRoom.setVerticalTextPosition(JButton.BOTTOM);  
		joinRoom.addActionListener(new joinRoomActionListener());
		
		
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.gridx = 1;
		constraints.gridy = 0;
		buttons.add(joinRoom,constraints);
		
		
	}
	
	public class  newRoomActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//create a new parameter page (linked to a session)
			new ParametresConfigurationPage(clientLocal, profil);
			
			Accueil.this.dispose();
		}
		
	}
	public class  joinRoomActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new JoinPage(clientLocal, new Profil(profil));
			
			Accueil.this.dispose();
		}
		
	}
	
}
