package com.sid.ovli.handlers;

import java.io.File;

import javax.swing.JFileChooser;

import com.sid.ovli.Ttaq;

public class BtnFileChooserFichierEntreeM3UHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {
		this.ttaq = ttaq;
		
		JFileChooser chooser = new JFileChooser();

		// chooser.setCurrentDirectory(new
		// File(System.getProperty("user.home") +
		// System.getProperty("file.separator")+ "Music"));
		// chooser.setCurrentDirectory(new
		// File(System.getProperty("user.dir")));
		chooser.setCurrentDirectory(new File(
				"C:\\Users\\SQ63215\\Desktop"));

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ttaq.textDirectory.setText(chooser.getSelectedFile()
					.getAbsolutePath());
		}

	}

}
