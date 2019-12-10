package login;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

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
	
	public void init () {
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
		username.setBounds(511, 325, 250, 40);
		panel.add(username);
		JPasswordField password = new JPasswordField();
		password.setBounds(511, 375, 250, 40);
		panel.add(password);
		
		JButton register= new JButton("Register");
		register.setBounds(381, 458, 130, 60);
		register.setFont(fontButton);
		panel.add(register);
		JButton signIn= new JButton("Sign In");
		signIn.setBounds(559, 458, 130, 60);
		signIn.setFont(fontButton);
		panel.add(signIn);
		
		getContentPane().add(panel);
	}
	
	public static void main (String[] args) {
		Login login = new Login ();
	}
}
