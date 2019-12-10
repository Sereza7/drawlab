package login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LoginListener implements ActionListener {

	private Login login;
	private JTextField username;
	
	public LoginListener (Login login, JTextField username) {
		this.login = login;
		this.username = username;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (username.getText().equals("test")) {
			JFrame frame = new JFrame();
			frame.setSize(1000,600);
			frame.setTitle("test");
			frame.setVisible(true);
			
			login.dispose();
		} else {
			JOptionPane.showMessageDialog(username, "Please input the correct username", "Username is incorrect", JOptionPane.ERROR_MESSAGE);	
		}
	}
}
