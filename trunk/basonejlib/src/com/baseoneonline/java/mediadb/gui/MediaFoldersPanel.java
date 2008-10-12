package com.baseoneonline.java.mediadb.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.baseoneonline.java.mediadb.actions.ScanCollectionAction;
import com.baseoneonline.java.mediadb.util.Conf;
import com.baseoneonline.java.mediadb.util.Const;

public class MediaFoldersPanel extends JPanel {

	/**
	 *
	 */
	private static final long		serialVersionUID	= 7209289547714450371L;
	private final JTextArea			taStatus;
	private final DefaultListModel	directoryModel		= new DefaultListModel();
	private final JButton			btAdd				= new JButton("Add");
	private final JButton			btRemove			= new JButton("Remove");
	private final JButton			btBrowse			= new JButton("Browse");
	private final JTextField		tfEditDirectory		= new JTextField();
	private final JList				lsDirectory			= new JList(
																directoryModel);
	private int						selectedIndex;

	/**
	 *
	 */
	/**
	 *
	 */
	public MediaFoldersPanel() {

		tfEditDirectory.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(final KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {

				}
			}

			@Override
			public void keyPressed(final KeyEvent e) {}

			@Override
			public void keyReleased(final KeyEvent e) {}
		});

		// BUTTONS

		btAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final File f = Utils.promptUserForDirectory();
				if (null != f) {
					final String dir = f.getAbsolutePath();
					tfEditDirectory.setText(dir);
					directoryModel.add(directoryModel.size(), dir);
					storeDirectories();
				}
			}
		});

		btRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				directoryModel.remove(selectedIndex);
				storeDirectories();
			}
		});

		btBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {

				final File f = Utils.promptUserForDirectory();
				if (null != f) {
					final String dir = f.getAbsolutePath();
					tfEditDirectory.setText(dir);
					directoryModel.setElementAt(dir, selectedIndex);
					storeDirectories();
				}

			}
		});

		lsDirectory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsDirectory.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(final ListSelectionEvent e) {
						selectedIndex = lsDirectory.getSelectedIndex();
						updateMediaFolderSelection(lsDirectory
								.getSelectedValue());
					}
				});
		for (final String dir : Conf.getStringArray(Const.MEDIA_FOLDERS)) {
			directoryModel.addElement(dir);
		}
		final JScrollPane listScrollPane = new JScrollPane(lsDirectory);

		taStatus = new JTextArea();
		taStatus.setEditable(false);
		// updatePanel.add(taStatus, BorderLayout.CENTER);
		final JPanel updatePanelTop = new JPanel(
				new FlowLayout(FlowLayout.LEFT));
		// updatePanel.add(updatePanelTop, BorderLayout.NORTH);
		final JTextField tfPath = new JTextField(Conf
				.getStringArray(Const.MEDIA_FOLDERS)[0]);
		tfPath.addKeyListener(new KeyListener() {
			public void keyPressed(final KeyEvent e) {}

			public void keyReleased(final KeyEvent e) {}

			public void keyTyped(final KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					final String[] folders = { tfPath.getText() };
					Conf.setStringArray(Const.MEDIA_FOLDERS, folders);
				}
			}
		});
		updatePanelTop.add(tfPath);
		final JButton btUpdate = new JButton(new ScanCollectionAction());
		updatePanelTop.add(btUpdate);

		// LAYOUT

		setBorder(new TitledBorder("Media Folders"));
		final GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		add(listScrollPane, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		add(btAdd, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(btRemove, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 0;
		c.weighty = 0;
		add(btBrowse, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		c.weighty = 0;
		add(tfEditDirectory, c);

		updateMediaFolderSelection(null);
	}

	private void updateMediaFolderSelection(final Object selection) {
		final boolean isSelected = (null != selection);
		if (isSelected) {
			tfEditDirectory.setText(selection.toString());
		} else {
			tfEditDirectory.setText("");
		}
		tfEditDirectory.setEnabled(isSelected);
		btRemove.setEnabled(isSelected);
		btBrowse.setEnabled(isSelected);

	}

	/**
	 * Store dirs in config.
	 */
	private void storeDirectories() {
		final String[] paths = new String[directoryModel.getSize()];
		for (int i = 0; i < directoryModel.getSize(); i++) {
			paths[i] = directoryModel.get(i).toString();
		}
		Conf.setStringArray(Const.MEDIA_FOLDERS, paths);
	}

}
