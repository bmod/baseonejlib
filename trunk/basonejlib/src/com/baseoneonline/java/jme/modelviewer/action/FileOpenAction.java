package com.baseoneonline.java.jme.modelviewer.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

public class FileOpenAction extends AbstractAction {

	
	public FileOpenAction() {
		putValue(NAME, "Open...");
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final JFileChooser fc = new JFileChooser();
		if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
			
		}
	}

}
