package com.baseoneonline.java.mediadb;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.baseoneonline.java.mediadb.db.AbstractDatabase;
import com.baseoneonline.java.mediadb.db.MemoryDatabase;
import com.baseoneonline.java.mediadb.gui.MainFrame;
import com.baseoneonline.java.mediadb.util.Conf;



public class Main {

	private final Logger log = Logger.getLogger(getClass().getName());


	public Main() {

		Conf.read("mediaDB.cfg");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final MainFrame app = new MainFrame();
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				app.setTitle("MediaDB");
				app.setSize(800, 600);
				app.setVisible(true);
			}

		});

		// Create a database to store items in
		final AbstractDatabase db = new MemoryDatabase();

		log.info("Initializing application...");
		Application.getInstance().initialize(db);

		//list.setDatabase(Application.getInstance().getDatabase());
	}



	public static void main(final String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) {
			e.printStackTrace();
		}

		try {
			new Main();
		} catch (final Exception e) {
			System.exit(1);
		}
	}

}
