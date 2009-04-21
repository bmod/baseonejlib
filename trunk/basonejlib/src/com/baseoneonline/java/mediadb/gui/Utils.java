package com.baseoneonline.java.mediadb.gui;

import java.io.File;

import javax.swing.JFileChooser;

public class Utils {
	public static File promptUserForDirectory() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);

		if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
			return fc.getSelectedFile();
		}
		return null;

	}
}
