package utils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.BinaryOperator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class WordsChooserWidget extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<String> words;
	private DefaultListModel<WordLine> wordsListModel;
	private class WordLine extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public WordLine(String word) {
			this.setLayout(new FlowLayout());
			this.add(new JLabel(word));
			JButton remove = new JButton();
			remove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WordsChooserWidget.this.words.remove(word);
					WordsChooserWidget.this.wordsListModel.removeElement(this);
				}
			});
		}
	}
	
	
	
	public WordsChooserWidget(ArrayList<String>words) {
		if (words.size()==0) {
			words.add("Your");
			words.add("words");
			words.add("here");
		}
		this.setLayout(new BorderLayout());
		this.add(new JLabel("Add your words and make them comma separated:"), BorderLayout.NORTH);
		
		String concatenatedWords = words.stream().reduce("", new BinaryOperator<String>() {
			
			@Override
			public String apply(String t, String u) {
				if (t.equals("")) {
					return u;
				}
				return t+","+u;
			}
		});
		JTextArea inputText = new JTextArea(concatenatedWords);
		this.add(inputText, BorderLayout.CENTER);
		
	}
	public WordsChooserWidget() {
		this(new ArrayList<String>());
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
 
        //Create and set up the content pane.
        
        JComponent newContentPane = new WordsChooserWidget();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}
}
