package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.rmi.RemoteException;

import javax.swing.JPanel;

import serveur.RemoteDessinServeur;

public abstract class Dessin extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean isSelected=false;
	private int clickX;
	private int clickY;
	private int initialMouseX;
	private int initialMouseY;
	private int deltaX;
	private int deltaY;
	private int initialWidth;
	private int initialHeight;
	private boolean motion;
	private int motionCorner; //1=haut gauche, 2=bas droite, 3=bas gauche, 4=haut droite
	private FullRectangle selectCorner1;
	private FullRectangle selectCorner2;
	private FullRectangle selectCorner3;
	private FullRectangle selectCorner4;
	private Color normalColor;
	private Color selectedColor;
	private ClickListener clickListener = new ClickListener();
	private DisplacementListener displacementListener = new DisplacementListener();
	private OrderListener orderListener = new OrderListener();
	private SelectedClickListener selectedClickListener = new SelectedClickListener();
	private SuppressionListener suppressionListener = new SuppressionListener();
	private RemoteDessinServeur proxy;
	
	public Dessin() {
		this.setOpaque(false);
		setVisible(true);
	}
	
	public Dessin(RemoteDessinServeur proxy) throws RemoteException {
		this.setLayout(null);
		selectCorner1 = new FullRectangle();
		selectCorner2 = new FullRectangle();
		selectCorner3 = new FullRectangle();
		selectCorner4 = new FullRectangle();
		this.add(selectCorner1);
		this.add(selectCorner2);
		this.add(selectCorner3);
		this.add(selectCorner4);
		this.addMouseListener(clickListener);
		this.addMouseWheelListener(orderListener);
		this.addMouseMotionListener(displacementListener);
		this.addMouseListener(selectedClickListener);
		this.addKeyListener(suppressionListener);
		this.proxy = proxy;
		this.setOpaque(false);
		setVisible(true);
	}
	
	public RemoteDessinServeur getProxy() {
		return this.proxy;
	}
	
	/**
	 * @return the clickX
	 */
	public int getClickX() {
		return clickX;
	}



	/**
	 * @param clickX the clickX to set
	 */
	public void setClickX(int clickX) {
		this.clickX = clickX;
	}



	/**
	 * @return the clickY
	 */
	public int getClickY() {
		return clickY;
	}



	/**
	 * @param clickY the clickY to set
	 */
	public void setClickY(int clickY) {
		this.clickY = clickY;
	}
	
	

	public int getInitialMouseX() {
		return initialMouseX;
	}

	public void setInitialLocationX(int initialMouseX) {
		this.initialMouseX = initialMouseX;
	}

	public int getInitialMouseY() {
		return initialMouseY;
	}

	public void setInitialMouseY(int initialLocationY) {
		this.initialMouseY = initialLocationY;
	}

	/**
	 * @return the deltaX
	 */
	public int getDeltaX() {
		return deltaX;
	}



	/**
	 * @param deltaX the deltaX to set
	 */
	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}



	/**
	 * @return the deltaY
	 */
	public int getDeltaY() {
		return deltaY;
	}



	/**
	 * @param deltaY the deltaY to set
	 */
	public void setDeltaY(int deltaY) {
		this.deltaY = deltaY;
	}
	
	



	public int getInitialWidth() {
		return initialWidth;
	}

	public void setInitialWidth(int initialWidth) {
		this.initialWidth = initialWidth;
	}

	public int getInitialHeight() {
		return initialHeight;
	}

	public void setInitialHeight(int initialHeight) {
		this.initialHeight = initialHeight;
	}
	
	
	
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	
	
	
	public void setProxyBounds(int x, int y, int w, int h) {
		setBounds(x, y, w, h);
		if(this.getProxy()!=null) {
			try {
				proxy.setBounds(x, y, w, h);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		paint(getGraphics());
	}
	
	
	public void setProxyLocation(int x, int y) {
		setLocation(x, y);
		if(this.getProxy()!=null) {
			try {
				proxy.setLocation(x, y);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		paint(getGraphics());
		
	}
	
	public void setProxyZOrder(int notches) {
		if(this.getParent()!=null) {
			int currentOrder = this.getParent().getComponentZOrder(this);
			if(notches>0) {
				this.getParent().setComponentZOrder(Dessin.this, Math.max(currentOrder-1,0));
				this.getParent().repaint();
			}
			else if(notches<0){
				this.getParent().setComponentZOrder(Dessin.this, Math.min(currentOrder+1, Dessin.this.getParent().getComponentCount()-1));
				this.getParent().repaint();
				
			}
			currentOrder = this.getParent().getComponentZOrder(this);
			if(this.getProxy()!=null) {
				try {
					proxy.setZOrder(currentOrder);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			for(Dessin d: ((ZoneDeDessin) this.getParent()).getDessins()) {
				try {
					d.getProxy().setZ(this.getParent().getComponentZOrder(d));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			paint(getGraphics());
		}
	}
	
	public void supprimerProxy() {
		//this.setVisible(false);
		if(this.getProxy()!=null) {
			try {
				proxy.supprimer();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void select() {
		isSelected=true;
		this.requestFocus();
		if(normalColor==null) {
			normalColor=getForeground();
			if(normalColor.getRed()>200 && normalColor.getBlue()<100) {
				selectedColor=Color.blue;
			}
			else {
				selectedColor=Color.red;
			}
		}
		selectCorner1.setBounds(0,0,15,15);
		selectCorner1.setVisible(true);
		selectCorner2.setBounds(getWidth()-15,getHeight()-15,15,15);
		selectCorner2.setVisible(true);
		selectCorner3.setBounds(0,getHeight()-15,15,15);
		selectCorner3.setVisible(true);
		selectCorner4.setBounds(getWidth()-15,0,15,15);
		selectCorner4.setVisible(true);
		setForeground(selectedColor);
		paint(getGraphics());
		((ZoneDeDessin) getParent()).setSelectedDessin(Dessin.this);
	}
	
	public void deselect() {
		this.selectCorner1.setVisible(false);
		this.selectCorner2.setVisible(false);
		this.selectCorner3.setVisible(false);
		this.selectCorner4.setVisible(false);
		this.setForeground(this.normalColor);
		paint(getGraphics());
		this.isSelected=false;
	}
	
	
	
	private class ClickListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			select();
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
	
	private class SelectedClickListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(isSelected) {
				setClickX(e.getX());
				setClickY(e.getY());
				setDeltaX(getWidth()-e.getX());
				setDeltaY(getHeight()-e.getY());
				setInitialLocationX(e.getLocationOnScreen().x);
				setInitialMouseY(e.getLocationOnScreen().y);
				setInitialWidth(getWidth());
				setInitialHeight(getHeight());
				if((e.getX()<15 ||Math.abs(e.getX()-getWidth())<15) && (e.getY()<15 || Math.abs(e.getY()-getHeight())<15)) {
					motion=false;
					if(e.getX()<15 && e.getY()<15) {
						motionCorner = 1;
					}
					else if(Math.abs(e.getX()-getWidth())<15 && Math.abs(e.getY()-getHeight())<15){
						motionCorner=2;
					}
					else if(e.getX()<15 && Math.abs(e.getY()-getHeight())<15) {
						motionCorner=3;
					}
					else {
						motionCorner=4;
					}
				}
				else {
					motion=true;
				}
			}
			else {
				try {
					((ZoneDeDessin)getParent()).beginDessin(e.getPoint(), getLocation());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
	
	
	
	private class DisplacementListener implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
			if(isSelected) {
				if(!motion) { 
					if(motionCorner==1) {
						setProxyBounds(getLocation().x-getClickX()+e.getX(),getLocation().y-getClickY()+e.getY(),-e.getLocationOnScreen().x+getInitialMouseX()+getInitialWidth(), -e.getLocationOnScreen().y+getInitialMouseY()+getInitialHeight());
					}
					else if(motionCorner==2){
						setProxyBounds(getLocation().x, getLocation().y, e.getX()+getDeltaX(), e.getY()+getDeltaY());
					}
					else if(motionCorner==3) {
						setProxyBounds(getLocation().x-getClickX()+e.getX(),getLocation().y,-e.getLocationOnScreen().x+getInitialMouseX()+getInitialWidth(), e.getLocationOnScreen().y-getInitialMouseY()+getInitialHeight());
					}
					else {
						setProxyBounds(getLocation().x, getLocation().y-getClickY()+e.getY(), e.getX()+getDeltaX(), -e.getLocationOnScreen().y+getInitialMouseY()+getInitialHeight());
					}
				}
				else {
					setProxyLocation(getLocation().x-getClickX()+e.getX(),getLocation().y-getClickY()+e.getY());
				}
				selectCorner1.setBounds(0,0,15,15);
				selectCorner2.setBounds(getWidth()-15,getHeight()-15,15,15);
				selectCorner3.setBounds(0,getHeight()-15,15,15);
				selectCorner4.setBounds(getWidth()-15,0,15,15);
				paint(getGraphics());
			}
			else {
				try {
					((ZoneDeDessin)getParent()).motionDessin(e.getPoint(), getLocation());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(isSelected) {
				if(((e.getX()>0 && e.getX()<15|| -e.getX()+getWidth()>0 && (-e.getX()+getWidth())<15) && (e.getY()>0 && e.getY()<15 || -e.getY()+getHeight()>0 && (-e.getY()+getHeight())<15))) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
			
		}
		
	}
	
	private class OrderListener implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if(isSelected) {
				int notches = e.getWheelRotation();
				Dessin.this.setProxyZOrder(notches);
				paint(getGraphics());
			}
		}
		
	}
	
	private class SuppressionListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(isSelected && e.getKeyCode()==KeyEvent.VK_DELETE) {
				Dessin.this.supprimerProxy();
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	
	

}
