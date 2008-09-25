package com.baseoneonline.java.mediadb;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.baseoneonline.java.jlib.utils.Configuration;
import com.baseoneonline.java.mediadb.db.DatabaseRecord;
import com.baseoneonline.java.mediadb.ui.FilteredList;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;



public class FileLister extends JFrame {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	
	private final Configuration config = Configuration.getConfiguration(new File("mediaDB.cfg"));

	private JTextArea taStatus;
	private final String databaseFile;
	private final ObjectContainer database;
	private final File logFile;

	private FilteredList list;

	private final String[] imageExtensions = { "jpg", "jpeg", "gif", "tif",
			"tiff" };
	private final String[] musicExtensions = { "mp3", "mpa", "wmv", "wav","mpaa" };

	private final String[] movieExtensions = { "mpg", "avi", "divx", "mpv","ogg", "mov" };

	public FileLister() {
		logFile = new File(config.get("logFile", "mediaDB.log"));

		databaseFile = "mediaDB.db";
		if (logFile.exists())
			logFile.delete();

		database = Db4o.openFile(databaseFile);

		createUI();
		
		list.setDatabase(database);

		//indexFolder(new File("F:/"));
	}
	
	private void createUI() {
		setLayout(new BorderLayout());
		
		JTabbedPane tabPane = new JTabbedPane();
		
		list = new FilteredList();
		tabPane.addTab("List",list);

		JPanel updatePanel = new JPanel(new BorderLayout());
		taStatus = new JTextArea();
		updatePanel.add(taStatus, BorderLayout.CENTER);
		JPanel updatePanelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		updatePanel.add(updatePanelTop, BorderLayout.NORTH);
		JButton btUpdate = new JButton("Update");
		btUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				indexFolder(new File("F:/"));
			}
		});
		updatePanelTop.add(btUpdate);
		tabPane.addTab("Update", updatePanel);
		
		add(tabPane);
		
	}

	private void indexFolder(final File root) {
		if (!root.isDirectory()) {
			log.warning("Invalid directory: "+root.getAbsolutePath());
			return;
		}
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				listFiles(root);
				return null;
			}

			@Override
			protected void done() {
				updateStatus("Done");
				super.done();
			}
		}.execute();
	}

	private int fileCounter = 0;
	private final int imageCounter = 0;
	private final int movieCounter = 0;
	private final int musicCounter = 0;

	private synchronized void updateStatus(String msg) {
		fileCounter++;

		taStatus.setText("Files checked\t" + fileCounter + "\n" + "checking:\n"
				+ msg + "\n" + "\n" + "images found\t" + imageCounter + "\n"
				+ "movies found\t" + movieCounter + "\n" + "music found\t"
				+ musicCounter + "\n" + "\n");
		// Runtime.getRuntime().gc();
		// }
	}
	
	@SuppressWarnings("unchecked")
	private void addRecord(String filename, int type) {
		Query q = database.query();
		q.constrain(DatabaseRecord.class);
		q.descend("filename").constrain(filename);
		ObjectSet<DatabaseRecord> re =	q.execute();
		if (re.size() > 0) return;
		database.set(new DatabaseRecord(filename, type));
	}
	

	private void listFiles(File dir) throws IOException {
		for (File f : dir.listFiles()) {
			if (f.isDirectory() && f.getCanonicalPath().equals(f.getPath())) {
				listFiles(f);
			} else {
				try {

					if (f.length() >= 4) {

						String fname = f.getName();

						if (fname.endsWith("jpeg") || fname.endsWith("jpg")) {
							addRecord(f.getAbsolutePath(), DatabaseRecord.TYPE_IMAGE);
						} else if (fname.endsWith("mp3")
								|| fname.endsWith("mpa")
								|| fname.endsWith("ogg")) {
							addRecord(f.getAbsolutePath(), DatabaseRecord.TYPE_MUSIC);
						} else if (fname.endsWith("mpg")
								|| fname.endsWith("avi")
								|| fname.endsWith("flv")
								|| fname.endsWith("wmv")
								|| fname.endsWith("mov")) {
							addRecord(f.getAbsolutePath(), DatabaseRecord.TYPE_MOVIE);
						}

						/*
						 * // Set up file reading FileInputStream fin = new
						 * FileInputStream(f); FileChannel ch =
						 * fin.getChannel(); // Allocate 4 bytes of memory
						 * MappedByteBuffer mb = ch.map(MapMode.READ_ONLY, 0L,
						 * 4); // Read header data int fileMarker = mb.getInt();
						 * ch.close();
						 * 
						 * if (isJPEG(fileMarker)) { }
						 */

					}

				} catch (Exception e) {
					System.out.println(e.getCause() + "\n" + e.getMessage());
				}

				updateStatus(f.getAbsolutePath());

			}
		}
	}

	private boolean isJPEG(int marker) {
		// JPEG
		// Check for SOI Marker and JFIF marker
		// info from
		// http://www.obrador.com/essentialjpeg/HeaderInfo.htm
		int jpegMarker = 0xFFD8FFE0;

		if (jpegMarker == marker) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FileLister app = new FileLister();
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				app.setSize(800, 600);
				app.setVisible(true);
			}

		});
	}

}
