package utils;

import javax.swing.Icon;
import javax.swing.JLabel;

import main.Profil;

public class ProfilInterface extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Profil profil;
	public ProfilInterface(Profil profil, Icon image) {
		super(image);
		this.profil = profil;
		this.setToolTipText(profil.getUserName());
	}
}
