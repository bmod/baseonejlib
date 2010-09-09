package com.baseoneonline.java.mediabrowser.test;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.baseoneonline.java.mediabrowser.core.Database;
import com.baseoneonline.java.mediabrowser.core.MediaScanner;
import com.baseoneonline.java.mediabrowser.gui.ScannerDialog;

public class TestScanner extends JFrame {

	public static void main(final String[] args) {
		final MediaScanner scanner = new MediaScanner(Database.get());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ScannerDialog.get(scanner, null);
			}
		});
		scanner.scan(Database.get().getMediaSources());
	}

}
