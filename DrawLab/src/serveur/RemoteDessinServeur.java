package serveur;

import java.awt.Color;
import java.rmi.Remote ;
import java.rmi.RemoteException ;

import main.CreateurDessin;

// interface qui décrit les services offerts par un proxy de Dessin (exécutés côté serveur, utilisés côté client) :
// - quand un client demandera à accéder à distance à un tel dessin, il récupérera un proxy de ce dessin
// - il sera possible d'invoquer des méthodes sur ce proxy
// - les méthodes seront en fait exécutées côté serveur, sur le référent 

public interface RemoteDessinServeur extends Remote {

   void setLocation (int x, int y) throws RemoteException ;
   void setBounds (int x, int y, int w, int h) throws RemoteException ;
   void setZOrder(int currentOrder) throws RemoteException;
   String getName () throws RemoteException ;
   int getX () throws RemoteException ;
   int getY () throws RemoteException ;
   int getZ() throws RemoteException;
   int getWidth () throws RemoteException ;
   int getHeight () throws RemoteException ;
   CreateurDessin getCreateurDessin() throws RemoteException;
   Color getColor() throws RemoteException;
   void setZ(int z) throws RemoteException;
   void supprimer() throws RemoteException;


}
