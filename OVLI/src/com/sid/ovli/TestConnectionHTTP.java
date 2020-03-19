package com.sid.ovli;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.UIManager;


public class TestConnectionHTTP {

    public static void main(String[] args) {
      URL uneURL = null;
      int ch;
      
      try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	  } catch (Exception evt) {}

      BufferedReader fichierEntree = null;
      BufferedWriter fichierSortieM3U = null;
      BufferedWriter fichierSortieTvList = null; 
      //BufferedWriter maListeM3U = null;
      
      String ligne;
      String nomChaineM3U = null;
      String nomChaineTvList = null;
      String titreTvList = null;
      String maListeM3U = "maListeM3U.m3u"; 
      int nbLignesFichierEntree = 0;
      int nbChainesFonctionnelles = 0;
   
      
      try{
    	  
    	  // Affichage du help de la commande
    	  if (args.length == 1){
    		  if (args[0].equals("-help")){
    	  		  System.out.println("\r\n\r\n");
    			  System.out.println("     La syntaxe du programme TestConnectionHTTP est:");
    			  System.out.println("     TestConnectionHTTP <source> <sortie M3U> <sortie tvlist> <titre tvlist>");
    			  System.out.println();
			      System.out.println("     <source> peut être un ficier ou bien un lien URL");
			      System.out.println("     Si c'est un lien URL, ça génère le fichier maListeM3U.m3u");
    			  System.out.println("\r\n\r\n");
			  return;
    		  }
    	  }
    	  
    	  
    	  
    	  
    	  //Ouverture du fichier d'entrées
    	  if (args.length != 0){
    		  if(args[0].startsWith("http")){
    			  //maListeM3U = new BufferedWriter(new FileWriter(new File("./maListeM3U.m3u")));
    			  
    			  // À partir d'un lien internet, on remplit un fichier M3U
    			  recupereListeLienM3U(args[0],maListeM3U);
    			  
    			  // On va lire à partir du fichier remplit par le lien
    			  fichierEntree = new BufferedReader(new FileReader(maListeM3U));
    			  
    		  } else{
    			  // On lit à partir d'un fichier m3u personnel
    			  fichierEntree = new BufferedReader(new FileReader(args[0]));
    		  	}
    	  } else {
    		  //fichierEntree = new BufferedReader(new FileReader("U:/Outils/Vérification lien HTTP/testtvlistes.m3u8"));
    		  
    		  // On lit à partir d'un fichier hardcodé et qui n'est pas passé en paramètre.
    		  fichierEntree = new BufferedReader(new FileReader("testtvlistes.m3u8"));
    	  }	  
    	  
    	  if (args.length == 2){
    		  //On passe en paramètre un fichier M3U qui va contenir juste les liens actifs
    		  fichierSortieM3U = new BufferedWriter(new FileWriter(new File(args[1])));
    	  } else {
    		  //fichierSortieM3U = new BufferedWriter(new FileWriter(new File("U:/Outils/Vérification lien HTTP/listeFonctionnelle.m3u")));
    		  
    		  // On force un fichier M3U en écriture qui va contenir juste les liens actifs 
    		  fichierSortieM3U = new BufferedWriter(new FileWriter(new File("./listeFonctionnelle.m3u")));
    	  }	
    	  
    	  //Création du fichier des liens fonctionnels sous format TVLIST
    	  if (args.length == 3){
    		  fichierSortieTvList = new BufferedWriter(new FileWriter(new File(args[2])));
    	  } else {
    		  //fichierSortieTvList = new BufferedWriter(new FileWriter(new File("U:/Outils/Vérification lien HTTP/tvlist.txt")));
    		  fichierSortieTvList = new BufferedWriter(new FileWriter(new File("./tvlist.txt")));
    	  }
    	  
    	 //Création du titre du fichier tvlist.txt
    	  if (args.length == 4){
    		  titreTvList = "title:" + args[3];
    	  } else {
    		  // le titre sera 'test' par défaut
    		  titreTvList = "title:test";
    	  }
    	  
    	   
    	  //Écriture de la 1ière ligne d'un fichier M3U
    	  fichierSortieM3U.write("#EXTM3U\r\n");
    	  
    	 //Écriture de la 1ière ligne d'un fichier tvlist.txt
    	  fichierSortieTvList.write(titreTvList + "\r\n");
    	  
    	 //Compter le nombre de lignes du fichier d'entrée
    	  while ((ligne = fichierEntree.readLine()) != null){
    		  if (ligne.contains("http") || ligne.contains("HTTP")){
    			  nbLignesFichierEntree ++;
    		  }
    	  }
    	  System.out.println("\r\n\r\n\r\n");
    	  System.out.println("*****************************    Nombre total de chaînes = " + nbLignesFichierEntree + "    *****************************");
    	  System.out.println("\r\n\r\n");
    	  // On doit le fermer pour le réouvrir plus tard afin de recommencer sa lecture depuis le début.
    	  fichierEntree.close();
    	  
      }
      catch(FileNotFoundException exc){
    	  if (fichierEntree == null){
    		  System.out.println("Erreur d'ouverture du fichier fichierEntree");
    	  } else if(fichierSortieM3U == null){
    		  System.out.println("Erreur d'ouverture du fichier fichierSortieM3U");
    	  } else if (fichierSortieTvList == null){
    		  System.out.println("Erreur d'ouverture du fichier fichierSortieTvList");
    	  }
      }
      catch(IOException ioe){
    	  System.out.println("Erreur de création de fichier");
      }
      
      
      
      
      
