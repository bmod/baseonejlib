package com.baseoneonline.java.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serializable;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileSelector extends JPanel implements Serializable {

	private File file;
	private final JLabel lbName;
	private final JTextField tfFile;
	private final JButton btBrowse;

	public FileSelector(final String name) {

		setLayout(new BorderLayout());
		lbName = new JLabel(name);
		add(lbName, BorderLayout.WEST);
		tfFile = new JTextField();
		add(tfFile);
		btBrowse = new JButton(new AbstractAction("...") {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				browseForFile();
			}
		});
		add(btBrowse, BorderLayout.EAST);

	}

	private void browseForFile() {
		final JFileChooser fc = new JFileChooser();
		if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
			final File newFile = fc.getSelectedFile();
			final File oldFile = file;
			file = newFile;
			firePropertyChange("File", oldFile, newFile);
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(final File file) {
		this.file = file;
	}

}
