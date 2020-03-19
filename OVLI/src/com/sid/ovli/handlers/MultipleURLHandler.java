package com.sid.ovli.handlers;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.sid.ovli.ConvertisseurCharset;
import com.sid.ovli.GestionnaireFichier;
import com.sid.ovli.SourceURL;
import com.sid.ovli.Ttaq;

public class MultipleURLHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {
		this.ttaq = ttaq;
		
		ttaq.textPaneResult.setText(null);
		// Creation du fichier sortie des liens de toutes les
		// URL
		BufferedWriter fichierMaListeLiensToutesURL = GestionnaireFichier
				.creerUnFichierSortie(ttaq.getContenuParDefaut(
						ttaq.txtListeLiensURLMultiple.getText(),
						"maListeLiensToutesURL.m3u"));

		Thread monThreadTraiterURLMultiple = new ThreadTraiterURLMultiple();
		monThreadTraiterURLMultiple.start();
		
		
	}
	
	class ThreadTraiterURLMultiple extends Thread {
		/**
		 * Permet de traiter des URL Multiple en calculant le nombre de liens de
		 * chaque URL.
		 * 
		 * @param input
		 *            L(InputStream qui sera utilisée pour lire les données
		 * @param inputCharset
		 *            Le charset utilisée pour lire les données.
		 * @param outputCharset
		 *            Le charset utilisée pour encoder les données lu via
		 *            read().
		 * @throws UnsupportedEncodingException
		 */
		public ThreadTraiterURLMultiple() {
		}

		@Override
		public void run() {
			traiterURLMultiple();
		}

		public void traiterURLMultiple() {
			// String contenuURL = null;
			InputStream contenuURL = null;
			SourceURL unSourceURL = null;
			int nbLiensActifs = 0;
			int nbLiensTraites = 0;
			int nbLiens = 0;

			ttaq.initCompteurs();

			// String[] contenuFenetreURLMultiple =
			// textAreaMultipleURL.getText().replaceAll("\n", "").split("http");
			// lblNbTotalLiens.setText("Nombre total de liens = " +
			// (contenuFenetreURLMultiple.length - 1));

			String[] contenuFenetreURLMultiple = ttaq.parseLiensHttp(ttaq.textAreaMultipleURL.getText());
			ttaq.lblNbTotalLiens.setText("Nombre total de liens = "
					+ (contenuFenetreURLMultiple.length));
			ttaq.textAreaMultipleURL.setText("");
			for (int i = 0; i < contenuFenetreURLMultiple.length; i++) {
				if (contenuFenetreURLMultiple[i] != null) {
					ttaq.textAreaMultipleURL.append(contenuFenetreURLMultiple[i] + "\n");
					nbLiens = i;
				} else {
					nbLiens = i;
					break;
				}
			}
			ttaq.lblNbTotalLiens.setText("Nombre total de liens = " + (nbLiens + 1));

			// Création du Model qui permet d'avoir la liste de valeurs à mettre
			// dans la JList
			DefaultListModel listModel = new DefaultListModel();
			/*
			 * for(int i=1; i< contenuFenetreURLMultiple.length;i++){
			 * listModel.addElement("http" + contenuFenetreURLMultiple[i]); }
			 * listLiensMultiple.setModel(listModel);
			 */

			// Un CellRenderer afin d'insérer l'élément en vert dans la liste.
			ttaq.listLiensMultiple.setCellRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					// On appele la méthode parent qui initialise tout bien :
					super.getListCellRendererComponent(list, value, index,
							isSelected, cellHasFocus);

					// Le paramètre 'value' correspond à la valeur de la ligne
					// courante
					// On s'en sert pour déterminer la couleur de fond à
					// appliquer
					// if ( **** ) {
					// setBackground(Color.RED);
					setForeground(new Color(24, 131, 56));
					// }

					return this;
				}

			});

			// Au cas où on veut annuler le traitement
			ttaq.exit = false;

			// for(int i=1; i< contenuFenetreURLMultiple.length;i++){
			for (int i = 0; i < nbLiens; i++) {
				if (!ttaq.exit) {
					try {

						nbLiensTraites++;
						ttaq.lblNbLiensTraites.setText("Nombre de liens traités = "
								+ nbLiensTraites);
						ttaq.textMsgException.setText(contenuFenetreURLMultiple[i]);

						unSourceURL = new SourceURL();
						// contenuURL = getContenuURL("http" +
						// contenuFenetreURLMultiple[i]);
						// contenuURL =
						// getContenuURL(contenuFenetreURLMultiple[i]);
						contenuURL = unSourceURL
								.getContenu(contenuFenetreURLMultiple[i]);

						System.out
								.print(contenuFenetreURLMultiple[i] + " - \t");

						ConvertisseurCharset unConvertisseur = new ConvertisseurCharset(
								Integer.parseInt(ttaq.textTailleBuffer.getText()));
						// String contenuConverti =
						// unConvertisseur.convertir(new
						// ByteArrayInputStream(contenu.getBytes(StandardCharsets.UTF_8)),"UTF8");
						String contenuConverti = unConvertisseur.convertir(
								contenuURL, "UTF8");
						unSourceURL.setContenu(contenuConverti);

						if (contenuConverti != null
								&& contenuConverti.contains("#EXT")) {
							nbLiensActifs++;
							ttaq.lblNbLiensActifs
									.setText("Nombre de liens actifs = "
											+ nbLiensActifs);
							// System.out.println(getNbLiensURL(contenuURL) +
							// " - " + "http" + contenuFenetreURLMultiple[i] +
							// "\r\n");
							System.out.println(unSourceURL.getNbLiens());
							// textPaneResult.append(getNbLiensURL(contenuURL) +
							// " - " + contenuFenetreURLMultiple[i] + "\r\n");
							// appendToPane(textPaneResult,getNbLiensURL(contenuURL)
							// + " - " + contenuFenetreURLMultiple[i] +
							// "\r\n",Color.BLACK);
							ttaq.textPaneResult.append(unSourceURL.getNbLiens()
									+ " - " + contenuFenetreURLMultiple[i]
									+ "\r\n", Color.GREEN);

							listModel.addElement(unSourceURL.getNbLiens()
									+ " - " + contenuFenetreURLMultiple[i]);

							ttaq.listLiensMultiple.setModel(listModel);

							contenuURL = null;
							unSourceURL = null;
						} else {
							System.out.print("Lien vide. \n");
						}

					} catch (IOException e) {
						// textPaneResult.append(contenuFenetreURLMultiple[i] +
						// "\t" + e.getClass() + " - " + e.getMessage() +
						// "\r\n",Color.RED);
						ttaq.textPaneResult.append(e.getMessage() + "\t"
								+ contenuFenetreURLMultiple[i] + "\r\n",
								Color.RED);
						ttaq.textMsgException.setText(e.toString());
						// nbLiensTraites++;
						// lblNbLiensTraites.setText("Nombre de liens traités = "
						// + nbLiensTraites);

						System.out.println(contenuFenetreURLMultiple[i] + "\t"
								+ e.getClass() + " - " + e.getMessage());

						// e.printStackTrace();
					} catch (StringIndexOutOfBoundsException siob) {
						// textPaneResult.append(contenuFenetreURLMultiple[i] +
						// "\t" + siob.getClass() + " - " + siob.getMessage() +
						// "\r\n",Color.RED);
						ttaq.textPaneResult
								.append("[" + unSourceURL.getStreamLength()
										+ "]  " + siob.getMessage() + "\t"
										+ contenuFenetreURLMultiple[i] + "\r\n",
										Color.BLUE);
						ttaq.textMsgException.setText(siob.toString());
						// nbLiensTraites++;
						// lblNbLiensTraites.setText("Nombre de liens traités = "
						// + nbLiensTraites);

						System.out.println(contenuFenetreURLMultiple[i] + "\t"
								+ siob.getClass() + " - " + siob.getMessage());
					}
				} else {
					break;
				}
			} // for

			ttaq.textMsgException
					.setText("                                  --- FIN---                        ");
			System.out
					.println("\r\n                        --- FIN---                        \r\n");

		}
	}

}
