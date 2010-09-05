package com.baseoneonline.java.mediabrowser.test;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.baseoneonline.java.mediabrowser.Settings;
import com.baseoneonline.java.mediabrowser.core.Database;
import com.baseoneonline.java.mediabrowser.core.MediaScanner;
import com.baseoneonline.java.mediabrowser.core.SQLiteDatabase;
import com.baseoneonline.java.mediabrowser.gui.ScannerDialog;

public class TestScanner extends JFrame {

	public static void main(final String[] args) {
		final Database db = new SQLiteDatabase("media.db");
		final MediaScanner scanner = new MediaScanner(db);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ScannerDialog.get(scanner);
			}
		});
		scanner.scan(Settings.get().getMediaSources());
	}

}
