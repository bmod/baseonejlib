package com.baseoneonline.java.mediadb.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.baseoneonline.java.mediadb.db.CollectionManager;

public class ScanCollectionAction extends AbstractAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -3376958759681970737L;

	public ScanCollectionAction() {
		putValue(Action.NAME, "Scan Collection");
	}

	public void actionPerformed(final ActionEvent arg0) {

		CollectionManager.getManager().scanCollection(false);

	}

}
