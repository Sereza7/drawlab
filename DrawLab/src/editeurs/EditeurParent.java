package editeurs;

import serveur.RemoteGlobalServeur;

public class EditeurParent extends Editeur {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditeurParent(RemoteGlobalServeur serveur, String dessinADeviner) {
		super(serveur);
		super.topBar.setTopText("Draw a "+dessinADeviner);
		
	}

}
