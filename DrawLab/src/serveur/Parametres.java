package serveur;

import java.io.Serializable;
import java.util.ArrayList;

public class Parametres implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int initialTimerLength;
	ArrayList<String> words;
	
	public Parametres() {
		this.initialTimerLength=1800;
		this.words= new ArrayList<String>();
	}
	
	public int getInitialTimerLength() {
		return initialTimerLength;
	}

	public ArrayList<String> getWords() {
		return words;
	}

	public void setInitialTimerLength(int initialTimerLength) {
		this.initialTimerLength = initialTimerLength;
	}

	public void setWords(ArrayList<String> words) {
		this.words = words;
	}

	
}
