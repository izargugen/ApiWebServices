package com.sid.ovli.handlers;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.sid.ovli.ConvertisseurCharset;
import com.sid.ovli.GestionnaireFichier;
import com.sid.ovli.Ttaq;

public class M3UFileHandler implements IHandler {
	
	Ttaq ttaq;

	public void perform(Ttaq ttaq) {
		this.ttaq = ttaq;
		
		try {
			ttaq.textPaneResult.setText(null);
			ttaq.textMsgException.setText(null);

			// Cr�ation du fichier Entr�e de fichier personnel M3U
			ttaq.fichierMaListeM3U = GestionnaireFichier
					.creerUnFichierEntree(ttaq.getContenuParDefaut(
							ttaq.textMaListeM3U.getText(),
							"maListeLiensURL.m3u"));

			ttaq.nbLignesFichierEntree = ttaq.compterNbLignesFichier(ttaq.fichierMaListeM3U);
			ttaq.textPaneResult
					.setText("*************    Nombre total de chaînes = "
							+ ttaq.nbLignesFichierEntree
							+ "    *************\r\n\r\n");
			ttaq.lblNbTotalLiens.setText("Nombre total de liens = "
					+ ttaq.nbLignesFichierEntree);

			// gffichierMaListeM3U.closeAll(); // Fermer l'instance
			// précédente avant de l'ouvrir de nouveau.
			ttaq.fichierMaListeM3U.close();
			ttaq.fichierMaListeM3U = GestionnaireFichier
					.creerUnFichierEntree(ttaq.getContenuParDefaut(
							ttaq.textMaListeM3U.getText(),
							"maListeLiensURL.m3u"));

			// Création du fichier sortie des liens actifs
			ttaq.fichierLiensActifsM3U = GestionnaireFichier
					.creerUnFichierSortie(ttaq.getContenuParDefaut(
							ttaq.textSortieLiensActifsM3U.getText(),
							"liensActifs.m3u"));

			ttaq.fichierLiensActifsM3U.write("#EXTM3U\r\n");
			if (!ttaq.textURL.getText().equals(null)
					&& !ttaq.textURL.getText().equals("")) {
				// Garder le lien URL de la liste M3U comme 1ier
				// lien dans le fichier des liens actifs pour
				// SmartIptv
				// avec un lien qui ne fonctionne pas pour que VLC
				// ne boucle pas sur ce 1ier lien
				ttaq.fichierLiensActifsM3U
						.write("#EXTINF:-1,Liens URL - "
								+ ttaq.textURL.getText() + "\r\n");
				ttaq.fichierLiensActifsM3U
						.write("http://livesports-server.tk:8000/live/Oj4oJtDy8q/CEntQEycVx/112.ts \r\n");
			}

			// Création du fichier sortie TVLIST.TXT
			ttaq.fichierTvList = GestionnaireFichier
					.creerUnFichierSortie(ttaq.getContenuParDefaut(
							ttaq.textSortieTvList.getText(),
							"tvlist.m3u"));

			ttaq.fichierTvList.write("title:"
					+ ttaq.getContenuParDefaut(
							ttaq.textTvListTitle.getText(), "test")
					+ "\r\n");

			ThreadTraiterFichier runnableTraiterFichier = new ThreadTraiterFichier();
			Thread threadTraiterFichier = new Thread(
					runnableTraiterFichier);
			threadTraiterFichier.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	class ThreadTraiterFichier implements Runnable {
		public void run() {
			ttaq.lblNbTotalLiens.setText("Nombre total de liens = "
					+ ttaq.nbLignesFichierEntree);
			traiterFichier(ttaq.fichierMaListeM3U, ttaq.nbLignesFichierEntree,
					ttaq.fichierLiensActifsM3U, ttaq.fichierTvList);
		}

		public void traiterFichier(BufferedReader fichierEntree,
				int nbLignesFichierEntree, BufferedWriter fichierLiensActifs,
				BufferedWriter fichierTvlist) {
			String ligne = null;
			String nomChaineM3U = null;
			String nomChaineTvList = null;
			URL uneURL = null;
			int nbChainesFonctionnelles = 0;
			Map<String, List<String>> listeDeChamps = null;

			// Réinitialiser le JTable
			ttaq.dtm.setRowCount(0);

			try {
				ttaq.initCompteurs();

				ttaq.lblNbTotalLiens.setText("Nombre total de liens = "
						+ nbLignesFichierEntree);
				ttaq.exit = false;

				ttaq.textPaneResult.append(
						"[Connection] [Length] [Type] [Resolution] ChannelName"
								+ "\r\n", Color.MAGENTA);
				ttaq.textPaneResult.append(
						"------------ -------- ------ ------------ -----------"
								+ "\r\n\n", Color.BLACK);

				// Lecture du fichier d'entr�e ligne par ligne et ne garder que
				// les liens actifs dans un fichier
				while ((ligne = fichierEntree.readLine()) != null && !ttaq.exit) {
					ligne = ligne.replaceAll("\u0000", "");

					if (ligne.contains("#EXTM3U")) {
						System.out.println(ligne);
						continue;
					} else if (ligne.contains("#EXTINF")) {
						nomChaineM3U = ligne;
						nomChaineTvList = ligne.substring(
								ligne.indexOf(",") + 1, ligne.length());

						System.out.print(nomChaineM3U + " - " + nomChaineTvList
								+ " - \t");

					} else if (ligne.contains("http") || ligne.contains("HTTP")) {
						try {

							ttaq.nbLiensTraites++;
							ttaq.lblNbLiensTraites
									.setText("Nombre de liens traités = "
											+ ttaq.nbLiensTraites);
							ttaq.textMsgException.setText(ligne);

							uneURL = new URL(ligne);
							HttpURLConnection connexion = (HttpURLConnection) uneURL
									.openConnection();
							connexion.setConnectTimeout(3000);
							connexion.setReadTimeout(3000);
							connexion
									.setRequestProperty(
											"User-Agent",
											"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

							// Afficher le header de l'URL pour voir quand est
							// ce que le lien expire
							listeDeChamps = connexion.getHeaderFields();
							System.out.println(listeDeChamps.toString());

							if (connexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
								if (!connexion.getContentType().contains(
										"text/html")) {
									// Récupération de la résolution du stream
									String resolution = null;
									if (connexion.getContentLength() != -1) {
										ConvertisseurCharset unConvertisseur = new ConvertisseurCharset(
												Integer.parseInt(ttaq.textTailleBuffer
														.getText()));
										String contenuConverti = unConvertisseur
												.convertir(connexion
														.getInputStream(),
														"UTF8");

										if (contenuConverti
												.contains("RESOLUTION=")) {
											int indexResolution = contenuConverti
													.indexOf("RESOLUTION=");
											resolution = contenuConverti
													.substring(
															indexResolution + 11,
															contenuConverti
																	.indexOf(
																			"\n",
																			indexResolution + 11));
										}
									}

									ttaq.textPaneResult
											.append("["
													+ connexion
															.getHeaderField("Connection")
													+ "] ["
													+ connexion
															.getContentLength()
													+ "] ["
													+ connexion
															.getContentType()
													+ "] [" + resolution + "] "
													+ nomChaineTvList + "\r\n",
													Color.BLACK);

									// Peupler le Jtable en modifiant son model
									// dtm
									ttaq.dtm.addRow(new Object[] {
											nbChainesFonctionnelles + 1, false,
											nomChaineTvList, ligne });

									fichierLiensActifs.write(nomChaineM3U
											+ "   ["
											+ connexion.getContentLength()
											+ "]" + "\r\n");
									fichierLiensActifs.write(ligne + "\r\n");
									fichierTvlist.write(nomChaineTvList + " ["
											+ connexion.getContentLength()
											+ "]" + "#link:" + ligne + "\r\n");

									nbChainesFonctionnelles++;
									ttaq.lblNbLiensActifs
											.setText("Nombre de liens actifs = "
													+ nbChainesFonctionnelles);
								}
							} else {
								ttaq.textPaneResult.append(nomChaineTvList + "\t"
										+ connexion.getResponseCode() + " - "
										+ connexion.getResponseMessage()
										+ "\r\n", Color.RED);

								// Peupler le Jtable en modifiant son model dtm
								ttaq.dtm.addRow(new Object[] {
										nbChainesFonctionnelles + 1, false,
										nomChaineTvList, ligne });

								if (ttaq.chckbxAvecErreurs.isSelected()) {
									fichierLiensActifs.write(nomChaineM3U
											+ "   ["
											+ connexion.getContentLength()
											+ "]" + "\r\n");
									fichierLiensActifs.write(ligne + "\r\n");
									fichierTvlist.write(nomChaineTvList + " ["
											+ connexion.getContentLength()
											+ "]" + "#link:" + ligne + "\r\n");
								}
							}

							// Pour pouvoir scroller automatiquement au fur et à
							// mesure que les liens s'affichent.
							// textPaneResult.setCaretPosition(textPaneResult.getText().length()
							// - 1);

							connexion.disconnect();
						} catch (Exception e) {
							/*
							 * Exceptions possibles: -
							 * java.net.SocketTimeoutException: Read timed out -
							 * java.lang.RuntimeException:
							 * java.lang.IllegalArgumentException: URI can't be
							 * null. - java.net.MalformedURLException: no
							 * protocol:
							 */
							ttaq.textMsgException.setText(e.getMessage());
							// textPaneResult.append(nomChaineTvList + "\t" +
							// e.toString() + "\r\n");
							// appendToPane(textPaneResult,nomChaineTvList +
							// "\t" + e.toString() + "\r\n",Color.RED);
							ttaq.textPaneResult.append(
									nomChaineTvList + "\t" + e.getMessage()
											+ "\r\n", Color.RED);
							// textPaneResult.setCaretPosition(textPaneResult.getText().length());
							System.out.println(e);
						}
					}
				} // while

				ttaq.textMsgException
						.setText("                                  --- FIN---                        ");
				System.out.println("\r\n\r\n");
				System.out.println("Nombre de chaînes fonctionnelles = "
						+ nbChainesFonctionnelles + " / "
						+ nbLignesFichierEntree);

				// textPaneResult.append("\r\nFin du traitement.\r\n");
				// appendToPane(textPaneResult,"\r\nFin du traitement.\r\n",Color.BLACK);
				ttaq.textPaneResult
						.append("\r\nFin du traitement.\r\n", Color.BLACK);
				// textPaneResult.setCaretPosition(textPaneResult.getText().length()
				// - 1);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fichierEntree != null) {
						fichierEntree.close();
					}
					if (fichierLiensActifs != null) {
						fichierLiensActifs.close();
					}
					if (fichierTvlist != null) {
						fichierTvlist.close();
					}

					ttaq.btnSauvegarder.setEnabled(true);
					ttaq.btnJouer.setEnabled(true);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
