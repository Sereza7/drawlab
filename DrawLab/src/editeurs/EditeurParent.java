package editeurs;

import serveur.RemoteGlobalServeur;
import serveur.RemoteProfilServeur;
import main.ClientLocal;

public class EditeurParent extends Editeur {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditeurParent(RemoteGlobalServeur serveur, ClientLocal clientLocal, RemoteProfilServeur profil, String dessinADeviner) {
		super(serveur, clientLocal, profil);
		super.topBar.setTopText("Draw a "+dessinADeviner);
		
	}

}
