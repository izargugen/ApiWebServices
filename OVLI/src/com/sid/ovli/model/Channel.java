package com.sid.ovli.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Cette Classe permet d'ouvrir une URL correpondant à un channel et de vérifier le statut
 * du stream.
 * 
 * @author Sid.
 *
 */

public class Channel implements IPublicateur{
	private String channelName;
	private String channelURL;
	private boolean channelStatus;
	private int channelStatusCode;
	private String channelStatusDescription;
	private String channelHeader;
	private Map<String,List<String>> channelHeaderList = null;
	private int channelStreamLength;
	
	private List<IAbonne> listeDesAbonnes = new ArrayList<IAbonne>();
	
	/**
	 * Constructeur
	 */
	public Channel(String channelName, String channelURL){
		this.channelName = channelName;
		this.channelURL  = channelURL;
		
		try {
			openURL(channelURL);
			notifyAbonnes();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'ouvrir une URL et de récupérer ses différentes propriétés.
	 * @param channelURL
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void openURL(String channelURL) throws MalformedURLException,IOException{
		String erreur = null;
		
		if (channelURL.equals("") || channelURL == null || !isUrlValide(channelURL)){
			setChannelStatus(false);
			setChannelStatusCode(-1);
			setChannelStatusDescription("Url is invalid.");

		} else{
				URL lienURL = new URL(channelURL);

				HttpURLConnection connexion = (HttpURLConnection)lienURL.openConnection();    
				
				// C'est nécessaire sinon on recevra un 403!
				connexion.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			
				if (connexion.getResponseCode() == HttpURLConnection.HTTP_OK){
					setChannelStatus(true);
				} else{
					setChannelStatus(false);					
				}
				
				setChannelStatusCode(connexion.getResponseCode());
				setChannelStatusDescription(connexion.getResponseMessage());
				setChannelHeader(connexion.getHeaderFields().toString());
				setChannelStreamLength(connexion.getContentLength());
				setChannelHeaderList(connexion.getHeaderFields());
		  } 
	}
	
	/**
	 * Permet de vérifier la validité d'une URL.
	 * @param url
	 * @return
	 */
	public boolean isUrlValide(String url){
		UrlValidator urlValidator = new UrlValidator();
		if(urlValidator.isValid(url)){
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * Permet d'afficher les propriétés d'une URL.
	 */
	
	public void display(){
		System.out.println("Channel Name = " + getChannelName());
		System.out.println("Channel URL = " + getChannelURL());
		System.out.println("Channel Status = " + isChannelStatus());
		System.out.println("Channel Status Code = " + getChannelStatusCode());
		System.out.println("Channel Status Description = " + getChannelStatusDescription());
		System.out.println("Channel Streanm Length = " + getChannelStreamLength());
		System.out.println("Channel Header = " + getChannelHeader());
	}
	
	/**
	 * Permet d'ajouter un abonné à la liste des abonnés afin qu'il soit
	 * imformé de tout changement. Un abonné peut être un composant graphique
	 * qui se mettra à jour automatiquement. Celà permet à cette classe d'être
	 * indépendante de la présentation graphique.
	 * 
	 */
	public void add(IAbonne abonne){
		this.listeDesAbonnes.add(abonne);
	}
	
	/**
	 * Permet de supprimer une abonné de laliste des abonnées.
	 */
	public void remove(IAbonne abonne){
		this.listeDesAbonnes.remove(abonne);
	}
	
	/**
	 * Permet d'informer tous les abonnés de tout changements.
	 */
	private void notifyAbonnes(){
		for(IAbonne abonne:this.listeDesAbonnes){
			abonne.update(this);
		}
	}
	
	/**
	 * Permet de vérifier si le statut de l'URL est actif ou inactif.
	 * @return
	 */
	public boolean isChannelStatus() {
		return channelStatus;
	}

	/**
	 * Permet de retourner le code du statut de l'URL.
	 * @return
	 */
	public int getChannelStatusCode() {
		return channelStatusCode;
	}

	/**
	 * Permet de retourner la desription du statut de l'URL.
	 * @return
	 */
	public String getChannelStatusDescription() {
		return channelStatusDescription;
	}
	
	/**
	 * Permet de retourner le nom du channel.
	 * @return
	 */
	public String getChannelName() {
		return channelName;
	}
	
	/**
	 * Permet de retourner l'URL du channel.
	 * @return
	 */
	public String getChannelURL() {
		return channelURL;
	}
	
	/**
	 * Permet de retourner le header http de l'URL sous forme d'une chaîne de caractères.
	 * @return
	 */
	public String getChannelHeader() {
		return channelHeader;
	}
	
	/**
	 * Permet de retourner le header http de l'URL sous forme d'une liste d'attributs.
	 * @return
	 */
	public Map<String, List<String>> getChannelHeaderList() {
		return channelHeaderList;
	}
	
	/**
	 * Permet de retourner la taille du stream.
	 * @return
	 */
	public int getChannelStreamLength() {
		return channelStreamLength;
	}
	
	
	
	private void setChannelStatus(boolean channelStatus) {
		this.channelStatus = channelStatus;
	}
	
	private void setChannelStatusCode(int channelStatusCode) {
		this.channelStatusCode = channelStatusCode;
	}
	
	private void setChannelStatusDescription(String channelStatusDescription) {
		this.channelStatusDescription = channelStatusDescription;
	}
	
	private void setChannelHeader(String channelHeader) {
		this.channelHeader = channelHeader;
	}

	private void setChannelHeaderList(Map<String, List<String>> channelHeaderList) {
		this.channelHeaderList = channelHeaderList;
	}
	
	private void setChannelStreamLength(int channelStreanLength) {
		this.channelStreamLength = channelStreanLength;
	}

}
