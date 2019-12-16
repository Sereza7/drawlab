package main;

import java.awt.BorderLayout;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;

import serveur.Parametres;
import serveur.RemoteGlobalServeur;
import serveur.RemoteProfilServeur;
import utils.SessionBottomBar;
import utils.TopBar;
import utils.WordsChooserWidget;

import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

public class ParametresConfigurationPage extends JFrame{

	private static final long serialVersionUID = 1L;
	private Session session;
	
	
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
		
		TopBar topBar = new TopBar(this, profil, clientLocal, this);
		topBar.setTopText("Parameter the upcoming session.");
		getContentPane().add(topBar, BorderLayout.NORTH);
		
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
		panel.setLayout(new BorderLayout(0, 0));
		
		WordsChooserWidget wordChooser = new WordsChooserWidget(baseconfiguration.getWords());
		panel.add(wordChooser,BorderLayout.CENTER);
		
		JPanel timer = new JPanel();
		timer.setLayout(new FlowLayout());
		panel.add(timer,BorderLayout.NORTH);
		
		timer.add(new JLabel("Time in seconds"));
		JSlider timeSlider = new JSlider(300, 3600, baseconfiguration.getInitialTimerLength());
		timer.add(timeSlider);
		
		
		
		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1034, 636);
		setVisible (true) ;
	} 
}
