package main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Accueil extends JFrame {
	
	private String username;
	private int type;
	private Font fontLabel = new Font("Arial", Font.PLAIN, 40);

	public Accueil (String username, int type) {
		this.username = username;
		this.type = type;
		
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
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		
		JPanel newRoomPanel = new JPanel();
		newRoomPanel.setLayout(null);
		newRoomPanel.setBounds(144, 81, 249, 238);
		newRoomPanel.setBackground(Color.WHITE);
		newRoomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		ImageIcon newRoomIcon = new ImageIcon("img/joinRoom.png");
		JLabel newRoomImage = new JLabel(newRoomIcon);
		newRoomImage.setBounds(93, 54, 64, 64);
		newRoomPanel.add(newRoomImage);
		JLabel newRoomLabel = new JLabel("New Room", JLabel.CENTER);
		newRoomLabel.setFont(fontLabel);
		newRoomLabel.setBounds(0, 128, 241, 47);
		newRoomPanel.add(newRoomLabel);
		panel.add(newRoomPanel);
		
		JPanel joinRoomPanel = new JPanel();
		joinRoomPanel.setLayout(null);
		joinRoomPanel.setBounds(549, 81, 249, 238);
		joinRoomPanel.setBackground(Color.WHITE);
		joinRoomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		ImageIcon joinRoomIcon = new ImageIcon("img/joinRoom.png");
		JLabel joinRoomImage = new JLabel(joinRoomIcon);
		joinRoomImage.setBounds(93, 54, 64, 64);
		joinRoomPanel.add(joinRoomImage);
		JLabel joinRoomLabel = new JLabel("Join Room", JLabel.CENTER);
		joinRoomLabel.setFont(fontLabel);
		joinRoomLabel.setBounds(0, 128, 241, 47);
		joinRoomPanel.add(joinRoomLabel);
		panel.add(joinRoomPanel);
		
		getContentPane().add(panel);
	}
	
	
	
}
