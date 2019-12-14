package main;


import java.awt.Color;

import javax.swing.JPanel;

import editeur.Dessin;

public class Preview extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CreateurDessin cd;
	
	public Preview() {
		super();
		this.setBackground(Color.white);
		this.setLayout(null);
	}
	
	
	
	public CreateurDessin getCd() {
		return cd;
	}

	public void setCd(CreateurDessin cd) {
		this.cd = cd;
		this.setDessin(this.getCd(), this.getForeground());
	}



	public void setDessin(CreateurDessin cd, Color color) {
		this.cd=cd;
		if(this.getComponentCount()==1) {
			this.remove(this.getComponent(0));
		}
		Dessin dessin = cd.creerDessin();
		dessin.setBounds(5, 5, this.getWidth()-10, this.getHeight()-10);
		this.setForeground(color);
		dessin.setForeground(color);
		this.add(dessin);
		this.repaint();
	}

}
