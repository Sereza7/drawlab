package editeurs;

public class EditeurParent extends Editeur {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditeurParent(String clientName, String serveurName, String serverHostName, int serverRMIPort, String dessinADeviner) {
		super(clientName, serveurName, serverHostName, serverRMIPort, "Draw a "+dessinADeviner);
		
		
	}

}
