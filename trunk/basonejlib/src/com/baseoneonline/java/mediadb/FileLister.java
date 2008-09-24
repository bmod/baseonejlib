package com.baseoneonline.java.mediadb;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.baseoneonline.java.jlib.utils.Configuration;
import com.baseoneonline.java.mediadb.db.DatabaseListModel;
import com.baseoneonline.java.mediadb.db.ImageRecord;
import com.baseoneonline.java.mediadb.db.MovieRecord;
import com.baseoneonline.java.mediadb.db.MusicRecord;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;



public class FileLister extends JFrame {
	
	private final Configuration config = Configuration.getConfiguration(new File("mediaDB.cfg"));

	private final JTextArea lbStatus;
	private final String databaseFile;
	private final ObjectContainer database;
	private final File logFile;

	private final JList imageList;
	private final DatabaseListModel<ImageRecord> imageListModel;

	private final String[] imageExtensions = { "jpg", "jpeg", "gif", "tif",
			"tiff" };
	private final String[] musicExtensions = { "mp3", "mpa", "wmv", "wav","mpaa" };

	private final String[] movieExtensions = { "mpg", "avi", "divx", "mpv","ogg", "mov" };

	public FileLister() {
		logFile = new File(config.get("logFile", "mediaDB.log"));
		/*
		imageExtensions = config.get("imageExtensions", imageExtensions);
		musicExtensions = config.get("musicExtensions", musicExtensions);
		movieExtensions = config.get("movieExtensions", movieExtensions);
		*/
		databaseFile = config.get("databaseFile", "mediaDB.db");
		if (logFile.exists())
			logFile.delete();

		database = Db4o.openFile(databaseFile);

		JTabbedPane tabPane = new JTabbedPane();

		lbStatus = new JTextArea();
		tabPane.addTab("Status", lbStatus);

		imageListModel = new DatabaseListModel<ImageRecord>();
		imageList = new JList(imageListModel);
		JScrollPane spImageList = new JScrollPane(imageList);
		tabPane.addTab("Images", spImageList);

		add(tabPane);

		indexFolder(new File("F:/"));
	}

	private void indexFolder(final File root) {
		if (!root.isDirectory())
			return;
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
	private int imageCounter = 0;
	private int movieCounter = 0;
	private int musicCounter = 0;

	private synchronized void updateStatus(String msg) {
		fileCounter++;

		lbStatus.setText("Files checked\t" + fileCounter + "\n" + "checking:\n"
				+ msg + "\n" + "\n" + "images found\t" + imageCounter + "\n"
				+ "movies found\t" + movieCounter + "\n" + "music found\t"
				+ musicCounter + "\n" + "\n");
		// Runtime.getRuntime().gc();
		// }

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
							database.set(new ImageRecord(f.getAbsolutePath()));
							imageCounter++;
						} else if (fname.endsWith("mp3")
								|| fname.endsWith("mpa")
								|| fname.endsWith("ogg")) {
							database.set(new MusicRecord(f.getAbsolutePath()));
							musicCounter++;
						} else if (fname.endsWith("mpg")
								|| fname.endsWith("avi")
								|| fname.endsWith("flv")
								|| fname.endsWith("wmv")
								|| fname.endsWith("mov")) {
							database.set(new MovieRecord(f.getAbsolutePath()));
							movieCounter++;
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
