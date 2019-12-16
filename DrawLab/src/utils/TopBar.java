package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import login.Login;
import main.Accueil;
import main.ClientLocal;
import serveur.RemoteProfilServeur;
import editeurs.Editeur;
import main.ParametresConfigurationPage;

public class TopBar extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JLabel topText;
	private Accueil accueil;
	private ParametresConfigurationPage parametresConfigurationPage;
	private Editeur editeur;


	public TopBar(Accueil accueil, RemoteProfilServeur profil, ClientLocal clientLocal, JFrame parent) {
		this.accueil = accueil;
		init(profil, clientLocal, parent); 
	}
	
	public TopBar(ParametresConfigurationPage parametresConfigurationPage, RemoteProfilServeur profil, ClientLocal clientLocal, JFrame parent) {
		this.parametresConfigurationPage = parametresConfigurationPage;
		init(profil, clientLocal, parent); 
	}
	
	public TopBar(Editeur editeur, RemoteProfilServeur profil, ClientLocal clientLocal, JFrame parent) {
		this.editeur = editeur;
		init(profil, clientLocal, parent); 
	}
	
	public void init(RemoteProfilServeur profil, ClientLocal clientLocal, JFrame parent) {

		this.setBackground(Color.WHITE);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
		BufferedImage  image = null;
		Image scaledImage;
		JLabel picLabelLogo;
		
		JPanel logo = new JPanel();
		logo.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
		logo.setBackground(Color.WHITE);
		this.add(logo);
		try {
			image = ImageIO.read(new File("img/logo_small.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabelLogo = new JLabel(new ImageIcon(scaledImage));
		picLabelLogo.setBackground(Color.WHITE);
		logo.add(picLabelLogo);
		
		topText = new JLabel("What are they drawing?", JLabel.CENTER);
		topText.setFont(new Font("Monotype Corsiva", Font.PLAIN, 36));
		topText.setBackground(Color.WHITE);
		this.add(topText);
		
		JPanel btnGroup = new JPanel();
		btnGroup.setBackground(Color.WHITE);
		JLabel rank = new JLabel("Rank: 1");
		rank.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		rank.setBackground(Color.WHITE);
		btnGroup.add(rank);
		
		JPanel trophy = new JPanel();
		trophy.setBackground(Color.WHITE);
		btnGroup.add(trophy);
		
		try {
			image = ImageIO.read(new File("trophy.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JButton btnTroPhy = new JButton(new ImageIcon(scaledImage));
		trophy.add(btnTroPhy);
		
		JPanel soundOrShare = new JPanel();
		soundOrShare.setBackground(Color.WHITE);
		btnGroup.add(soundOrShare);
		try {
			image = ImageIO.read(new File("soundOrShare.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JButton btnSoundOrShare = new JButton(new ImageIcon(scaledImage));
		btnSoundOrShare.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(btnSoundOrShare, "Share to Facebook", "Success", JOptionPane.WARNING_MESSAGE);
			}
		});
		soundOrShare.add(btnSoundOrShare);
		
		JPanel logOut = new JPanel();
		logOut.setBackground(Color.WHITE);
		btnGroup.add(logOut);
		try {
			image = ImageIO.read(new File("logout.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JButton btnLogOut = new JButton(new ImageIcon(scaledImage));
		btnLogOut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					profil.setLoggedOn(false);
					clientLocal.recepteurUnicast.setLoginLocal(new Login(clientLocal));
					
					
					parent.dispose();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		logOut.add(btnLogOut);
		this.add(btnGroup);
	}
	
	public Accueil getAccueil() {
		return this.accueil;
	}
	
	public ParametresConfigurationPage getParametresConfigurationPage() {
		return this.parametresConfigurationPage;
	}
	
	public Editeur getEditeur() {
		return this.editeur;
	}
	
	public JLabel getTopText() {
		return topText;
	}

	public void setTopText(String text) {
		this.topText.setText(text);;
	}
	
}
