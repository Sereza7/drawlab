package utils;

import java.awt.Color;
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
import javax.swing.JLabel;
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
	private RemoteProfilServeur profil;
	private ClientLocal clientLocal;
	private Accueil accueil;
	private ParametresConfigurationPage parametresConfigurationPage;
	private Editeur editeur;
	private int type;

	public TopBar(Accueil accueil, RemoteProfilServeur profil, ClientLocal clientLocal, int type) {
		this.accueil = accueil;
		init(profil, clientLocal, type); 
	}
	
	public TopBar(ParametresConfigurationPage parametresConfigurationPage, RemoteProfilServeur profil, ClientLocal clientLocal, int type) {
		this.parametresConfigurationPage = parametresConfigurationPage;
		init(profil, clientLocal, type); 
	}
	
	public TopBar(Editeur editeur, RemoteProfilServeur profil, ClientLocal clientLocal, int type) {
		this.editeur = editeur;
		init(profil, clientLocal, type); 
	}
	
	public void init(RemoteProfilServeur profil, ClientLocal clientLocal, int type) {
//		this.profil = profil;
//		this.clientLocal = clientLocal;
//		this.type = type;
		
		this.setBackground(Color.WHITE);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
//		this.setBounds(20, 20, 994, 50);
		BufferedImage  image = null;
		Image scaledImage;
		JLabel picLabelLogo;
		
		JPanel logo = new JPanel();
		logo.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
		logo.setBackground(Color.WHITE);
//		logo.setBounds(0, 0, 50, 50);
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
//		topText.setBounds(125, 20, 600, 50);
		this.add(topText);
		
		JPanel btnGroup = new JPanel();
		btnGroup.setBackground(Color.WHITE);
		JLabel rank = new JLabel("Rank: 1");
		rank.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		rank.setBackground(Color.WHITE);
//		rank.setBounds(750, 20, 85, 20);
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
//		btnTroPhy.setBounds(835, 20, 50, 50);
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
//		btnSoundOrShare.setBounds(900, 20, 50, 50);
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
//		btnLogOut.setBounds(965, 20, 50, 50);
		btnLogOut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
//					RemoteProfilServeur profil = getProfil();
//					ClientLocal clientLocal = getClientLocal();
					clientLocal.getServeur().supprimerProfil(profil.getUserName());
					new Login(clientLocal);
//					int type = getType();
					if (type == 0) {
						getAccueil().dispose();
					} else if (type == 1) {
						getParametresConfigurationPage().dispose();
					} else if (type == 2) {
						getEditeur().dispose();
					}
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
	
//	public int getType() {
//		return this.type;
//	}
	
	public ParametresConfigurationPage getParametresConfigurationPage() {
		return this.parametresConfigurationPage;
	}
	
	public Editeur getEditeur() {
		return this.editeur;
	}
	
//	public RemoteProfilServeur getProfil() {
//		return this.profil;
//	}
//	
//	public ClientLocal getClientLocal() {
//		return this.clientLocal;
//	}
	
	public JLabel getTopText() {
		return topText;
	}

	public void setTopText(String text) {
		this.topText.setText(text);;
	}
	
}
