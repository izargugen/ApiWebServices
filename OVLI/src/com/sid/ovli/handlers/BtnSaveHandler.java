package com.sid.ovli.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.sid.ovli.GestionnaireFichier;
import com.sid.ovli.Ttaq;

public class BtnSaveHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {

		this.ttaq = ttaq;
		try {
			String infoMessage = null;
			String titleBar = null;

			if (ttaq.textTvListTitle.getText().equals("")) {
				infoMessage = "Veuillez saisir un titre pour votre liste.";
				titleBar = "Attention!";
				JOptionPane.showMessageDialog(null, infoMessage,
						titleBar, JOptionPane.INFORMATION_MESSAGE);
			} else {

				String nomFichier = ttaq.textSortieLiensActifsM3U.getText()
						.substring(
								0,
								ttaq.textSortieLiensActifsM3U.getText()
										.indexOf("."))
						+ "_" + ttaq.textTvListTitle.getText() + ".m3u";
				BufferedReader fichierLiensActifsM3UREntree = GestionnaireFichier
						.creerUnFichierEntree(ttaq.getContenuParDefaut(
								ttaq.textSortieLiensActifsM3U.getText(),
								"liensActifs.m3u"));
				BufferedWriter fichierLiensActifsM3USortie = GestionnaireFichier
						.creerUnFichierSortie(ttaq.getContenuParDefaut(
								nomFichier, "liensActifs_copie.m3u"));

				String ligne = null;

				while ((ligne = fichierLiensActifsM3UREntree.readLine()) != null) {
					fichierLiensActifsM3USortie.write(ligne + "\r\n");
				}

				fichierLiensActifsM3UREntree.close();
				fichierLiensActifsM3USortie.close();

				nomFichier = ttaq.textSortieTvList.getText().substring(0,
						ttaq.textSortieTvList.getText().indexOf("."))
						+ "_" + ttaq.textTvListTitle.getText() + ".txt";
				BufferedReader fichierTvListEntree = GestionnaireFichier
						.creerUnFichierEntree(ttaq.getContenuParDefaut(
								ttaq.textSortieTvList.getText(),
								"tvlist.m3u"));
				BufferedWriter fichierTvListSortie = GestionnaireFichier
						.creerUnFichierSortie(ttaq.getContenuParDefaut(
								nomFichier, "tvlist_copie.m3u"));
				ligne = null;

				while ((ligne = fichierTvListEntree.readLine()) != null) {
					fichierTvListSortie.write(ligne + "\r\n");
				}

				fichierTvListEntree.close();
				fichierTvListSortie.close();

				infoMessage = "Sauvegarde réussie";
				titleBar = "Succès!";
				JOptionPane.showMessageDialog(null, infoMessage,
						titleBar, JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
