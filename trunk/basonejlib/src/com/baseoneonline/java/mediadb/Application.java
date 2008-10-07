package com.baseoneonline.java.mediadb;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import com.baseoneonline.java.jlib.utils.Config;
import com.baseoneonline.java.mediadb.db.AbstractDatabase;
import com.baseoneonline.java.mediadb.db.DatabaseListModel;
import com.baseoneonline.java.mediadb.db.MediaItem;
import com.baseoneonline.java.mediadb.events.DefaultEventSource;
import com.baseoneonline.java.mediadb.events.Event;
import com.baseoneonline.java.mediadb.events.EventListener;
import com.baseoneonline.java.mediadb.events.EventSource;
import com.baseoneonline.java.mediadb.events.EventType;
import com.baseoneonline.java.mediadb.ui.Constants;

public class Application implements EventSource {
	private final Logger log = Logger.getLogger(getClass().getName());

	private static Application instance;

	private DatabaseListModel listModel;
	private AbstractDatabase database;
	final String[][] exts = { 
			{ "jpg", "jpeg", "png", "tif", "tiff", "gif" },
			{ "mp3", "mpa" }, 
			{ "mov", "wmv", "avi" },
			{ "sfc", "smc", "nes" } 
	};
	private final EventSource eventSource = new DefaultEventSource();

	private final Vector<ActionListener> listeners = new Vector<ActionListener>();

	private Application() {

	}

	public void initialize(AbstractDatabase db) {
		database = db;
	}

	public void scanCollection() {
		final File root = new File(Config.getConfig().get(
				Constants.CFG_MEDIA_PATH));
		if (!root.isDirectory()) {
			log.warning("Invalid directory: " + root.getAbsolutePath());
			return;
		}
		new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() throws Exception {
				listFiles(root);
				return "Done";
			}

			@Override
			protected void done() {
				updateStatus("Done");
				super.done();
			}
		}.execute();
	}

	@SuppressWarnings("unchecked")
	public void addItem(String filename, int type) {
		MediaItem item = new MediaItem(filename, type);
		if (!database.contains(item)) {
			database.addItem(item);
		}
		/*
		 * Query q = database.query(); q.constrain(MediaItem.class);
		 * q.descend("filename").constrain(filename); ObjectSet<MediaItem> re =
		 * q.execute(); if (re.size() > 0) return; database.set(new
		 * MediaItem(filename, type));
		 */
	}

	private synchronized void updateStatus(String msg) {

		/*
		 * taStatus.setText("Files checked\t" + fileCounter + "\n" +
		 * "checking:\n" + msg + "\n" + "\n" + "images found\t" + imageCounter +
		 * "\n" + "movies found\t" + movieCounter + "\n" + "music found\t" +
		 * musicCounter + "\n" + "\n"); // Runtime.getRuntime().gc(); // }
		 * 
		 */
	}

	/**
	 * @param filename
	 * @return -1 if not recognized or int denoting the type.
	 */
	private int getMediaType(File f) {

		String fname = f.getName();


		
		for (int i=0; i<exts.length; i++){
			for (String ext : exts[i]) {
				if (fname.endsWith(ext)) {
					return i;
				}
			}
		}
		return -1;
	}

	private void listFiles(File dir) throws IOException {
		for (File f : dir.listFiles()) {
			if (f.isDirectory() && f.getCanonicalPath().equals(f.getPath())) {
				listFiles(f);
			} else {
				try {

					if (f.length() >= 4) {

						int type = getMediaType(f);
						if (-1 != type) {
							addItem(f.getName(), type);
						} else {
							// type not found
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

	public static Application getInstance() {
		if (instance == null)
			instance = new Application();
		return instance;
	}

	public void addListener(EventType type, EventListener lst) {
		eventSource.addListener(type, lst);
	}

	public void fireEvent(Event evt) {
		eventSource.fireEvent(evt);
	}

	public void removeListener(EventType type, EventListener lst) {
		eventSource.removeListener(type, lst);
	}
}
