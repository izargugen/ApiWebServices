package com.sid.ovli.handlers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sid.ovli.MonJTextPane;
import com.sid.ovli.MonStreamGobbler;
import com.sid.ovli.Ttaq;

public class BtnExecuterScriptHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {

		this.ttaq = ttaq;
		
		try {
			
			// Création d'une console en forme de popup
			MonJTextPane textConsoleScript = new MonJTextPane();
			textConsoleScript.setFont(new Font("Courier New", Font.PLAIN, 13));
			textConsoleScript.setBackground(new Color(255, 255, 170));

			// Afin d'afficher une ligne de texte dépassant la largeur du textPane
			// en affichant le scroll horizontal
			JPanel noWrapConsoleScriptPanel = new JPanel(new BorderLayout());
			noWrapConsoleScriptPanel.add(textConsoleScript);
			// JScrollPane scrollPane = new JScrollPane( noWrapPanel );

			JScrollPane scrollPaneConsoleScript = new JScrollPane(noWrapConsoleScriptPanel);
			
			/*
			JDialog dialog = new JDialog();
			//dialog.setModal(true);
			dialog.setTitle("Exécution du script");
			dialog.add(scrollPaneConsoleScript);
			dialog.setBounds(300, 200, 700, 400);

			dialog.setVisible(true);
			*/
			
			JFrame fenetreConsole = new JFrame();
			//dialog.setModal(true);
			fenetreConsole.setTitle("Exécution du script");
			fenetreConsole.getContentPane().add(scrollPaneConsoleScript);
			fenetreConsole.setBounds(300, 200, 700, 400);

			fenetreConsole.setVisible(true);
			/*
			String resultatPython = "";
			
			while ((resultatPython = in.readLine()) != null) {
				textConsoleScript.append(resultatPython, Color.BLACK);
				System.out.println(resultatPython);
			}
			in.close();
			*/
			textConsoleScript.append("Exécution du script en cours ...\n", Color.BLACK);
			
			
			String script = "\"" + ttaq.cheminFichierScriptSelectionne + "\\" + ttaq.fichierScriptSelectionne + "\"";
			System.out.println("Script = " + ttaq.textPythonDefaultPath.getText() + " " + script);

			Process p = Runtime.getRuntime().exec(ttaq.textPythonDefaultPath.getText() + " " + script);
			//BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			 // any error message?
			MonStreamGobbler errorGobbler = new MonStreamGobbler(p.getErrorStream(), "ERROR");            
            
            // any output?
			MonStreamGobbler outputGobbler = new MonStreamGobbler(p.getInputStream(), "OUTPUT");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
                                    
            // any error???
            int exitVal;
			try {
				exitVal = p.waitFor();
				System.out.println("ExitValue: " + exitVal);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
                
			textConsoleScript.append("\nTerminé.", Color.BLACK);
			textConsoleScript.append(outputGobbler.getResultat(), Color.BLACK);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
