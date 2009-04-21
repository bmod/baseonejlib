package com.baseoneonline.java.mediadb.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SettingsPanel extends JPanel implements com.baseoneonline.java.mediadb.util.Const {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1073100868264291946L;

	public SettingsPanel() {
		createUI();
	}

	private void createUI() {
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		add(new MediaFoldersPanel());
		add(new ScanPanel());
	}




}
