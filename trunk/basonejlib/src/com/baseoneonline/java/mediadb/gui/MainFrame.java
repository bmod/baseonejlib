package com.baseoneonline.java.mediadb.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5026037771498507028L;

	private SettingsPanel updatePanel;

	private FilteredListPanel list;



	public MainFrame() {
		createUI();
	}
	private void createUI() {
		setLayout(new BorderLayout());

		final JTabbedPane tabPane = new JTabbedPane();

		updatePanel = new SettingsPanel();
		tabPane.addTab("Settings", updatePanel);

		list = new FilteredListPanel();
		tabPane.addTab("List",list);

		add(tabPane);

	}
}
