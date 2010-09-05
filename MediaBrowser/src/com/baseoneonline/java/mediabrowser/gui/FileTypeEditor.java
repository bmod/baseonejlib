/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FileTypeEditor.java
 *
 * Created on Sep 5, 2010, 1:44:03 PM
 */

package com.baseoneonline.java.mediabrowser.gui;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import com.baseoneonline.java.mediabrowser.core.FileType;
import com.baseoneonline.java.mediabrowser.util.ArrayListModel;

/**
 * 
 * @author bmod
 */
public class FileTypeEditor extends javax.swing.JPanel {

	private FileType type;

	private final ArrayListModel<String> extensions =
			new ArrayListModel<String>();

	private final ArrayList<Listener> listeners = new ArrayList<Listener>();

	/** Creates new form FileTypeEditor */
	public FileTypeEditor() {
		initComponents();
	}

	public void addListener(final Listener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	public void removeListener(final Listener l) {
		listeners.remove(l);
	}

	public void setFileType(final FileType type) {
		this.type = type;
		tfName.setText(type.getName());
		extensions.clear();
		for (final String ext : type.getExtensions()) {
			extensions.add(ext);
		}
		listExtensions.setSelectedIndex(-1);
		btRemove.setEnabled(false);
	}

	public FileType getFileType() {
		return type;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		tfName.setEnabled(enabled);
		listExtensions.setEnabled(enabled);
		btRemove.setEnabled(enabled);
		btAdd.setEnabled(enabled);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listExtensions = new javax.swing.JList();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();

        jLabel1.setText("Name (confirm with enter key)");

        tfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfNameActionPerformed(evt);
            }
        });

        jLabel2.setText("Extensions");

        listExtensions.setModel(extensions);
        listExtensions.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listExtensionsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listExtensions);

        btAdd.setText("Add...");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btRemove.setText("Remove");
        btRemove.setEnabled(false);
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfName, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btRemove, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                            .addComponent(btAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btRemove))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void listExtensionsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listExtensionsValueChanged
		boolean enable = listExtensions.getSelectedIndices().length > 0;
		btRemove.setEnabled(enable);
	}//GEN-LAST:event_listExtensionsValueChanged

	private void btRemoveActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btRemoveActionPerformed
		for (final int i : listExtensions.getSelectedIndices()) {
			extensions.remove(i);
		}
		fireDataChanged();
	}// GEN-LAST:event_btRemoveActionPerformed

	private void btAddActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btAddActionPerformed
		final String input = JOptionPane.showInputDialog(btAdd, "Extension?");
		if (null != input)
			extensions.add(input.trim());
		fireDataChanged();
	}// GEN-LAST:event_btAddActionPerformed

	private void tfNameActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tfNameActionPerformed
		fireDataChanged();
	}// GEN-LAST:event_tfNameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList listExtensions;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables

	private void fireDataChanged() {
		Collections.sort(extensions);
		type.setName(tfName.getText().trim());
		type.setExtensions(extensions.toArray(new String[extensions.size()]));

		for (final Listener l : listeners) {
			l.dataChanged();
		}
	}

	public static interface Listener {
		public void dataChanged();
	}

}
