package com.sid.ovli;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.validator.routines.UrlValidator;

public class GestionnaireLiensIPTV {

	private String flux = null;
	private int nbLiens = 0;
	private Map<String,String>tableauDeLiens = null;
	
	public GestionnaireLiensIPTV(){
		
	}
	
	public GestionnaireLiensIPTV(String flux){
		this.flux = flux;
		getLiensDuFlux();
	}
	
	public InputStream getContenuLien(String url) throws MalformedURLException, IOException{
		String erreur = null;
		if (url.equals("") || url == null || !isUrlValide(url)){
			erreur = "L'url n'est pas valide!";
			return new ByteArrayInputStream(erreur.getBytes(StandardCharsets.UTF_8));
		} else{
			URL lienURL = new URL(url);

	        HttpURLConnection connexion = (HttpURLConnection)lienURL.openConnection();    	
				
			if (connexion.getResponseCode() == HttpURLConnection.HTTP_OK){
				InputStream flux = connexion.getInputStream();
				connexion.disconnect();
				return flux;
					
		   	} else{
		   		erreur = "L'url n'est pas active!";
				return new ByteArrayInputStream(erreur.getBytes(StandardCharsets.UTF_8));
		   	}
		}
	}
	
	private void getLiensDuFlux(){
		
		String[] tableauTemporaireDeLiens = null;
		String nomChaine = null;
		String lienChaine = null;
	
		 if (flux != null && !flux.equals("") && isLienM3U()){
			 tableauTemporaireDeLiens = flux.substring(8,flux.length()).split("#EXTINF:-1,");
			 for(int i=0;i<tableauTemporaireDeLiens.length;i++){
				 nomChaine = tableauTemporaireDeLiens[i].substring(0, tableauTemporaireDeLiens[i].indexOf("http"));
				 lienChaine = tableauTemporaireDeLiens[i].substring(tableauTemporaireDeLiens[i].indexOf("http"), tableauTemporaireDeLiens[i].length());
				 tableauDeLiens.put(nomChaine, lienChaine);
			 }
		 }
	}
	
	
	public int getNbLiensFlux(){
		return nbLiens;
	}
	
	public Map<String,String> getLiensFlux(){
		return tableauDeLiens;
	}
	
	public boolean isUrlValide(String url){
		UrlValidator urlValidator = new UrlValidator();
		if(urlValidator.isValid(url)){
			return true;
		} else{
			return false;
		}
	}
	
	public boolean isLienActif(String url){
		return true;
	}
	
	public boolean isLienM3U(){
		if (flux.startsWith("#EXTM3U")){
			return true;
		} else{
			return false;
		}
	}
}
