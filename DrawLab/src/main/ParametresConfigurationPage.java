package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import serveur.Parametres;
import serveur.RemoteGlobalServeur;
import serveur.RemoteProfilServeur;
import utils.SessionBottomBar;
import utils.TopBar;
import utils.WordsChooserWidget;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ParametresConfigurationPage extends JFrame{

	private static final long serialVersionUID = 1L;
	private Session session;
	private DefaultTableModel model = new DefaultTableModel();
	private ArrayList<String> wordList = new ArrayList<String>();
	private int seconds;
	
	
	public ParametresConfigurationPage(ClientLocal clientLocal, RemoteProfilServeur profil) {
		
		Parametres baseconfiguration = null;
		try {
			baseconfiguration = profil.getDefaultParameters();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.session = null;
		try {
			this.session = new Session(clientLocal, clientLocal.getServeur().addSession(profil),this);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		clientLocal.setSession(session);
		
		getContentPane().setLayout(new BorderLayout());
//		getContentPane().setLayout(null);
		
		TopBar topBar = new TopBar(this, profil, clientLocal, this);
		topBar.setTopText("Setup the upcoming session.");
		getContentPane().add(topBar, BorderLayout.NORTH);
		
		// Bottom bar part, don't need to move
		SessionBottomBar bottomBar = new SessionBottomBar(session);
		getContentPane().add(bottomBar, BorderLayout.SOUTH);
		
		JButton launchSessionButton = new JButton("Launch the playing session.");
		launchSessionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ParametresConfigurationPage.this.session.launchEditeurs(clientLocal, profil);
				
			}
		});
		getContentPane().add(launchSessionButton,BorderLayout.EAST);

		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
        panel.setBounds(50,120,1000,500);
        panel.setOpaque(true);
        panel.setBackground(Color.gray);
        
        
		
		JLabel timeLabel = new JLabel("Set time");
		timeLabel.setBounds(90,30,500,50);
		timeLabel.setFont(new Font ("Arial", Font.PLAIN, 40));
        panel.add(timeLabel);
        
        JTextField timeText = new JTextField("0");
        timeText.setBounds(90,90,100,40);
        timeText.setFont(new Font ("ARIAL", Font.PLAIN, 24));
        panel.add(timeText);
        
		JLabel minuteLabel = new JLabel("mimutes");
		minuteLabel.setBounds(200,90,100,40);
		minuteLabel.setFont(new Font ("ARIAL", Font.PLAIN, 24));
        panel.add(minuteLabel);
        
        JLabel wordConfigLabel = new JLabel("Create your word list!");
        wordConfigLabel.setBounds(90, 160, 500, 50);
        wordConfigLabel.setFont(new Font ("ARIAL", Font.PLAIN, 40));
        panel.add(wordConfigLabel);
        
        JTextField wordText = new JTextField("give a new word");
        wordText.setBounds(90,220,320,40);
        wordText.setFont(new Font ("ARIAL", Font.PLAIN, 24));
        panel.add(wordText);
        
        ImageIcon addIcon = new ImageIcon("add.png");
		addIcon.setImage(addIcon.getImage().getScaledInstance(27, 27, 27));
		
        JButton addButton =new JButton("Add word to list");
        addButton.setBounds(90,280,350,40);
        addButton.setIcon(addIcon);
        addButton.setFont(new Font ("ARIAL", Font.PLAIN, 22));
        panel.add(addButton);
        
        ImageIcon editIcon = new ImageIcon("edit.png");
//        System.out.println("image!!!!!!!!!!!!!!!"+editIcon);
		editIcon.setImage(editIcon.getImage().getScaledInstance(22, 22, 22));
		
        JButton editButton = new JButton("Update word to row selected");
        editButton.setBounds(90,340,350,40);
        editButton.setIcon(editIcon);
        editButton.setFont(new Font ("ARIAL", Font.PLAIN, 22));
        panel.add(editButton);
        
        ImageIcon deleteIcon = new ImageIcon("delete.png");
		deleteIcon.setImage(deleteIcon.getImage().getScaledInstance(23, 23, 23));
		
        JButton deleteRowButton =new JButton("Delete Row Selected");
        deleteRowButton.setBounds(90,400,350,40);
        deleteRowButton.setIcon(deleteIcon);
        deleteRowButton.setFont(new Font ("ARIAL", Font.PLAIN, 22));
        panel.add(deleteRowButton);
        
        JLabel listLabel = new JLabel("Word List");
        listLabel.setBounds(550, 30, 500, 50);
        listLabel.setFont(new Font ("ARIAL", Font.PLAIN, 40));
        panel.add(listLabel);
        
        Object[] columnNames = {"Word"};
        JTable wordTable = new JTable();
		
        JScrollPane tablePane = new JScrollPane(wordTable);
        tablePane.setBounds(550,90,400,280);
        panel.add(tablePane);
        
        model.setColumnIdentifiers(columnNames);
        wordTable.setModel(model);
        
        wordTable.setBackground(Color.LIGHT_GRAY);
        wordTable.setForeground(Color.black);
        Font font = new Font("", 1, 23);
        wordTable.setFont(font);
        wordTable.setRowHeight(30); 
        
        ImageIcon startIcon = new ImageIcon("start.png");
		startIcon.setImage(startIcon.getImage().getScaledInstance(25, 25, 25));
        
        JButton startButton =new JButton("Start");
        startButton.setBounds(780,390,170,50);
        startButton.setIcon(startIcon);
        startButton.setFont(new Font ("ARIAL", Font.PLAIN, 24));
//        startButton.setVerticalTextPosition(JButton.BOTTOM);
//        startButton.setHorizontalTextPosition(JButton.CENTER);
        panel.add(startButton);
        
        
        Object[] row = new Object[1];
        addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				row[0] = numText.getText();			
				row[0] = wordText.getText();
				
				model.addRow(row);
				getWordsToList(wordTable);
			}});
        
        
        deleteRowButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rowDelete = wordTable.getSelectedRow();
				if(rowDelete >= 0) {
					model.removeRow(rowDelete);
					getWordsToList(wordTable);
				}
				else {
					JOptionPane.showMessageDialog(null, "Select a row of word to delete !", "Delete error", JOptionPane.WARNING_MESSAGE);
					System.out.println("Delete Error");
				}
			}
        	
        });
        
        wordTable.addMouseListener(new MouseAdapter() {
        	
        	public void mouseClicked(MouseEvent e) {
        		int rowSelected = wordTable.getSelectedRow();
//        		numText.setText(model.getValueAt(rowSelected, 0).toString());
        		wordText.setText(model.getValueAt(rowSelected, 0).toString());
        	}
        	
        });
        
        editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rowEdit = wordTable.getSelectedRow();

				if(rowEdit >= 0) {
//					model.setValueAt(numText.getText(), rowEdit, 0);
					model.setValueAt(wordText.getText(), rowEdit, 0);
					getWordsToList(wordTable);
				}
				else {
					JOptionPane.showMessageDialog(null, "Select a row of word to edit !", "Edit error", JOptionPane.WARNING_MESSAGE);
					System.out.println("Edit Error");
				}
				
			}
        	
        });
        
        startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getSeconde(timeText);
				setWordList();
				setSeconds();
				ParametresConfigurationPage.this.session.setParameters(clientLocal, profil, wordList, seconds);
				ParametresConfigurationPage.this.session.launchEditeur(clientLocal, profil);			
				ParametresConfigurationPage.this.dispose();
				
			}
		});
        
		
		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1034, 636);
		setVisible (true) ;
	} 
	
    public ArrayList<String> getWordsToList(JTable table) {
//    	ArrayList<String> wordList = new ArrayList<String>();
    	for(int i=0; i < table.getRowCount(); i++) {
    		wordList.add(model.getValueAt(i, 0).toString());
    	}
    	
    	System.out.println("Table: "+ wordList);
    	
    	return wordList;
    }
    
    public int getSeconde(JTextField timePlay) {
    	System.out.println("time: "+ (Integer.valueOf(timePlay.getText())*60));
    	seconds = Integer.valueOf(timePlay.getText())*60;
    	return seconds;
    	
    	
    }
    
    public void setWordList() {
    	this.wordList = wordList;
    	
    }
    
    public void setSeconds() {
    	this.seconds = seconds;
    }

}


