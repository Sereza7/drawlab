package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;

import serveur.RemoteSessionServeur;
import utils.SessionBottomBar;
import utils.TopBar;

public class JoinPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private ClientLocal clientLocal;
	private Session session;
	private Profil profil;
	private SessionBottomBar bottomBar;
	
	public JoinPage(ClientLocal clientLocal, Profil profil) {
		this.clientLocal = clientLocal;
		this.session=null;
		this.bottomBar=null;
		this.profil = profil;
		
		
		
		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1034, 636);
		setVisible (true) ;
		
		TopBar topBar = new TopBar(new Accueil(clientLocal, profil.proxy), profil.proxy, clientLocal, this);
		topBar.setTopText(profil.getUserName()+" Select your session!");
		getContentPane().add(topBar, BorderLayout.NORTH);
		
		
		
		ArrayList<RemoteSessionServeur> data = null;
		try {
			data = clientLocal.getServeur().getSharedSessions();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		DefaultListModel<Session> listModel = new DefaultListModel<Session>();
		for (RemoteSessionServeur remoteSession : data) {
			listModel.addElement(new Session(remoteSession));
		}
		JList<Session> sessionList = new JList<Session>(listModel);
		getContentPane().add(sessionList, BorderLayout.CENTER);
		
		JButton joinButton = new JButton("Join");
		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JoinPage.this.resetSession();

				JoinPage.this.setSession(sessionList.getSelectedValue());
			}
		});
		getContentPane().add(joinButton,BorderLayout.EAST);
	}
	public void resetSession() {
		if(this.session!=null) {
			this.session.removeUser(JoinPage.this.profil);
			JoinPage.this.getContentPane().remove(JoinPage.this.bottomBar);
			this.session=null;
		}
	}
	public void setSession(Session session) {
		resetSession();
		this.session= session;
		session.addUser(profil);
		bottomBar = new SessionBottomBar(JoinPage.this.session);
		getContentPane().add(JoinPage.this.bottomBar,BorderLayout.SOUTH);
		setVisible(true);
	}
}
