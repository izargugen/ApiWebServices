package com.sid.ovli.handlers;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sid.ovli.ConvertisseurCharset;
import com.sid.ovli.GestionnaireFichier;
import com.sid.ovli.SourceURL;
import com.sid.ovli.Ttaq;

public class URLHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {
		this.ttaq = ttaq;
		
		ttaq.textPaneResult.setText(null);
		// Création du fichier sortie des liens de l'URL
		ttaq.fichierMaListeLiensURL = GestionnaireFichier
				.creerUnFichierSortie(ttaq.getContenuParDefaut(
						ttaq.textMaListeLiensURL.getText(),
						"maListeLiensURL.m3u"));

		ThreadTraiterLienURL threadTraiterLienURL = new ThreadTraiterLienURL();
		threadTraiterLienURL.start();
	}
	
	class ThreadTraiterListeLiensM3u extends Thread {
		public ThreadTraiterListeLiensM3u() {
		}

		public void run() {

			try {

				String contenuTextAreaMultipleURL = "";
				for (String line : ttaq.textAreaMultipleURL.getText().split("\\n")) {
					// Ignorer les lignes superflues
					if (line.toUpperCase().contains("#EXTM3U")
							|| line.toUpperCase().contains("#EXTINF")
							|| line.toUpperCase().contains("HTTP")) {
						contenuTextAreaMultipleURL = contenuTextAreaMultipleURL
								+ line + "\n";
					}
				}

				SourceURL unSourceURL = new SourceURL();
				unSourceURL.setContenu(contenuTextAreaMultipleURL);

				ttaq.textPaneResult.append(contenuTextAreaMultipleURL, Color.BLACK);

				ttaq.lblNbTotalLiens.setText("Nombre total de liens = "
						+ unSourceURL.getNbLiens());

				ttaq.fichierMaListeLiensURL.write(contenuTextAreaMultipleURL);
				ttaq.fichierMaListeLiensURL.close();

				ttaq.rdbtnSelectM3UFile.doClick();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ThreadTraiterLienURL extends Thread {
		public Set<Entry<String, List<String>>> lesEntrees = null;

		public ThreadTraiterLienURL() {
		}

		public void run() {
			String[] tableauDeLiens = null; // contiendra les liens HTTP lors du
											// parse de l'URL

			try {
				ttaq.initCompteurs();

				SourceURL unSourceURL = new SourceURL();
				InputStream contenu = unSourceURL.getContenu(ttaq.textURL.getText());

				// Afficher le header de l'URL pour voir quand est ce que le
				// lien expire

				if (unSourceURL.getDescription() != null) {
					lesEntrees = unSourceURL.getDescription().entrySet();
					if (lesEntrees != null) {
						Iterator<Entry<String, List<String>>> it = lesEntrees
								.iterator();
						while (it.hasNext()) {
							Entry<String, List<String>> e = it.next();
							ttaq.textPaneResult.append(
									e.getKey() + " : " + e.getValue() + "\r\n",
									Color.BLACK);
						}
						ttaq.textPaneResult
								.append("---------------------------------------------------------------------\r\n",
										Color.BLACK);
					}
				}

				// textAreaResult.append(unSourceURL.getDescription().get("Expires").toString());

				ConvertisseurCharset unConvertisseur = new ConvertisseurCharset(
						Integer.parseInt(ttaq.textTailleBuffer.getText()));
				// String contenuConverti = unConvertisseur.convertir(new
				// ByteArrayInputStream(contenu.getBytes(StandardCharsets.UTF_8)),"UTF8");
				String contenuConverti = unConvertisseur.convertir(contenu,
						"UTF8");

				if (!contenuConverti.startsWith("<!doctype html>")
						&& !contenuConverti.startsWith("<!DOCTYPE html>")) {
					unSourceURL.setContenu(contenuConverti);

					ttaq.textPaneResult.append(contenuConverti, Color.BLACK);

					ttaq.lblNbTotalLiens.setText("Nombre total de liens = "
							+ unSourceURL.getNbLiens());

					ttaq.fichierMaListeLiensURL.write(contenuConverti);
					ttaq.fichierMaListeLiensURL.close();
				} else { // traiter un lien URL pour parser le contenu afin d'en
							// extraire les liens IPTV

					ttaq.textPaneResult.append(contenuConverti, Color.BLACK);
					tableauDeLiens = parsePageHTML(contenuConverti);

					for (int i = 0; i < tableauDeLiens.length; i++) {
						if (tableauDeLiens[i] == null) {
							break;
						}

						ttaq.textAreaMultipleURL.append(tableauDeLiens[i] + "\n");
					}
					// tableauDeLiens
				}
				ttaq.rdbtnSelectM3UFile.doClick();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public String[] parsePageHTML(String contenuPageHTML) {

			String leLienHttp = null;
			int indiceLien = 0;
			int tailleMaxTableauDeLiens = 300;
			String[] tableauDeLiens = new String[tailleMaxTableauDeLiens];

			// Extraire juste les URL
			Pattern lePattern = Pattern
					.compile("http\\w?://[0-9A-Za-z.;,:_\\@#%?&=/-]*");
			// Pattern lePattern =
			// Pattern.compile("/([^<>\"\'\\s]\\s*)(\\b(https?):\\/\\/[-\\w+&@#\\/%?=~|!:,.;]*[-\\w+&@#\\/%=~|])(\\s*[^<>\"\'\\s])/gi");
			Matcher leMatcher = lePattern.matcher(contenuPageHTML);
			while (leMatcher.find()) {
				// System.out.println(leMatcher.group(0));
				// System.out.println(leMatcher.group(1));
				leLienHttp = leMatcher.group();
				System.out.println(leLienHttp);
				tableauDeLiens[indiceLien] = leLienHttp;

				// On a juste le droit de collecter 300 liens. On peut
				// l'augmenter ci-dessus
				indiceLien++;
				if (indiceLien > tailleMaxTableauDeLiens - 1) {
					break;
				}
			}

			return tableauDeLiens;
		}

	}

}