      // Réouverture du fichier d'entrée car on l'a déjà fermer lorsqu'on a finit de compter 
      // son nombre total de liens m3u.
      try{
    	//Ouverture du fichier d'entrées
    	  if (args.length != 0){
    		  if(args[0].startsWith("http")){
    			//maListeM3U = new BufferedWriter(new FileWriter(new File("./maListeM3U.m3u")));
    			  
    			// À partir d'un lien internet, on remplit un fichier M3U  
  			  	recupereListeLienM3U(args[0],maListeM3U);
  			  	
  			  	// On va lire à partir du fichier remplit par le lien
  			  	fichierEntree = new BufferedReader(new FileReader(maListeM3U));
  			  	
    		  } else{
    			  // On lit à partir d'un fichier m3u personnel
    			  fichierEntree = new BufferedReader(new FileReader(args[0]));
    		  	}
    	  	} else {
    	  			//fichierEntree = new BufferedReader(new FileReader("U:/Outils/Vérification lien HTTP/testtvlistes.m3u8"));
    	  		
    	  			// On lit à partir d'un fichier hardcodé et qui n'est pas passé en paramètre.
    	  			fichierEntree = new BufferedReader(new FileReader("testtvlistes.m3u8"));
    	  	  }
    	  
    	  
    	  
    	  
    	  
    	  	// Lecture du fichier d'entrée ligne par ligne et ne garder que les liens actifs dans un fichier 
          	while ((ligne = fichierEntree.readLine()) != null){     
	      		if (ligne.contains("#EXTM3U")){
					System.out.println(ligne);
					continue;
           		} else if (ligne.contains("#EXTINF")){ 
           					nomChaineM3U = ligne;
                            nomChaineTvList = ligne.substring(ligne.indexOf(",")+1,ligne.length());
           					
                            System.out.print(nomChaineM3U + " - " + nomChaineTvList + " - ");
           					
           			   } else if(ligne.contains("http") || ligne.contains("HTTP")) {
           				   try {
           					   		uneURL = new URL(ligne);
           					   		HttpURLConnection connexion = (HttpURLConnection)uneURL.openConnection();
        		
           					   		//InputStream flux = connexion.getInputStream();
           					   		//System.out.println("Status de la connexion : " + connexion.getResponseMessage());
        		
           					   		if (connexion.getResponseCode() == HttpURLConnection.HTTP_OK){
           					   			/*	
										while ((ch=flux.read())!= -1)
            								System.out.print((char) ch);
        									flux.close();
           					   			 */ 
           					   			System.out.println(ligne);
           					   			fichierSortieM3U.write (nomChaineM3U + "\r\n");
           					   			fichierSortieM3U.write (ligne + "\r\n");
           					   			fichierSortieTvList.write(nomChaineTvList + "#link:" + ligne + "\r\n");
           					   			nbChainesFonctionnelles++;
           					   		}
           					   		connexion.disconnect();
           				   }
           				   catch(Exception e) {
           					   System.out.println(e);
           				   }
           			   }
          } // while
          
          System.out.println("\r\n\r\n");
          System.out.println("Nombre de chaînes fonctionnelles = " + nbChainesFonctionnelles + " / " + nbLignesFichierEntree);
          fichierEntree.close();
          fichierSortieM3U.close();
          fichierSortieTvList.close();
      }
      catch(IOException ioe){
    	  if (fichierEntree == null){
    		  System.out.println("Erreur de lecture ou de fermeture de fichier fichierEntree");
    	  } else if(fichierSortieM3U == null){
    		  System.out.println("Erreur de lecture ou de fermeture de fichier fichierSortieM3U");
    	  } else if (fichierSortieTvList == null){
    		  System.out.println("Erreur de lecture ou de fermeture de fichier fichierSortieTvList");
    	  }
      }
    }
    
    public static void recupereListeLienM3U(String lien, String listeM3u){

    	URL lienURL = null;
        int ch2 = 0;
        BufferedWriter maListeM3U = null;
        //lien = "http://121.167.132.238:9981/playlist";

        try {
        	maListeM3U = new BufferedWriter(new FileWriter(new File(listeM3u)));
		
        	//uneURL = new URL("https://pastebin.com/raw/YPYLBtyx");
        	lienURL = new URL(lien);

        	HttpURLConnection connexion = (HttpURLConnection)lienURL .openConnection();

        	InputStream flux = connexion.getInputStream();
        	System.out.println("Status de la connexion : " + connexion.getResponseMessage());

        	if (connexion.getResponseCode() == HttpURLConnection.HTTP_OK){
          		while ((ch2=flux.read())!= -1){
 	  			//System.out.print((char) ch2);
                		maListeM3U.write(ch2);
          		}
        	}
        	flux.close(); 
        	connexion.disconnect();
        	maListeM3U.close();
		
      	} 
      	catch(Exception e) {
          System.out.println(e);
      	}

    }
}