package com.baseoneonline.java.mediadb;

import java.awt.BorderLayout;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.baseoneonline.java.mediadb.db.AbstractDatabase;
import com.baseoneonline.java.mediadb.db.MemoryDatabase;
import com.baseoneonline.java.mediadb.ui.FilteredListPanel;
import com.baseoneonline.java.mediadb.ui.SettingsPanel;
import com.baseoneonline.java.mediadb.util.Conf;



public class Main extends JFrame {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	
	private SettingsPanel updatePanel;

	private FilteredListPanel list;


	public Main() {
		
		Conf.read("mediaDB.cfg");

		createUI();
		
		// Create a database to store items in 
		AbstractDatabase db = new MemoryDatabase();
		
		log.info("Initializing application...");
		Application.getInstance().initialize(db);
		
		//list.setDatabase(Application.getInstance().getDatabase());
	}
	
	private void createUI() {
		setLayout(new BorderLayout());
		
		JTabbedPane tabPane = new JTabbedPane();
		
		list = new FilteredListPanel();
		tabPane.addTab("List",list);

		updatePanel = new SettingsPanel();
		tabPane.addTab("Settings", updatePanel);
		
		add(tabPane);
		
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main app = new Main();
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				app.setSize(800, 600);
				app.setVisible(true);
			}

		});
	}

}
