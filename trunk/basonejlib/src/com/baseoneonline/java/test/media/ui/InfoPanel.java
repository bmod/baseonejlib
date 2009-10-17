package com.baseoneonline.java.test.media.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.baseoneonline.java.test.media.nodes.FileNode;
import com.baseoneonline.java.test.media.nodes.INode;

public class InfoPanel extends JPanel {

	JTextArea textArea;

	public InfoPanel() {
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		add(textArea);
	}

	public void setNode(final INode node) {
		final StringBuffer buf = new StringBuffer();
		buf.append("Name: " + node.toString() + "\n");
		if (node instanceof FileNode) {
			final FileNode fn = (FileNode) node;
			buf.append("Path: " + fn.file.getAbsolutePath() + "\n");
		}
		textArea.setText(buf.toString());
	}
}
