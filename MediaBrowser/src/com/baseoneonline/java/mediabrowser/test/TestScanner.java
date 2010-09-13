package com.baseoneonline.java.mediabrowser.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.baseoneonline.java.mediabrowser.core.Database;
import com.baseoneonline.java.mediabrowser.core.MediaScanner;
import com.baseoneonline.java.mediabrowser.gui.ScannerDialog;

public class TestScanner extends JFrame {

	public static void main(final String[] args) {
		
		Logger.getLogger("").setLevel(Level.SEVERE);
		
		final MediaScanner scanner = new MediaScanner(Database.get());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ScannerDialog scannerDialog = ScannerDialog.get(scanner);
				scannerDialog.setVisible(true);
			}
		});
		scanner.scan(Database.get().getMediaSources());
	}

}
