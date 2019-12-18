package utils;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.Profil;
import main.ProfilListener;
import main.Session;

public class SessionBottomBar extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Icon defaultImage;
	public SessionBottomBar(Session session) {
		setLayout(new FlowLayout());
		setBackground(Color.WHITE);
		ArrayList<ProfilInterface> profilInterfaces = new ArrayList<ProfilInterface>();
		
		BufferedImage defaultUserImage = null;
        try {
        	defaultUserImage = ImageIO.read(new File("img/defaultUserPicture.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		defaultImage = new ImageIcon(defaultUserImage.getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		
		for (Profil profil : session.getUsers()) {
			ProfilInterface profilInterface = new ProfilInterface(profil, defaultImage);
			profilInterfaces.add(profilInterface);
			add(profilInterface);
		};
		session.addProfilListener(new ProfilListener() {
			
			@Override
			public void removedUser(Profil profil) {
				ProfilInterface localProfilInterface = null;
				for (ProfilInterface profilInterface : profilInterfaces) {
					if(profilInterface.profil.equals(profil)) {
						localProfilInterface = profilInterface;
					}
				}
				if (localProfilInterface==null) {
					System.out.println("There is no corresponding profile in the list displayed.");
					return;
				}
				profilInterfaces.remove(localProfilInterface);
				remove(localProfilInterface);
				return;
			}
			
			@Override
			public void addedUser(Profil profil) {
				ProfilInterface newProfilInterface = new ProfilInterface(profil, defaultImage);
				profilInterfaces.add(newProfilInterface);
				add(newProfilInterface);
				revalidate();
				return;
			}
		});
	}
}
