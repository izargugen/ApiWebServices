package com.sid.ovli.handlers;

import com.sid.ovli.Ttaq;

public class BtnVerifierHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {
		this.ttaq = ttaq;
		
		ttaq.nbLignesFichier = 0;
		
		if (ttaq.rdbtnSelectM3UFile.isSelected()) {
			
			M3UFileHandler m3uFileHandler = new M3UFileHandler();
			ttaq.traiterCode(m3uFileHandler);

		}
		// else{
		if (ttaq.rdbtnSelectURL.isSelected()) {
			URLHandler urlHandler = new URLHandler();
			ttaq.traiterCode(urlHandler);
			
		} // else
		if (ttaq.rdbtnListeM3U.isSelected()) {
			
			ListeM3UHandler listeM3UHandler = new ListeM3UHandler();
			ttaq.traiterCode(listeM3UHandler);

		}
		// else{ //traitement de liens multiples
		if (ttaq.rdbtnMultipleURL.isSelected()) {
			MultipleURLHandler multipleURLHandler = new MultipleURLHandler();
			ttaq.traiterCode(multipleURLHandler);
		}

		if (ttaq.rdbtnSelectDirectory.isSelected()) {
			DirectoryHandler directoryHandler = new DirectoryHandler();
			ttaq.traiterCode(directoryHandler);
		}
	}

}
