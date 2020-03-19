package com.sid.ovli.handlers;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;

import com.sid.ovli.GestionnaireFichier;
import com.sid.ovli.SourceURL;
import com.sid.ovli.Ttaq;

public class ListeM3UHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {

		this.ttaq = ttaq;
		
		ttaq.textPaneResult.setText("");

		// Création du fichier sortie des liens de l'URL
		ttaq.fichierMaListeLiensURL = GestionnaireFichier
				.creerUnFichierSortie(ttaq.getContenuParDefaut(
						ttaq.textMaListeLiensURL.getText(),
						"maListeLiensURL.m3u"));

		ThreadTraiterListeLiensM3u threadTraiterListeLiensM3u = new ThreadTraiterListeLiensM3u();
		threadTraiterListeLiensM3u.start();
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

}
