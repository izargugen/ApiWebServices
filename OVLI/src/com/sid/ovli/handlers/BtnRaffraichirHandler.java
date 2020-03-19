package com.sid.ovli.handlers;

import java.io.File;

import com.sid.ovli.Ttaq;

public class BtnRaffraichirHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {
		this.ttaq = ttaq;
		
		//Supprimer toute la liste pour la remplir de nouveau
		ttaq.listScriptsModel.removeAllElements();
		
		//Remplir la JList avec les noms de scripts
		File repertoire = new File(ttaq.textCheminScripts.getText());
		for (final File fileEntry : repertoire.listFiles()) {
			if (fileEntry.isDirectory()) {
				continue;
			} else {
				if (fileEntry.getName().toLowerCase().endsWith(".py")) {

					// Ajouter l'élément au model
					ttaq.listScriptsModel.addElement(fileEntry.getName());

					// Associer le model au JList
					ttaq.jListScripts.setModel(ttaq.listScriptsModel);
				}
			}
		}
	}

}
