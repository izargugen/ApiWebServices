package com.sid.ovli;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.UrlValidator;


public class SourceURL implements SourceStream{

	//private String url = null;
	private String contenu = null;
	private Map<String,List<String>> listeDeChamps = null;
	private int streamLength = 0;
	
	public SourceURL() throws MalformedURLException, IOException{
		//this.url = url;
		//contenu = getContenuURL();
	}
	
	public void setContenu(String contenuConverti){
		this.contenu = contenuConverti;
	}
	
	public int getNbLiens() {
		if (contenu!= null || !contenu.equals("")){
		return contenu.split("#EXTINF").length-1;
		} else{
			return 0;	
		}
	}

	public Map<String, String> getListeLiens() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String,List<String>> getDescription(){
		return listeDeChamps;
	}
	
	public InputStream getContenu(String url) throws MalformedURLException,IOException{
		String erreur = null;
		
		if (url.equals("") || url == null || !isUrlValide(url)){
			erreur = "L'url n'est pas valide!";
			return new ByteArrayInputStream(erreur.getBytes(StandardCharsets.UTF_8));
			//return erreur;
		} else{
				URL lienURL = new URL(url);

				HttpURLConnection connexion = (HttpURLConnection)lienURL.openConnection();    
				
				// C'est nécessaire sinon on recevra un 403!
				connexion.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			
				if (connexion.getResponseCode() == HttpURLConnection.HTTP_OK){
					InputStream flux = connexion.getInputStream();
					setStreamLength(connexion.getContentLength());
					listeDeChamps = connexion.getHeaderFields();
					return flux;
						
				} else{
					erreur = connexion.getResponseCode() + " - " + connexion.getResponseMessage() + " - L'url n'est pas active! \n" + connexion.getHeaderFields();
					return new ByteArrayInputStream(erreur.getBytes(StandardCharsets.UTF_8));
				  }
		  } 
	}
	
	public int getStreamLength(){
		return streamLength;
	} 
	
	public void setStreamLength(int streamLength){
		this.streamLength =  streamLength;
	}
	
	public Map<String,String> getListeChainesURL(){
		Map<String,String> tableauDeLiens = null;
		String[] tableauTemporaireDeLiens = null;
		String nomChaine = null;
		String lienChaine = null;
	
		 if (contenu != null && !contenu.equals("") && isLienM3U(contenu)){
			 tableauTemporaireDeLiens = contenu.substring(8,contenu.length()).split("#EXTINF:-1,");
			 
			 for(int i=0;i<tableauTemporaireDeLiens.length;i++){
				 nomChaine = tableauTemporaireDeLiens[i].substring(0, tableauTemporaireDeLiens[i].indexOf("http"));
				 lienChaine = tableauTemporaireDeLiens[i].substring(tableauTemporaireDeLiens[i].indexOf("http"), tableauTemporaireDeLiens[i].length());
				 tableauDeLiens.put(nomChaine, lienChaine);
			 }
			 return tableauDeLiens;
		 }
		 else{
			 return null;
		 }
	}
	
	public boolean isUrlValide(String url){
		UrlValidator urlValidator = new UrlValidator();
		if(urlValidator.isValid(url)){
			return true;
		} else{
			return false;
		}
	}
	
	public boolean isLienM3U(String contenu){
		if (contenu.startsWith("#EXTM3U")){
			return true;
		} else{
			return false;
		}
	}

}
