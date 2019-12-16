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
	private TopBar topBar;
	
	public JoinPage(ClientLocal clientLocal, Profil profil, TopBar topBar) {
		this.clientLocal = clientLocal;
		this.session=null;
		this.bottomBar=null;
		this.profil = profil;
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		setTitle("DrawLab");
		setSize(1034, 636);
		setVisible (true) ;
		
		this.topBar = topBar;
		topBar.setTopText("Select your session!");
		getContentPane().add(topBar, BorderLayout.NORTH);
		
		
		
		ArrayList<RemoteSessionServeur> data = null;
		try {
			data = clientLocal.getServeur().getSharedSessions();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		DefaultListModel<RemoteSessionServeur> listModel = new DefaultListModel<RemoteSessionServeur>();
		for (RemoteSessionServeur remoteSession : data) {
			listModel.addElement(remoteSession);
		}
		JList<RemoteSessionServeur> sessionList = new JList<RemoteSessionServeur>(listModel);
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
		this.setVisible(true);
	}
	public void resetSession() {
		if(this.session!=null) {
			this.session.removeUser(JoinPage.this.profil);
			JoinPage.this.getContentPane().remove(JoinPage.this.bottomBar);
			this.session=null;
		}
	}
	public void setSession(RemoteSessionServeur remoteSession) {
		resetSession();
		clientLocal.setSession(new Session(clientLocal, remoteSession,this));
		clientLocal.getSession().createUser(profil);
		bottomBar = new SessionBottomBar(clientLocal.getSession());
		getContentPane().add(bottomBar,BorderLayout.SOUTH);
		topBar.setTopText("Waiting...");
		revalidate();
	}
}
