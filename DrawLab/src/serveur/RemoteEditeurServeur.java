package serveur ;

import java.awt.Color;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException ;
import java.util.ArrayList;

import main.CreateurDessin;
import main.Profil.ProfilType;

// interface qui décrit les services offerts par un proxy d'éditeur(côté serveur) :
// - quand un client demandera à accéder à distance à un tel éditeur, il récupérera un proxy de cet éditeur
// - il sera possible d'invoquer des méthodes sur ce proxy
// - les méthodes seront en fait exécutées côté serveur, sur le référent 

public interface RemoteEditeurServeur extends Remote {

   int getPortEmission (InetAddress adresseClient) throws RemoteException ;
   void answer (String question) throws RemoteException ;
   int getRMIPort () throws RemoteException ;
   RemoteDessinServeur addDessin (int x, int y, int w, int h, CreateurDessin cd, Color color) throws RemoteException ;
   RemoteProfilServeur addProfil ( int ranking, ProfilType type, String username, Parametres parametres) throws RemoteException;
   ArrayList <RemoteDessinServeur> getSharedDessins () throws RemoteException ;
   ArrayList <RemoteProfilServeur> getSharedProfils () throws RemoteException ;
   RemoteDessinServeur getDessin(String name) throws RemoteException ;
   RemoteProfilServeur getProfil (String name) throws RemoteException;
   void supprimerDessin(String name) throws RemoteException;
   void supprimerProfil(String name) throws RemoteException;

}
