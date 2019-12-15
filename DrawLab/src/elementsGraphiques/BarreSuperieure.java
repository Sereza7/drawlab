package elementsGraphiques;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class BarreSuperieure extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BarreSuperieure(String titre) {
		this.setBackground(Color.WHITE);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		BufferedImage  image = null;
		Image scaledImage;
		JLabel picLabelLogo;
		
		JPanel logo = new JPanel();
		logo.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
		logo.setBackground(Color.WHITE);
		this.add(logo);
		try {
			image = ImageIO.read(new File("img/logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		picLabelLogo = new JLabel(new ImageIcon(scaledImage));
		picLabelLogo.setBackground(Color.WHITE);
		logo.add(picLabelLogo);
		
		JLabel topText = new JLabel(titre);
		topText.setFont(new Font("Monotype Corsiva", Font.PLAIN, 36));
		topText.setBackground(Color.WHITE);
		this.add(topText);
		
		JLabel rank = new JLabel("Rank: 1");
		rank.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		rank.setBackground(Color.WHITE);
		this.add(rank);
		
		JPanel trophy = new JPanel();
		trophy.setBackground(Color.WHITE);
		this.add(trophy);
		
		try {
			image = ImageIO.read(new File("img/trophy.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel picTrophy = new JLabel(new ImageIcon(scaledImage));
		picTrophy.setBackground(Color.WHITE);
		trophy.add(picTrophy);
		
		JPanel soundOrShare = new JPanel();
		soundOrShare.setBackground(Color.WHITE);
		this.add(soundOrShare);
		try {
			image = ImageIO.read(new File("img/soundOrShare.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel picLabelSoundOrShare = new JLabel(new ImageIcon(scaledImage));
		picLabelSoundOrShare.setBackground(Color.WHITE);
		soundOrShare.add(picLabelSoundOrShare);
		
		JPanel logOut = new JPanel();
		logOut.setBackground(Color.WHITE);
		this.add(logOut);
		try {
			image = ImageIO.read(new File("img/logout.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		JLabel picLabellogOut = new JLabel(new ImageIcon(scaledImage));
		picLabellogOut.setBackground(Color.WHITE);
		logOut.add(picLabellogOut);
	}
	
	

}
