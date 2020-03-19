package com.sid.ovli;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;


public class GestionnaireFichier {
	private static File file = null;
	
	private static BufferedReader fichierEntree = null;
	private static FileInputStream fileInputStream = null;
	private static InputStreamReader inputStreamReader = null;
	
	private static BufferedWriter fichierSortie = null;
	private static FileOutputStream fileOutputStream = null;
	private static OutputStreamWriter outputStreamWriter = null;
	
	public static BufferedReader creerUnFichierEntree(String nomFichier){
		String infoMessage = null;
		String titleBar = null;
		try {
			//fichierEntree = new BufferedReader(new FileReader(nomFichier));
			
			//fichierEntree = new BufferedReader(new InputStreamReader(new FileInputStream(new File(nomFichier)),"UTF8"));
			
			//J'ai remplacé l'instruction précédente avec les suivantes pour pouvoir fermer les stream afin de libérer le fichier
			//àla fin du traitement.
			file = new File(nomFichier);
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream,"UTF8");
			fichierEntree = new BufferedReader(inputStreamReader);
						
		} catch (FileNotFoundException e) {
			infoMessage = "FileNotFoundException! \r\nLe fichier d'entrée M3U \"" + nomFichier + "\" n'a pas pu être crée";
			
			System.out.println(infoMessage);
			e.printStackTrace();
			
			titleBar = "Échec!";
			JOptionPane.showMessageDialog(null, infoMessage,titleBar, JOptionPane.INFORMATION_MESSAGE);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Le fichier d'entrée " + nomFichier + "n'a pas pu être créé");
			e.printStackTrace();
		}
		
		return fichierEntree;
		
	}
	
	
	public static BufferedWriter creerUnFichierSortie(String nomFichier){
		
		try {
			//fichierSortie = new BufferedWriter(new FileWriter(new File(nomFichier)));
			
			//fichierSortie = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(nomFichier)), "UTF8"));
			//J'ai remplacé l'instruction précédente avec les suivantes pour pouvoir fermer les stream afin de libérer le fichier
			//àla fin du traitement.
			file = new File(nomFichier);
			fileOutputStream = new FileOutputStream(file);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8");
			fichierSortie = new BufferedWriter(outputStreamWriter);
			
			//fileOutputStream.close();
			//outputStreamWriter.close();
			
		}  catch (IOException e) {
			System.out.println("Le fichier de sortie " + nomFichier + "des liens actifs n'a pas pu être créé");
			e.printStackTrace();
		}
		
		return fichierSortie;
	}

}
