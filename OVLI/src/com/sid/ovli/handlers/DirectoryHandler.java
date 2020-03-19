package com.sid.ovli.handlers;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.sid.ovli.Ttaq;

public class DirectoryHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {

		this.ttaq = ttaq;
		
		ttaq.textPaneResult.setText(null);

		// Cr�ation du Model qui permet d'avoir la liste de
		// valeurs � mettre dans la JList
		DefaultListModel listModel = new DefaultListModel();

		// Un CellRenderer afin d'ins�rer l'�l�ment en vert dans
		// la liste.
		ttaq.listLiensMultiple
				.setCellRenderer(new DefaultListCellRenderer() {
					public Component getListCellRendererComponent(
							JList list, Object value,
							int index, boolean isSelected,
							boolean cellHasFocus) {
						// On appele la m�thode parent qui
						// initialise tout:
						super.getListCellRendererComponent(
								list, value, index, isSelected,
								cellHasFocus);

						// Le param�tre 'value' correspond � la
						// valeur de la ligne courante
						// On s'en sert pour d�terminer la
						// couleur de fond � appliquer
						setForeground(new Color(24, 131, 56));

						return this;
					}

				});

		File repertoire = new File(ttaq.textDirectory.getText());
		for (final File fileEntry : repertoire.listFiles()) {
			if (fileEntry.isDirectory()) {
				continue;
			} else {
				if (fileEntry.getName().toLowerCase()
						.endsWith(".m3u")) {
					listModel.addElement(fileEntry.getName());
					ttaq.listLiensMultiple.setModel(listModel);
				}
			}
		}

		// Thread monThreadTraiterURLMultiple = new
		// ThreadTraiterURLMultiple();
		// monThreadTraiterURLMultiple.start();
	
	}

}
