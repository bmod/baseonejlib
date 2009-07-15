package com.baseoneonline.java.guerrilla;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.commons.lang.StringUtils;

public class CorrectSkeletons extends JPanel {

	FileSelector fileSelector;

	Preferences prefs = Preferences.userNodeForPackage(getClass());

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame();
		
		frame.setSize(800, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		final CorrectSkeletons panel = new CorrectSkeletons();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				panel.windowClosing();
				System.exit(0);
			}
		});
		
		frame.setTitle(panel.getClass().getSimpleName());
		frame.add(panel);
		frame.setVisible(true);

	}

	public void windowClosing() {
		prefs.put("dirHistory", StringUtils
				.join(fileSelector.getHistory(), ";"));
	}

	public CorrectSkeletons() {
		BoxLayout box = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(box);

		fileSelector = new FileSelector();
		add(fileSelector);

		fileSelector.setHistory(prefs.get("dirHistory", "").split(";"));

	}

}

class FileSelector extends JPanel {

	private JComboBox cbFile;
	private JButton btBrowse;
	private File file;
	private DefaultComboBoxModel model;
	private int fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES;

	public FileSelector() {
		model = new DefaultComboBoxModel();
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		setLayout(new BorderLayout());
		cbFile = new JComboBox(model);
		add(cbFile);
		btBrowse = new JButton(browseAction);
		add(btBrowse, BorderLayout.EAST);
	}

	public void setHistory(String[] files) {
		model.removeAllElements();
		for (String f : files) {
			model.addElement(new File(f));
		}
	}

	public String[] getHistory() {
		int len = model.getSize();
		String[] fs = new String[len];
		for (int i = 0; i < len; i++) {
			fs[i] = ((File) model.getElementAt(i)).getAbsolutePath();
		}
		cbFile.setSelectedIndex(model.getSize()-1);
		return fs;
	}

	public File getFile() {
		return file;
	}

	private Action browseAction = new AbstractAction("Browse...") {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(fileSelectionMode);
			if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
				file = fc.getSelectedFile();
				model.addElement(file);
				cbFile.setSelectedItem(file);
			}
		}
	};

}
