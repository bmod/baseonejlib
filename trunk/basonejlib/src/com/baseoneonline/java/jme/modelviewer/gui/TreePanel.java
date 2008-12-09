package com.baseoneonline.java.jme.modelviewer.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class TreePanel extends JPanel {
	public TreePanel() {
		setLayout(new BorderLayout());
		final JTree tree = new JTree();
		add(new JScrollPane(tree));
	}
}