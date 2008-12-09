package com.baseoneonline.java.jme.modelviewer.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jdesktop.application.Application;

public class QuitAction extends AbstractAction {
	
	public QuitAction() {
		putValue(NAME, "Quit");
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		Application.getInstance().quit(e);
	}

}
