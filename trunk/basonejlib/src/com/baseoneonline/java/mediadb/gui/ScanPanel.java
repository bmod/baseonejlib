package com.baseoneonline.java.mediadb.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.baseoneonline.java.mediadb.actions.ScanCollectionAction;
import com.baseoneonline.java.mediadb.db.Collection;
import com.baseoneonline.java.mediadb.db.CollectionManager;
import com.baseoneonline.java.mediadb.events.Event;
import com.baseoneonline.java.mediadb.events.EventListener;
import com.baseoneonline.java.mediadb.events.EventType;

public class ScanPanel extends JPanel{
	/**
	 *
	 */
	private static final long serialVersionUID = 5049889326323394079L;
	private final JButton btScan = new JButton(new ScanCollectionAction());
	private final JTextArea taOutput = new JTextArea();

	private final Collection collection = Collection.getInstance();
	private final CollectionManager colManager = CollectionManager.getManager();

	public ScanPanel() {



		taOutput.setEditable(false);

		colManager.addEventListener(EventType.COLLECTION_SCANNING, new EventListener() {
			public void onEvent(final Event e) {
				taOutput.setText("Scanning collection: "+collection.get(collection.size()-1).getFile().getName()
						+"\nItems found: "+collection.size());
			}
		});
		colManager.addEventListener(EventType.COLLECTION_SCANNING_DONE, new EventListener() {
			public void onEvent(final Event e) {
				taOutput.setText("Scanning collection: Done!"
						+"\nItems found: "+collection.size());
			}
		});



		setBorder(new TitledBorder("Media Scanning"));

		final GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		add(btScan, c);

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		add(taOutput, c);

	}
}
