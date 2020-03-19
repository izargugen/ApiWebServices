package com.sid.ovli;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.validator.UrlValidator;

import com.sid.ovli.handlers.BtnAnnulerHandler;
import com.sid.ovli.handlers.BtnExecuterScriptHandler;
import com.sid.ovli.handlers.BtnFileChooserFichierEntreeM3UHandler;
import com.sid.ovli.handlers.BtnJouerHandler;
import com.sid.ovli.handlers.BtnRaffraichirHandler;
import com.sid.ovli.handlers.BtnSaveHandler;
import com.sid.ovli.handlers.BtnSearchHandler;
import com.sid.ovli.handlers.BtnVerifierHandler;
import com.sid.ovli.handlers.IHandler;

public class Ttaq {

	CardLayout cardLayout = new CardLayout();

	// Liste des noms de nos conteneurs pour la pile de cartes
	String[] listContent = { "CARD_TRAITEMENT", "CARD_PARAMETRES",
			"CARD_ORGANISER", "CARD_SCRIPTS" };

	public JToolBar toolbar;

	public JFrame frmFramePrincipale;
	public JFrame frmOutilDeVerification;
	public final ButtonGroup buttonGroup = new ButtonGroup();
	public JTextField textMaListeM3U;
	public JTextField textSortieLiensActifsM3U;
	public JTextField textSortieTvList;
	public JTextField textTvListTitle;
	public JTextField textURL;
	public JTextField textMaListeLiensURL;
	public JTextField txtListeLiensURLMultiple;
	public MonJTextPane textPaneResult;
	public JTextArea textAreaMultipleURL;
	public JList listLiensMultiple;
	public JCheckBox chckbxAvecErreurs;

	public JRadioButton rdbtnSelectURL;
	public JRadioButton rdbtnSelectM3UFile;
	public JRadioButton rdbtnMultipleURL;
	public JRadioButton rdbtnListeM3U;
	public JRadioButton rdbtnSelectDirectory;

	public JButton btnSauvegarder;
	public JButton btnJouer;

	public static JTextField textTailleBuffer;
	public static JLabel lblNbTotalLiens;
	public JScrollPane scrollPaneResult;

	public JLabel lblNbLiensActifs;
	public JLabel lblNbLiensTraites;

	public BufferedReader fichierMaListeM3U = null;
	public BufferedReader fichierScript = null;
	public BufferedWriter fichierMaListeLiensURL = null;
	public BufferedWriter fichierLiensActifsM3U = null;
	public BufferedWriter fichierTvList = null;

	public int nbLignesFichier = 0;
	public int nbLiensTraites = 0;
	public int nbLignesFichierEntree = 0;

	public boolean exit = false;

	public JTextField textMsgException;
	public JButton btnFileChooserFichierEntreeM3U;
	public JTextField textDirectory;
	public JTable table;
	public JTextField txtAjouterGroupe;

	// Pour le Tree
	public JTree treeGroupes;
	public DefaultMutableTreeNode noeudRacine;

	// Pour la table
	public DefaultTableModel dtm;

	public JButton btnSupprimerGroup;
	public JTextField txtCheminVLC;
	public JTextField textCheminScriptsParDefaut;
	public JTextField textCheminScripts;

	public String fichierScriptSelectionne;
	public String cheminFichierScriptSelectionne;
	public JTextField textPythonDefaultPath;
	public DefaultListModel listScriptsModel;
	
	public JList jListScripts;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

