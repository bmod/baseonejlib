/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SettingsDialog.java
 *
 * Created on Sep 5, 2010, 11:54:51 AM
 */

package com.baseoneonline.java.mediabrowser.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.baseoneonline.java.mediabrowser.Settings;

/**
 * 
 * @author bmod
 */
public class SettingsDialog extends javax.swing.JDialog {

	Settings settings;

	private static SettingsDialog instance;

	/** Creates new form SettingsDialog */
	private SettingsDialog(final java.awt.Frame parent, final boolean modal,
			final Settings settings) {
		super(parent, modal);
		this.settings = settings;
		initComponents();

		directoryListEditor.setDirectories(settings.getMediaSources());

		directoryListEditor.addListener(new DirectoryListEditor.Listener() {

			@Override
			public void dataChanged() {
				settings.setMediaSources(directoryListEditor.getDirectories());
			}
		});

		fileTypeEditor1.setFileTypes(settings.getMediaFileTypes());

		fileTypeEditor1.addListener(new FileTypeListEditor.Listener() {

			@Override
			public void dataChanged() {
				settings.setFileTypes(fileTypeEditor1.getFileTypes());
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				settings.flush();
			}
		});

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jTabbedPane1 = new javax.swing.JTabbedPane();
		directoryListEditor =
				new com.baseoneonline.java.mediabrowser.gui.DirectoryListEditor();
		fileTypeEditor1 =
				new com.baseoneonline.java.mediabrowser.gui.FileTypeListEditor();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jTabbedPane1.addTab("Media Sources", directoryListEditor);
		jTabbedPane1.addTab("Media Types", fileTypeEditor1);

		final javax.swing.GroupLayout layout =
				new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300,
				Short.MAX_VALUE));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				final SettingsDialog dialog =
						new SettingsDialog(new javax.swing.JFrame(), true, null);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(final java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.baseoneonline.java.mediabrowser.gui.DirectoryListEditor directoryListEditor;
	private com.baseoneonline.java.mediabrowser.gui.FileTypeListEditor fileTypeEditor1;
	private javax.swing.JTabbedPane jTabbedPane1;

	// End of variables declaration//GEN-END:variables

	public static SettingsDialog get(final Settings settings) {
		if (null == instance)
			instance = new SettingsDialog(null, true, settings);
		instance.setVisible(true);
		return instance;
	}

}
