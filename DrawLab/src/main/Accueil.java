package main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Profil.ProfilType;

public class Accueil extends JFrame {
	
	private String username;
	private Profil profil;
	private ProfilType profilType;
	private Font fontLabel = new Font("Arial", Font.PLAIN, 40);

	public Accueil (String username, ProfilType profilType) {
		this.username = username;
		this.profilType = profilType;

		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1034, 636);
		setLayout(null);
		init();
		setVisible (true) ;
	}
	
	public void init () {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(44, 89, 946, 477);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		
		JButton newRoom = new JButton("New Room", new ImageIcon("img/plus.jpg"));
		newRoom.setBounds(144, 81, 249, 238);
		newRoom.setFont(fontLabel);
		newRoom.setHorizontalTextPosition(JButton.CENTER); 
		newRoom.setVerticalTextPosition(JButton.BOTTOM);
		newRoom.setBackground(Color.WHITE);
		panel.add(newRoom);
		
		JButton joinRoom = new JButton("Join Room", new ImageIcon("img/users.png"));
		joinRoom.setBounds(549, 81, 249, 238);
		joinRoom.setFont(fontLabel);
		joinRoom.setHorizontalTextPosition(JButton.CENTER); 
		joinRoom.setVerticalTextPosition(JButton.BOTTOM);  
		panel.add(joinRoom);
		
		
		
		getContentPane().add(panel);
	}
	
	
	
}