					Ttaq window = new Ttaq();
					window.frmOutilDeVerification.setVisible(true);
					// window.frmFramePrincipale.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ttaq() {
		initializeFrame();
		initializeMenu();
		initializeJToolBar();
		initializeCardTraitement();
		initializeCardParametres();
		initializeCardOrganiser();
		initializeCardScripts();
	}

	public void initializeFrame() {
		frmFramePrincipale = new JFrame();
		frmFramePrincipale.getContentPane().setLayout(new BorderLayout());

		frmOutilDeVerification = new JFrame();
		frmOutilDeVerification
				.setTitle("OVLI - Outil de v\u00E9rification de liens");
		frmOutilDeVerification.setBounds(100, 80, 1045, 650);
		frmOutilDeVerification.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOutilDeVerification.getContentPane().setLayout(cardLayout);

		// frmFramePrincipale.add(frmOutilDeVerification,BorderLayout.SOUTH);

	}

	public void initializeMenu() {

		// Création du menu

		JMenuBar menuBar;
		JMenu menuTraitement, menuParametres, submenu, menuOrganiser, menuScripts;
		JMenuItem menuItemURL, menuItemFichier, menuItemRepertoire, menuItemLiensMultiples, menuItemListeM3U, menuItemParametres, menuItemOrganiser, menuItemScripts;

		menuBar = new JMenuBar();

		menuTraitement = new JMenu("Ouvrir");
		menuTraitement.setMnemonic(KeyEvent.VK_O);
		menuTraitement.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menuTraitement);

		// a group of JMenuItems
		menuItemURL = new JMenuItem("URL", KeyEvent.VK_U);
		menuItemURL.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				ActionEvent.ALT_MASK));
		menuItemURL.getAccessibleContext().setAccessibleDescription(
				"Permet d'ouvrir un lien URL.");
		menuTraitement.add(menuItemURL);
		menuItemURL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[0]);
				rdbtnSelectURL.doClick();

			}
		});

		menuItemFichier = new JMenuItem("Fichier", KeyEvent.VK_F);
		menuItemFichier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				ActionEvent.ALT_MASK));
		menuItemFichier.getAccessibleContext().setAccessibleDescription(
				"Permet d'ouvrir un fichier.");
		menuTraitement.add(menuItemFichier);
		menuItemFichier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[0]);
				rdbtnSelectM3UFile.doClick();
			}
		});

		menuItemRepertoire = new JMenuItem("Répertoire", KeyEvent.VK_R);
		menuItemRepertoire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.ALT_MASK));
		menuItemRepertoire.getAccessibleContext().setAccessibleDescription(
				"Permet d'ouvrir un répertoire.");
		menuTraitement.add(menuItemRepertoire);
		menuItemRepertoire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[0]);
				rdbtnSelectDirectory.doClick();
			}
		});

		menuItemLiensMultiples = new JMenuItem("Liens Multiples", KeyEvent.VK_M);
		menuItemLiensMultiples.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_M, ActionEvent.ALT_MASK));
		menuItemLiensMultiples.getAccessibleContext().setAccessibleDescription(
				"Permet de traiter plusieurs liens.");
		menuTraitement.add(menuItemLiensMultiples);
		menuItemLiensMultiples.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[0]);
				rdbtnMultipleURL.doClick();
			}
		});

		menuItemListeM3U = new JMenuItem("Liste M3U", KeyEvent.VK_L);
		menuItemListeM3U.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.ALT_MASK));
		menuItemListeM3U.getAccessibleContext().setAccessibleDescription(
				"Permet de traiter une liste M3U.");
		menuTraitement.add(menuItemListeM3U);
		menuItemListeM3U.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[0]);
				rdbtnListeM3U.doClick();
			}
		});

		menuItemParametres = new JMenuItem("Paramètres", KeyEvent.VK_P);
		menuItemParametres.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.ALT_MASK));
		menuItemParametres.getAccessibleContext().setAccessibleDescription(
				"Permet d'afficher les paramètres.");
		menuTraitement.add(menuItemParametres);
		menuItemParametres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[1]);
				textSortieLiensActifsM3U.requestFocus(true);

			}
		});

		menuOrganiser = new JMenu("Organiser");
		menuOrganiser.getAccessibleContext().setAccessibleDescription(
				"Permet d'organiser les favoris.");
		menuBar.add(menuOrganiser);

		menuItemOrganiser = new JMenuItem("Organiser", KeyEvent.VK_G);
		menuItemOrganiser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				ActionEvent.ALT_MASK));
		menuItemOrganiser.getAccessibleContext().setAccessibleDescription(
				"Permet d'organiser les favoris.");
		menuOrganiser.add(menuItemOrganiser);
		menuItemOrganiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[2]);
				// textSortieLiensActifsM3U.requestFocus(true);

			}
		});

		menuScripts = new JMenu("Scripts");
		menuScripts.getAccessibleContext().setAccessibleDescription(
				"Permet d'exécuter des scripts.");
		menuBar.add(menuScripts);

		menuItemScripts = new JMenuItem("Scripts", KeyEvent.VK_S);
		menuItemScripts.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.ALT_MASK));
		menuItemScripts.getAccessibleContext().setAccessibleDescription(
				"Permet d'exécuter des scripts.");
		menuScripts.add(menuItemScripts);
		menuItemScripts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[3]);
			}
		});

		frmOutilDeVerification.setJMenuBar(menuBar);
	}

	public void initializeJToolBar() {
		toolbar = new JToolBar();
		toolbar.setRollover(true);
		toolbar.setBounds(10, 10, 100, 10);

		JButton button = new JButton("button");
		toolbar.add(button);
		toolbar.addSeparator();

		toolbar.add(new JButton("button 3"));
		toolbar.add(button);

	}

	public void initializeCardTraitement() {

		JPanel cardTraitement = new JPanel();
		cardTraitement.setLayout(null);
		// cardTraitement.add(toolbar,BorderLayout.NORTH);

		rdbtnSelectM3UFile = new JRadioButton("");
		rdbtnSelectM3UFile.setFont(new Font("Courier New", Font.PLAIN, 12));
		buttonGroup.add(rdbtnSelectM3UFile);
		rdbtnSelectM3UFile.setBounds(17, 107, 18, 23);
		cardTraitement.add(rdbtnSelectM3UFile);

		rdbtnSelectURL = new JRadioButton("");
		rdbtnSelectURL.setSelected(true);
		rdbtnSelectURL.setFont(new Font("Courier New", Font.PLAIN, 12));
		buttonGroup.add(rdbtnSelectURL);
		rdbtnSelectURL.setBounds(17, 58, 18, 23);
		rdbtnSelectURL.setMnemonic(KeyEvent.VK_U);
		cardTraitement.add(rdbtnSelectURL);

		rdbtnMultipleURL = new JRadioButton("");
		rdbtnMultipleURL.setFont(new Font("Courier New", Font.PLAIN, 12));
		buttonGroup.add(rdbtnMultipleURL);
		rdbtnMultipleURL.setBounds(18, 190, 18, 23);
		cardTraitement.add(rdbtnMultipleURL);

		rdbtnSelectDirectory = new JRadioButton("");
		buttonGroup.add(rdbtnSelectDirectory);
		rdbtnSelectDirectory.setFont(new Font("Courier New", Font.PLAIN, 12));
		rdbtnSelectDirectory.setBounds(17, 154, 18, 23);
		cardTraitement.add(rdbtnSelectDirectory);

		textMaListeM3U = new JTextField();
		textMaListeM3U.setEnabled(false);
		textMaListeM3U.setFont(new Font("Courier New", Font.PLAIN, 12));
		textMaListeM3U.setBounds(207, 110, 180, 20);
		cardTraitement.add(textMaListeM3U);
		textMaListeM3U.setColumns(10);

		JLabel lblInputM3U = new JLabel(
				"<HTML><U>F</U>ichier d'entr\u00E9e (M3U)</HTML>");
		lblInputM3U.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblInputM3U.setBounds(40, 113, 167, 14);
		cardTraitement.add(lblInputM3U);

		JLabel lblTitrePourTvliste = new JLabel("Titre pour Tvliste");
		lblTitrePourTvliste.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblTitrePourTvliste.setBounds(17, 25, 138, 14);
		cardTraitement.add(lblTitrePourTvliste);

		textTvListTitle = new JTextField();
		textTvListTitle.setFont(new Font("Courier New", Font.PLAIN, 12));
		textTvListTitle.setBounds(173, 22, 87, 20);
		cardTraitement.add(textTvListTitle);
		textTvListTitle.setColumns(10);

		JLabel lblUrl = new JLabel("<HTML><U>U</U>RL</HTML>");
		lblUrl.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblUrl.setBounds(40, 64, 27, 14);
		cardTraitement.add(lblUrl);

		textURL = new JTextField();
		textURL.setBounds(78, 61, 342, 20);
		cardTraitement.add(textURL);
		textURL.setColumns(10);

		JLabel lblRepository = new JLabel("<HTML><U>R</U>épertoire</HTML>");
		lblRepository.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblRepository.setBounds(44, 160, 83, 14);
		cardTraitement.add(lblRepository);

		textDirectory = new JTextField();
		textDirectory.setFont(new Font("Courier New", Font.PLAIN, 12));
		textDirectory.setColumns(10);
		textDirectory.setBounds(124, 158, 263, 20);
		cardTraitement.add(textDirectory);

		textPaneResult = new MonJTextPane();
		textPaneResult.setFont(new Font("Courier New", Font.PLAIN, 13));
		textPaneResult.setBackground(new Color(255, 255, 170));

		// Afin d'afficher une ligne de texte dépassant la largeur du textPane
		// en affichant le scroll horizontal
		JPanel noWrapPanel = new JPanel(new BorderLayout());
		noWrapPanel.add(textPaneResult);
		// JScrollPane scrollPane = new JScrollPane( noWrapPanel );

		scrollPaneResult = new JScrollPane(noWrapPanel);
		scrollPaneResult.setBounds(430, 20, 573, 460);

		// Cette instruction cause un problème de wrapping, ça retourne
		// automatiquement à la ligne au lieu de scroller
		// scrollPaneResult.setViewportView(textPaneResult);

		// frmOutilDeVerification.getContentPane().add(scrollPaneResult);
		cardTraitement.add(scrollPaneResult);

		JButton btnVerifier = new JButton("V\u00E9rifier liens");
		btnVerifier.setBackground(new Color(143, 188, 143));
		btnVerifier.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnVerifier.setBounds(430, 531, 134, 30);
		btnVerifier.setMnemonic(KeyEvent.VK_V);
		// frmOutilDeVerification.getContentPane().add(btnVerifier);
		cardTraitement.add(btnVerifier);

		JScrollPane scrollPaneMultipleURL = new JScrollPane();
		scrollPaneMultipleURL.setBounds(28, 224, 388, 168);
		// frmOutilDeVerification.getContentPane().add(scrollPaneMultipleURL);
		cardTraitement.add(scrollPaneMultipleURL);

		textAreaMultipleURL = new JTextArea();
		textAreaMultipleURL.setLocation(34, 0);
		textAreaMultipleURL.setFont(new Font("Courier New", Font.PLAIN, 12));
		textAreaMultipleURL.setEnabled(false);
		textAreaMultipleURL.setBackground(new Color(255, 255, 170));
		scrollPaneMultipleURL.setViewportView(textAreaMultipleURL);

		JLabel lblRsultats = new JLabel("R\u00E9sultats");
		lblRsultats.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblRsultats.setBounds(430, 7, 87, 14);
		// frmOutilDeVerification.getContentPane().add(lblRsultats);
		cardTraitement.add(lblRsultats);

		JLabel lblMaListeLiensURL = new JLabel("Fichier liens URL");
		lblMaListeLiensURL.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblMaListeLiensURL.setBounds(40, 86, 130, 14);
		// frmOutilDeVerification.getContentPane().add(lblMaListeLiensURL);
		cardTraitement.add(lblMaListeLiensURL);

		textMaListeLiensURL = new JTextField();
		textMaListeLiensURL.setFont(new Font("Courier New", Font.PLAIN, 12));
		textMaListeLiensURL.setBounds(173, 82, 220, 20);
		textMaListeLiensURL.setText("maListeLiensURL.m3u");
		// frmOutilDeVerification.getContentPane().add(textMaListeLiensURL);
		cardTraitement.add(textMaListeLiensURL);
		textMaListeLiensURL.setColumns(10);

		JLabel lblURLMultiple = new JLabel(
				"<HTML>Fichier URL <U>M</U>ultiples</HTML>");
		lblURLMultiple.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblURLMultiple.setBounds(41, 197, 153, 14);
		// frmOutilDeVerification.getContentPane().add(lblURLMultiple);
		cardTraitement.add(lblURLMultiple);

		txtListeLiensURLMultiple = new JTextField();
		txtListeLiensURLMultiple.setEnabled(false);
		txtListeLiensURLMultiple
				.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtListeLiensURLMultiple.setBounds(206, 193, 113, 20);
		// frmOutilDeVerification.getContentPane().add(txtListeLiensURLMultiple);
		cardTraitement.add(txtListeLiensURLMultiple);
		txtListeLiensURLMultiple.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(17, 55, 400, 2);
		// frmOutilDeVerification.getContentPane().add(separator);
		cardTraitement.add(separator);

		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnAnnuler.setBounds(569, 531, 83, 31);
		btnAnnuler.setMnemonic(KeyEvent.VK_A);
		// frmOutilDeVerification.getContentPane().add(btnAnnuler);
		cardTraitement.add(btnAnnuler);

		lblNbTotalLiens = new JLabel("Nombre total de liens = 0");
		lblNbTotalLiens.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblNbTotalLiens.setBounds(806, 521, 215, 14);
		// frmOutilDeVerification.getContentPane().add(lblNbTotalLiens);
		cardTraitement.add(lblNbTotalLiens);

		lblNbLiensTraites = new JLabel("Nombre de liens traités = 0");
		lblNbLiensTraites.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblNbLiensTraites.setBounds(806, 538, 216, 14);
		// frmOutilDeVerification.getContentPane().add(lblNbLiensTraites);
		cardTraitement.add(lblNbLiensTraites);

		lblNbLiensActifs = new JLabel("Nombre de liens actifs = 0");
		lblNbLiensActifs.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblNbLiensActifs.setBounds(806, 556, 216, 14);
		// frmOutilDeVerification.getContentPane().add(lblNbLiensActifs);
		cardTraitement.add(lblNbLiensActifs);

		JScrollPane scrollPaneListLiensMultiple = new JScrollPane();
		scrollPaneListLiensMultiple.setBounds(31, 404, 382, 166);
		// frmOutilDeVerification.getContentPane().add(scrollPaneListLiensMultiple);
		cardTraitement.add(scrollPaneListLiensMultiple);

		listLiensMultiple = new JList();
		listLiensMultiple.setFont(new Font("Courier New", Font.PLAIN, 12));
		listLiensMultiple.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {

				// Copier la valeur de la ligne sélectionner dans le textURL
				// afin de traiter ce lien.
				if (listLiensMultiple.getSelectedValue() != null) {
					String lien = listLiensMultiple.getSelectedValue()
							.toString();

					if (!lien.toLowerCase().contains("http")
							&& lien.toLowerCase().endsWith(".m3u")) {
						//Est considéré comme un répertoire
						rdbtnSelectM3UFile.doClick();
						textMaListeM3U.setText(textDirectory.getText() + "\\"
								+ lien);
					} else {
						// Est considéré comme un lien
						textURL.enable();
						rdbtnSelectURL.doClick();
						textURL.setText(lien.substring(lien.indexOf("http"),
								lien.length()));
					}
				}
				
				listLiensMultiple.grabFocus();
			}
		});
		
		listLiensMultiple.addKeyListener(new KeyAdapter(){
			  public void keyPressed(KeyEvent ke){
				  
				  if(ke.getKeyCode() == KeyEvent.VK_DOWN){
					  listLiensMultiple.setSelectedIndex(listLiensMultiple.getSelectedIndex());
				  } else if(ke.getKeyCode() == KeyEvent.VK_UP){
					  listLiensMultiple.setSelectedIndex(listLiensMultiple.getSelectedIndex());
				  }
				  
					// Copier la valeur de la ligne sélectionner dans le textURL
					// afin de traiter ce lien.
					if (listLiensMultiple.getSelectedValue() != null) {
						String lien = listLiensMultiple.getSelectedValue()
								.toString();

						if (!lien.toLowerCase().contains("http")
								&& lien.toLowerCase().endsWith(".m3u")) {
							//Est considéré comme un répertoire
							rdbtnSelectM3UFile.doClick();
							textMaListeM3U.setText(textDirectory.getText() + "\\"
									+ lien);
						} else {
							// Est considéré comme un lien
							textURL.enable();
							rdbtnSelectURL.doClick();
							textURL.setText(lien.substring(lien.indexOf("http"),
									lien.length()));
						}
					}					
			  }
		});

		scrollPaneListLiensMultiple.setViewportView(listLiensMultiple);

		chckbxAvecErreurs = new JCheckBox("Avec erreurs");
		chckbxAvecErreurs.setFont(new Font("Courier New", Font.PLAIN, 12));
		chckbxAvecErreurs.setBounds(207, 130, 122, 18);
		// frmOutilDeVerification.getContentPane().add(chckbxAvecErreurs);
		cardTraitement.add(chckbxAvecErreurs);

		textMsgException = new JTextField();
		textMsgException.setFont(new Font("Courier New", Font.PLAIN, 12));
		textMsgException.setBounds(430, 490, 540, 28);
		// frmOutilDeVerification.getContentPane().add(textMsgException);
		cardTraitement.add(textMsgException);
		textMsgException.setColumns(10);

		rdbtnListeM3U = new JRadioButton("Liste M3U");
		buttonGroup.add(rdbtnListeM3U);
		rdbtnListeM3U.setMnemonic(KeyEvent.VK_L);

		rdbtnListeM3U.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				textAreaMultipleURL.enable(true);
				txtListeLiensURLMultiple.enable(true);
				txtListeLiensURLMultiple.setText("listeLiensURLMultiple.m3u");

				textMaListeM3U.enable(false);
				textMaListeM3U.setText(null);
				textURL.enable(false);
				textMaListeLiensURL.setText(null);
				textMaListeLiensURL.enable(false);

				textAreaMultipleURL.requestFocus();
			}
		});
		rdbtnListeM3U.setFont(new Font("Courier New", Font.PLAIN, 12));
		rdbtnListeM3U.setBounds(322, 195, 98, 18);
		// frmOutilDeVerification.getContentPane().add(rdbtnListeM3U);
		cardTraitement.add(rdbtnListeM3U);

		btnSauvegarder = new JButton("Save");
		btnSauvegarder.setMnemonic(KeyEvent.VK_S);
		btnSauvegarder.setEnabled(false);
		btnSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				BtnSaveHandler btnSaveHandler = new BtnSaveHandler();
				traiterCode(btnSaveHandler);
			}
		});

		btnSauvegarder.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnSauvegarder.setBounds(658, 531, 62, 31);
		// frmOutilDeVerification.getContentPane().add(btnSauvegarder);
		cardTraitement.add(btnSauvegarder);

		btnJouer = new JButton("Jouer");
		btnJouer.setMnemonic(KeyEvent.VK_J);
		btnJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				BtnJouerHandler btnJouerHandler = new BtnJouerHandler();
				traiterCode(btnJouerHandler); 
			}
		});

		btnJouer.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnJouer.setEnabled(false);
		btnJouer.setBounds(725, 531, 69, 31);
		// frmOutilDeVerification.getContentPane().add(btnJouer);
		cardTraitement.add(btnJouer);

		btnFileChooserFichierEntreeM3U = new JButton("...");
		btnFileChooserFichierEntreeM3U.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();

				// chooser.setCurrentDirectory(new
				// File(System.getProperty("user.home") +
				// System.getProperty("file.separator")+ "Music"));
				chooser.setCurrentDirectory(new File(System
						.getProperty("user.dir")));

				FileFilter filter = new FileNameExtensionFilter("Listes m3u",
						"m3u");
				chooser.addChoosableFileFilter(filter);
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					textMaListeM3U.setText(chooser.getSelectedFile()
							.getAbsolutePath());
				}

			}
		});
		btnFileChooserFichierEntreeM3U.setFont(new Font("Courier New",
				Font.PLAIN, 12));
		btnFileChooserFichierEntreeM3U.setBounds(395, 110, 25, 20);
		// frmOutilDeVerification.getContentPane().add(button);
		cardTraitement.add(btnFileChooserFichierEntreeM3U);

		// frmOutilDeVerification.getContentPane().add(cardTraitement,
		// listContent[0]);

		JButton btnFileChooserFichiersLiensURL = new JButton("...");
		btnFileChooserFichiersLiensURL.setFont(new Font("Courier New",
				Font.PLAIN, 12));
		btnFileChooserFichiersLiensURL.setBounds(395, 82, 25, 20);
		cardTraitement.add(btnFileChooserFichiersLiensURL);

		JButton btnSearch = new JButton("...");
		btnSearch.setSelectedIcon(new ImageIcon(
				"E:\\OutilVerificationHTTP\\ressources\\loupe.png"));
		btnSearch.setIcon(new ImageIcon(
				"E:\\OutilVerificationHTTP\\ressources\\loupe.png"));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BtnSearchHandler btnSearchHandler = new BtnSearchHandler();
				traiterCode(btnSearchHandler);
			}
		});
		btnSearch.setBounds(973, 490, 30, 26);
		cardTraitement.add(btnSearch);

		JButton btnFileChooserFichierDirectory = new JButton("...");
		btnFileChooserFichierDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				BtnFileChooserFichierEntreeM3UHandler btnFileChooserFichierEntreeM3UHandler = new BtnFileChooserFichierEntreeM3UHandler();
				traiterCode(btnFileChooserFichierEntreeM3UHandler);
			}
		});
		btnFileChooserFichierDirectory.setFont(new Font("Courier New",
				Font.PLAIN, 12));
		btnFileChooserFichierDirectory.setBounds(395, 156, 25, 20);
		cardTraitement.add(btnFileChooserFichierDirectory);

		rdbtnSelectURL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textURL.enable(true);
				textMaListeLiensURL.enable(true);
				textMaListeLiensURL.setText("maListeLiensURL.m3u");
				textURL.requestFocus();
				textURL.selectAll();

				textMaListeM3U.setText(null);
				textMaListeM3U.enable(false);
				txtListeLiensURLMultiple.setText(null);
				txtListeLiensURLMultiple.enable(false);
				textAreaMultipleURL.enable(false);
			}
		});

		rdbtnSelectM3UFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textMaListeM3U.setText("maListeLiensURL.m3u");
				textMaListeM3U.enable(true);
				btnFileChooserFichierEntreeM3U.requestFocus();

				textURL.enable(false);
				textMaListeLiensURL.setText(null);
				textMaListeLiensURL.enable(false);
				txtListeLiensURLMultiple.setText(null);
				txtListeLiensURLMultiple.enable(false);
				textAreaMultipleURL.enable(false);
			}
		});

		rdbtnMultipleURL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textAreaMultipleURL.enable(true);
				textAreaMultipleURL.requestFocus();
				txtListeLiensURLMultiple.enable(true);
				txtListeLiensURLMultiple.setText("listeLiensURLMultiple.m3u");

				textMaListeM3U.enable(false);
				textMaListeM3U.setText(null);
				textURL.enable(false);
				textMaListeLiensURL.setText(null);
				textMaListeLiensURL.enable(false);
			}
		});

		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				BtnAnnulerHandler btnAnnulerHandler = new BtnAnnulerHandler();
				traiterCode(btnAnnulerHandler);
			}
		});

		btnVerifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BtnVerifierHandler btnVerifierHandler = new BtnVerifierHandler();
				traiterCode(btnVerifierHandler);
			}
		});

		frmOutilDeVerification.getContentPane().add(cardTraitement,
				listContent[0]);
	}

	public void initializeCardParametres() {
		/*
		 * JPanel cardParametres = new JPanel(); card1.setLayout(new
		 * BorderLayout()); card1.setBackground(Color.blue); JButton btnCard1 =
		 * new JButton("Card 1"); card1.add(btnCard1,BorderLayout.NORTH);
		 */

		JPanel cardParametres = new JPanel();
		cardParametres.setLayout(null);
		frmOutilDeVerification.getContentPane().add(cardParametres,
				listContent[1]);

		JLabel lblActiveLinksOutput1 = new JLabel("Fichier de sortie");
		lblActiveLinksOutput1.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblActiveLinksOutput1.setBounds(17, 35, 138, 14);
		cardParametres.add(lblActiveLinksOutput1);

		JLabel lblActiveLinksOutput2 = new JLabel("pour les liens actifs");
		lblActiveLinksOutput2.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblActiveLinksOutput2.setBounds(17, 45, 152, 14);
		cardParametres.add(lblActiveLinksOutput2);

		textSortieLiensActifsM3U = new JTextField();
		textSortieLiensActifsM3U
				.setFont(new Font("Courier New", Font.PLAIN, 12));
		textSortieLiensActifsM3U.setText("liensActifs.m3u");
		textSortieLiensActifsM3U.setBounds(194, 35, 247, 20);
		cardParametres.add(textSortieLiensActifsM3U);
		textSortieLiensActifsM3U.setColumns(10);

		JLabel lblTvListOutput = new JLabel("Fichier sortie Tvlist");
		lblTvListOutput.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblTvListOutput.setBounds(17, 67, 152, 14);
		cardParametres.add(lblTvListOutput);

		textSortieTvList = new JTextField();
		textSortieTvList.setFont(new Font("Courier New", Font.PLAIN, 12));
		textSortieTvList.setText("tvlist.txt");
		textSortieTvList.setBounds(194, 63, 247, 20);
		cardParametres.add(textSortieTvList);
		textSortieTvList.setColumns(10);

		JLabel lblTailleBuffer = new JLabel("Taille buffer");
		lblTailleBuffer.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblTailleBuffer.setBounds(19, 114, 97, 14);
		cardParametres.add(lblTailleBuffer);

		textTailleBuffer = new JTextField();
		textTailleBuffer.setText("8192");
		textTailleBuffer.setFont(new Font("Courier New", Font.PLAIN, 12));
		textTailleBuffer.setBounds(195, 110, 60, 20);
		cardParametres.add(textTailleBuffer);
		textTailleBuffer.setColumns(10);

		JButton btnRetour = new JButton("Retour");
		btnRetour.setMnemonic(KeyEvent.VK_R);
		btnRetour.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[0]);
			}
		});
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(frmOutilDeVerification.getContentPane(),
						listContent[0]);
			}
		});
		btnRetour.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnRetour.setBounds(193, 192, 90, 28);
		cardParametres.add(btnRetour);

		JLabel lblCheminVlv = new JLabel("Chemin VLC");
		lblCheminVlv.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblCheminVlv.setBounds(17, 88, 152, 16);
		cardParametres.add(lblCheminVlv);

		txtCheminVLC = new JTextField();
		txtCheminVLC.setText("C:\\Mes_apps\\VideoLan\\VLC");
		txtCheminVLC.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtCheminVLC.setBounds(194, 88, 247, 20);
		cardParametres.add(txtCheminVLC);
		txtCheminVLC.setColumns(10);

		JLabel lblScriptsDirectory = new JLabel("Scripts directory");
		lblScriptsDirectory.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblScriptsDirectory.setBounds(17, 168, 140, 16);
		cardParametres.add(lblScriptsDirectory);

		textCheminScriptsParDefaut = new JTextField();
		textCheminScriptsParDefaut
				.setText("C:\\Users\\SQ63215\\Desktop\\Nouveau dossier\\python\\scripts");
		textCheminScriptsParDefaut.setFont(new Font("Courier New", Font.PLAIN,
				12));
		textCheminScriptsParDefaut.setBounds(193, 164, 247, 20);
		cardParametres.add(textCheminScriptsParDefaut);
		textCheminScriptsParDefaut.setColumns(10);
		
		JLabel lblPythonDefaultPath = new JLabel("Python Default Directory");
		lblPythonDefaultPath.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblPythonDefaultPath.setBounds(17, 140, 187, 16);
		cardParametres.add(lblPythonDefaultPath);
		
		textPythonDefaultPath = new JTextField();
		textPythonDefaultPath.setFont(new Font("Courier New", Font.PLAIN, 12));
		textPythonDefaultPath.setText("C:\\Mes_apps\\Python38\\python.exe");
		textPythonDefaultPath.setBounds(194, 136, 247, 20);
		cardParametres.add(textPythonDefaultPath);
		textPythonDefaultPath.setColumns(10);

	}

	public void initializeCardOrganiser() {
		JPanel cardOrganiser = new JPanel();
		cardOrganiser.setLayout(null);
		frmOutilDeVerification.getContentPane().add(cardOrganiser,
				listContent[2]);

		JLabel lblTitreOrganiser = new JLabel(
				"Organiser les chaînes en groupes");
		lblTitreOrganiser.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblTitreOrganiser.setBounds(6, 6, 231, 16);
		cardOrganiser.add(lblTitreOrganiser);

		// Pour le JTree
		JScrollPane scrollPaneTree = new JScrollPane();
		scrollPaneTree.setBounds(632, 68, 373, 468);
		cardOrganiser.add(scrollPaneTree);

		noeudRacine = new DefaultMutableTreeNode("Groupes");
		treeGroupes = new JTree(noeudRacine);
		treeGroupes.setEditable(true);
		scrollPaneTree.setViewportView(treeGroupes);

		// Pourla table
		JScrollPane scrollPaneTable = new JScrollPane();
		scrollPaneTable.setBounds(10, 66, 519, 470);
		cardOrganiser.add(scrollPaneTable);

		table = new JTable();
		table.setFont(new Font("Courier New", Font.PLAIN, 12));

		// Permet de ne pas changer la taille des colonne et permettre plutôt le
		// scroll horizontal
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Créer un model pour le JTable. Le model doit être public pour y
		// accéder d'une autre méthode
		dtm = new DefaultTableModel(0, 0) {
			Class[] columnTypes = new Class[] { Integer.class, Boolean.class,
					String.class, Object.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};

		// Créer les entêtes de colonnes
		String header[] = new String[] { "Num", "Select", "Nom", "URL" };

		// Ajout des entêtes au model
		dtm.setColumnIdentifiers(header);

		// Associer le model à la table
		table.setModel(dtm);

		// Il suffit maintenant d'ajouter les lignes au model pour que la table
		// se remplisse

		// Définir la largeure de chaque colonne
		table.getColumnModel().getColumn(0).setPreferredWidth(35);
		table.getColumnModel().getColumn(1).setPreferredWidth(55);
		table.getColumnModel().getColumn(2).setPreferredWidth(249);
		table.getColumnModel().getColumn(3).setPreferredWidth(1000);
		scrollPaneTable.setViewportView(table);
		scrollPaneTable
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JLabel lblAjouterGroupe = new JLabel("Ajouter un groupe");
		lblAjouterGroupe.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblAjouterGroupe.setBounds(541, 16, 128, 16);
		cardOrganiser.add(lblAjouterGroupe);

		txtAjouterGroupe = new JTextField();
		txtAjouterGroupe.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtAjouterGroupe.setBounds(671, 8, 122, 28);
		cardOrganiser.add(txtAjouterGroupe);
		txtAjouterGroupe.setColumns(10);

		JButton btnAjouterChaine = new JButton("");
		btnAjouterChaine
				.setIcon(new ImageIcon(
						"E:\\OVLI\\git\\LocalOVLIGitRepository\\OutilVerificationHTTP\\ressources\\forward_nav.gif"));
		btnAjouterChaine.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnAjouterChaine.setBounds(556, 228, 47, 28);
		cardOrganiser.add(btnAjouterChaine);
		btnAjouterChaine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DefaultMutableTreeNode noeudCourant = null;

				// Récupérer lemodel du JTree
				DefaultTreeModel model = (DefaultTreeModel) treeGroupes
						.getModel();
				DefaultMutableTreeNode leGroupRacine = (DefaultMutableTreeNode) model
						.getRoot();

				// Récupérer le noeud sélectionné par l'usager
				TreePath path = treeGroupes.getSelectionPath();

				if (path != null) {
					Object noeudSelectionne = path.getLastPathComponent();

					// On ne fait rien avec le noeud racine
					if (noeudSelectionne != treeGroupes.getModel().getRoot()) {
						if (noeudSelectionne instanceof DefaultMutableTreeNode) {
							noeudCourant = (DefaultMutableTreeNode) noeudSelectionne;

							// donnees est un vecteur de vecteur. Il contient
							// toutes les lignes de la table.
							Vector donnees = dtm.getDataVector();

							Boolean select = false;
							String chaine = "";

							// Ajouter au groupe toutes les lignes cochées de la
							// table
							for (int i = 0; i < donnees.size(); i++) {
								select = (Boolean) ((Vector) donnees
										.elementAt(i)).elementAt(1);
								if (select) {
									chaine = (String) ((Vector) donnees
											.elementAt(i)).elementAt(2);
									noeudCourant
											.add(new DefaultMutableTreeNode(
													chaine));

									// On supprime la ligne de la table pour
									// qu'elle ne soit pas ajoutée une nouvelle
									// fois à un groupe.
									dtm.removeRow(i);
									i--;
								}
							}

							model.reload(leGroupRacine);

							// Ouvrir tous les noeuds.
							for (int i = 0; i < treeGroupes.getRowCount(); i++) {
								treeGroupes.expandRow(i);
							}

							treeGroupes.setSelectionPath(path);
						}
					}
				}
			}
		});

		JButton btnEnleverChaine = new JButton("");
		btnEnleverChaine
				.setIcon(new ImageIcon(
						"E:\\OVLI\\git\\LocalOVLIGitRepository\\OutilVerificationHTTP\\ressources\\backward_nav.gif"));
		btnEnleverChaine.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnEnleverChaine.setBounds(556, 259, 47, 28);
		cardOrganiser.add(btnEnleverChaine);

		JButton btnSauvegarderGroupes = new JButton("");
		btnSauvegarderGroupes
				.setIcon(new ImageIcon(
						"E:\\OVLI\\git\\LocalOVLIGitRepository\\OutilVerificationHTTP\\ressources\\save_edit.gif"));
		btnSauvegarderGroupes.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnSauvegarderGroupes.setBounds(631, 548, 55, 28);
		cardOrganiser.add(btnSauvegarderGroupes);

		JButton btnAjouterTout = new JButton("Tout");
		btnAjouterTout
				.setIcon(new ImageIcon(
						"E:\\OVLI\\git\\LocalOVLIGitRepository\\OutilVerificationHTTP\\ressources\\forward_nav.gif"));
		btnAjouterTout.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnAjouterTout.setBounds(535, 326, 89, 28);
		cardOrganiser.add(btnAjouterTout);

		JButton btnEnleverTout = new JButton("Tout");
		btnEnleverTout
				.setIcon(new ImageIcon(
						"E:\\OVLI\\git\\LocalOVLIGitRepository\\OutilVerificationHTTP\\ressources\\backward_nav.gif"));
		btnEnleverTout.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnEnleverTout.setBounds(535, 355, 89, 28);
		cardOrganiser.add(btnEnleverTout);

		JButton btnAjouterGroup = new JButton("");
		btnAjouterGroup
				.setIcon(new ImageIcon(
						"E:\\OVLI\\git\\LocalOVLIGitRepository\\OutilVerificationHTTP\\ressources\\iu_add.gif"));
		btnAjouterGroup.setBounds(799, 8, 47, 28);
		btnAjouterGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Permet d'ajouter de nouveaux groupes à l'arbre
				// Il suffit juste de modifier le model associé et de le
				// recharger
				DefaultTreeModel model = (DefaultTreeModel) treeGroupes
						.getModel();
				DefaultMutableTreeNode leGroupRacine = (DefaultMutableTreeNode) model
						.getRoot();
				if (!txtAjouterGroupe.getText().equals("")) {
					leGroupRacine.add(new DefaultMutableTreeNode(
							txtAjouterGroupe.getText()));
				}
				model.reload(leGroupRacine);
				txtAjouterGroupe.setText("");
				txtAjouterGroupe.requestFocus(true);
			}
		});
		cardOrganiser.add(btnAjouterGroup);

		btnSupprimerGroup = new JButton("");
		btnSupprimerGroup
				.setIcon(new ImageIcon(
						"E:\\OVLI\\git\\LocalOVLIGitRepository\\OutilVerificationHTTP\\ressources\\delete_obj.gif"));
		btnSupprimerGroup.setBounds(851, 8, 47, 28);
		btnSupprimerGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Permet de supprimer un groupe de l'arbre
				// Il suffit juste de modifier le model associé et de le
				// recharger
				DefaultTreeModel model = (DefaultTreeModel) treeGroupes
						.getModel();
				DefaultMutableTreeNode leGroupRacine = (DefaultMutableTreeNode) model
						.getRoot();

				// Récupérer le noeud sélectionné
				TreePath path = treeGroupes.getSelectionPath();
				if (path != null) {
					Object node = path.getLastPathComponent();

					// On ne doit pas supprimer le noeud racine
					if (node != treeGroupes.getModel().getRoot()) {
						model.removeNodeFromParent((DefaultMutableTreeNode) node);
						model.reload(leGroupRacine);
					}
				}
			}
		});
		cardOrganiser.add(btnSupprimerGroup);
		btnEnleverTout.setFont(new Font("Courier New", Font.PLAIN, 12));

	}

	public void initializeCardScripts() {

		JPanel cardScripts = new JPanel();
		cardScripts.setLayout(null);
		frmOutilDeVerification.getContentPane()
				.add(cardScripts, listContent[3]);

		JLabel lblListeScripts = new JLabel("Liste des scripts");
		lblListeScripts.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblListeScripts.setBounds(18, 49, 132, 16);
		cardScripts.add(lblListeScripts);

		final JTextArea textAreaSourceScript = new JTextArea();
		textAreaSourceScript.setBounds(270, 79, 740, 417);
		textAreaSourceScript.setFont(new Font("Courier New", Font.PLAIN, 13));
		textAreaSourceScript.setBackground(new Color(255, 255, 170));

		// cardScripts.add(textAreaSourceScript);

		JPanel noWrapPanelSourceScript = new JPanel(new BorderLayout());
		noWrapPanelSourceScript.add(textAreaSourceScript);

		JScrollPane scrollPaneSourceScript = new JScrollPane(
				noWrapPanelSourceScript);
		scrollPaneSourceScript.setBounds(257, 78, 749, 420);
		cardScripts.add(scrollPaneSourceScript);

		JScrollPane scrollPaneListeScripts = new JScrollPane();
		scrollPaneListeScripts.setBounds(17, 77, 225, 421);
		cardScripts.add(scrollPaneListeScripts);

		jListScripts = new JList();
		jListScripts.setFont(new Font("Courier New", Font.PLAIN, 12));
		jListScripts.setBounds(0, 0, 1, 1);

		jListScripts.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				fichierScriptSelectionne = "";
				cheminFichierScriptSelectionne = textCheminScripts.getText();
				String ligne = "";

				// Copier la valeur de la ligne sélectionner dans le textURL
				// afin de traiter ce lien.
				if (jListScripts.getSelectedValue() != null) {

					fichierScriptSelectionne = jListScripts.getSelectedValue()
							.toString();

					fichierScript = GestionnaireFichier
							.creerUnFichierEntree(getContenuParDefaut(
									cheminFichierScriptSelectionne + "\\"
											+ fichierScriptSelectionne,
									"maListeLiensURL.m3u"));

					try {
						textAreaSourceScript.setText("");
						while ((ligne = fichierScript.readLine()) != null
								&& !exit) {
							textAreaSourceScript.append(ligne + "\n");
						}

						textAreaSourceScript.setCaretPosition(1);

					} catch (IOException e) {
						e.printStackTrace();
					}

					/*
					 * String lien = jListScripts.getSelectedValue().toString();
					 * 
					 * if (!lien.toLowerCase().startsWith("http") &&
					 * lien.toLowerCase().endsWith(".m3u")){
					 * rdbtnSelectM3UFile.doClick();
					 * textMaListeM3U.setText(textDirectory.getText() + "\\" +
					 * lien); } else { //String lien =
					 * listLiensMultiple.getSelectedValue().toString();
					 * textURL.enable(); rdbtnSelectURL.doClick();
					 * textURL.setText(lien.substring(lien.indexOf("http"),
					 * lien.length())); }
					 */
				}
			}
		});

		scrollPaneListeScripts.setViewportView(jListScripts);
		// cardScripts.add(jListScripts);

		JLabel lblScriptsDirectory = new JLabel("Répertoire de scripts");
		lblScriptsDirectory.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblScriptsDirectory.setBounds(18, 21, 158, 16);
		cardScripts.add(lblScriptsDirectory);

		textCheminScripts = new JTextField();
		textCheminScripts.setFont(new Font("Courier New", Font.PLAIN, 12));
		textCheminScripts.setBounds(212, 15, 303, 22);
		cardScripts.add(textCheminScripts);
		textCheminScripts.setColumns(10);
		textCheminScripts.setText(textCheminScriptsParDefaut.getText());

		JButton btnFileChooserCheminScripts = new JButton("...");
		btnFileChooserCheminScripts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();

				//chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				chooser.setCurrentDirectory(new File(textCheminScriptsParDefaut.getText()));
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				
				//FileFilter filter = new FileNameExtensionFilter("Scripts Python","py");
				//chooser.addChoosableFileFilter(filter);
				//chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					textCheminScripts.setText(chooser.getSelectedFile().getAbsolutePath());
				}
				
				//Supprimer toute la liste pour la remplir de nouveau
				listScriptsModel.removeAllElements();
				
				//Remplir la JList avec les noms de scripts
				File repertoire = new File(textCheminScripts.getText());
				for (final File fileEntry : repertoire.listFiles()) {
					if (fileEntry.isDirectory()) {
						continue;
					} else {
						if (fileEntry.getName().toLowerCase().endsWith(".py")) {

							// Ajouter l'élément au model
							listScriptsModel.addElement(fileEntry.getName());

							// Associer le model au JList
							jListScripts.setModel(listScriptsModel);
						}
					}
				}
			}
		});
		btnFileChooserCheminScripts.setFont(new Font("Courier New", Font.PLAIN,
				12));
		btnFileChooserCheminScripts.setBounds(527, 13, 41, 22);
		cardScripts.add(btnFileChooserCheminScripts);

		JButton btnExecuterScript = new JButton("Executer");
		btnExecuterScript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BtnExecuterScriptHandler btnExecuterScriptHandler = new BtnExecuterScriptHandler();
				traiterCode(btnExecuterScriptHandler);
			}
		});
		btnExecuterScript.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnExecuterScript.setBounds(144, 510, 90, 28);
		cardScripts.add(btnExecuterScript);

		JLabel lblInfoSortieScript = new JLabel(
				"Le résultat d'exécution de ce script se retrouvera dans le Clipboard");
		lblInfoSortieScript.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblInfoSortieScript.setBounds(275, 508, 520, 16);
		cardScripts.add(lblInfoSortieScript);
		
		JButton btnRaffraichir = new JButton("Raffraichir");
		btnRaffraichir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				BtnRaffraichirHandler btnRaffraichirHandler = new BtnRaffraichirHandler();
				traiterCode(btnRaffraichirHandler);
			}
		});
		btnRaffraichir.setFont(new Font("Courier New", Font.PLAIN, 12));
		btnRaffraichir.setBounds(27, 510, 111, 28);
		cardScripts.add(btnRaffraichir);

		// Créer un model qui contiendrait les informations à afficher dans le
		// JList (scripts python).
		listScriptsModel = new DefaultListModel();

		File repertoire = new File(textCheminScripts.getText());
		for (final File fileEntry : repertoire.listFiles()) {
			if (fileEntry.isDirectory()) {
				continue;
			} else {
				if (fileEntry.getName().toLowerCase().endsWith(".py")) {

					// Ajouter l'élément au model
					listScriptsModel.addElement(fileEntry.getName());

					// Associer le model au JList
					jListScripts.setModel(listScriptsModel);
				}
			}
		}

	}

	public String getContenuParDefaut(String valeurChamp, String valeurParDefaut) {
		if (valeurChamp.equals(null) || valeurChamp.equals("")) {
			valeurChamp = valeurParDefaut;
		}

		return valeurChamp;
	}

	public int compterNbLignesFichier(BufferedReader fichier) {
		String ligne = null;
		int nbLignesFichierEntree = 0;

		try {
			// Compter le nombre de lignes du fichier d'entr�e

			textPaneResult.setText(null);

			while ((ligne = fichier.readLine()) != null) {
				if (ligne.startsWith("http") || ligne.startsWith("HTTP")) {
					nbLignesFichierEntree++;
				}
			}

			textPaneResult.append(
					"*****************************    Nombre total de chaînes = "
							+ nbLignesFichierEntree
							+ "    *****************************", Color.BLACK);
			textPaneResult.append("\r\n\r\n\r\n", Color.BLACK);

			System.out.println("\r\n\r\n\r\n");
			System.out
					.println("*****************************    Nombre total de chaînes = "
							+ nbLignesFichierEntree
							+ "    *****************************");
			System.out.println("\r\n\r\n");
			// On doit le fermer pour le r�ouvrir plus tard afin de recommencer
			// sa lecture depuis le d�but.
			fichier.close();
		} catch (IOException e) {
			System.out.println("Impossible de lire le fichier");
			e.printStackTrace();
		}

		return nbLignesFichierEntree;
	}

	public static InputStream toUTF8(InputStream input)
			throws UnsupportedEncodingException, IOException {
		// https://www.developpez.net/forums/d225600/java/general-java/langage/convertir-inputstream-utf8/
		return toUTF8(input, null);
	}

	public static InputStream toUTF8(InputStream input, String inputCharsetName)
			throws UnsupportedEncodingException, IOException {
		// https://www.developpez.net/forums/d225600/java/general-java/langage/convertir-inputstream-utf8/
		int size = 0;
		byte[] data = new byte[0];
		byte[] buff = new byte[2048];
		int nbRead = 0;

		try {
			while ((nbRead = input.read(buff)) > 0) {
				String sBuff = inputCharsetName == null ? new String(buff)
						: new String(buff, inputCharsetName);
				System.out.println(sBuff);
				size += nbRead;
				byte[] temp = new byte[size];
				System.arraycopy(data, 0, temp, 0, data.length);
				System.arraycopy(sBuff.getBytes("UTF8"), 0, temp, data.length,
						nbRead);
				data = temp;
			}
		} finally {
			input.close();
		}
		return new ByteArrayInputStream(data);
	}

	public String[] parseLiensHttp(String contenu) {

			String leLienHttp = null;
			int indiceLien = 0;
			int tailleMaxTableauDeLiens = 300;
			String[] tableauDeLiens = new String[tailleMaxTableauDeLiens];

			// Extraire juste les URL
			Pattern lePattern = Pattern.compile("http\\w?://[0-9A-Za-z.;,:_\\@#%?&=/-]*");
			// Pattern lePattern =
			// Pattern.compile("/([^<>\"\'\\s]\\s*)(\\b(https?):\\/\\/[-\\w+&@#\\/%?=~|!:,.;]*[-\\w+&@#\\/%=~|])(\\s*[^<>\"\'\\s])/gi");
			Matcher leMatcher = lePattern.matcher(contenu);
			while (leMatcher.find()) {
				// System.out.println(leMatcher.group(0));
				// System.out.println(leMatcher.group(1));
				leLienHttp = leMatcher.group();
				System.out.println(indiceLien + " - " + leLienHttp);
				tableauDeLiens[indiceLien] = leLienHttp;

				// On a juste le droit de collecter 300 liens. On peut
				// l'augmenter ci-dessus
				indiceLien++;
				if (indiceLien > tailleMaxTableauDeLiens - 1) {
					break;
				}
			}
			System.out.println("            ---------------------  Fin ----------------------------\n");

			return tableauDeLiens;
		}

	public String getContenuURL(String uneURL)
			throws UnsupportedEncodingException, IOException {

		HttpURLConnection connexion;
		InputStream flux;
		String fluxConverti = null;

		UrlValidator urlValidator = new UrlValidator();

		if (urlValidator.isValid(uneURL)) {

			URL url = new URL(uneURL);
			connexion = (HttpURLConnection) url.openConnection();
			connexion.setConnectTimeout(2000);
			connexion.setReadTimeout(2000);

			// C'est nécessaire sinon on recevra un 403!
			connexion
					.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

			flux = connexion.getInputStream();
			System.out.print("[" + connexion.getContentLength() + "]   ");
			// System.out.println("Status de la connexion : " +
			// connexion.getResponseMessage());

			ConvertisseurCharset unConvertisseur = new ConvertisseurCharset(
					Integer.parseInt(textTailleBuffer.getText()));
			fluxConverti = unConvertisseur.convertir(flux, "UTF8");

		}

		return fluxConverti;
	}

	public int getNbLiensURL(String flux) {
		String[] tableauDeLiensDuneURL = flux.split("#EXTINF");
		return tableauDeLiensDuneURL.length - 1;
	}

	public void initCompteurs() {
		nbLiensTraites = 0;
		lblNbTotalLiens.setText("Nombre total de liens = 0");
		lblNbLiensTraites.setText("Nombre de liens traités = 0");
		lblNbLiensActifs.setText("Nombre de liens actifs = 0");
	}
	
	//Permet d'appeler le code correspondant à l'action qu'on veut faire
	public void traiterCode(IHandler code){
		code.perform(this);
	}

}
