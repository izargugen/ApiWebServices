package com.sid.ovli;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.Scanner;

public class ConvertisseurCharset {
	
	private InputStream input; 
	private String inputCharsetName;
	private int tailleBuffer = 8192;
	private int nbLiens = 0;
	private int contenuLength = 0;
	
	
	public int getContenuLength() {
		return contenuLength;
	}

	public void setContenuLength(int contenuLength) {
		this.contenuLength = contenuLength;
	}

	public ConvertisseurCharset(){
		
	}
	
	public ConvertisseurCharset(int tailleBuffer){
		this.tailleBuffer = tailleBuffer;
	}
	
	public ConvertisseurCharset(InputStream input, String inputCharsetName){
		this.input = input;
		this.inputCharsetName = inputCharsetName;
	}
	
	public int getNbLiens(){
		return nbLiens;
	}
	
	public void setTailleBuffer(int tailleBuffer){
		this.tailleBuffer = tailleBuffer;
	}
	
	public String convertir(InputStream input, String inputCharsetName) throws UnsupportedEncodingException, IOException {
		// https://www.developpez.net/forums/d225600/java/general-java/langage/convertir-inputstream-utf8/
		
		String[] tableauDeLiens = null;
		byte[] buff = new byte[tailleBuffer];
	    int nbRead = 0;
	    String contenuConverti = "";
	    int tailleInput = input.toString().length();
	    setContenuLength(tailleInput);	
	    
				    
	    try {
	    	if(tailleInput < tailleBuffer){
	    		while((nbRead = input.read(buff)) > 0) {
	    			String sBuff = inputCharsetName == null ? new String(buff) : new String(buff, inputCharsetName);
	    			//System.out.println(sBuff);
	    			contenuConverti += sBuff.substring(0, nbRead).replaceAll("\u0000", "");
	    		}
	    	} else {
	    		System.out.println("Le contenu du lien est trop gros!");
	    		return "Le contenu du lien est trop gros! ( > " + tailleBuffer + " ).";
	    	}
				    	
		    } finally {
		        input.close();
		    }
				    
		    if (contenuConverti != null && !contenuConverti.equals("")){
		    	if (contenuConverti.contains("http")){
		    		tableauDeLiens = contenuConverti.split("http");
		    		nbLiens = tableauDeLiens.length;
		    		//System.out.println(contenuConverti);
		    		//lblNbTotalLiens.setText("Nombre total de liens = " + nbLignesFichierEntree);
		    	}
		    }
		    return contenuConverti;
	}
	
	public String inputStream2String(InputStream flux){
		Scanner s = new Scanner(flux).useDelimiter("\\A");
		String contenu = s.hasNext() ? s.next() : "";
		return contenu;
	}
}
