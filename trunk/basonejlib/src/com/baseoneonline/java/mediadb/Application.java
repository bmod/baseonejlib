package com.baseoneonline.java.mediadb;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import com.baseoneonline.java.mediadb.db.AbstractDatabase;
import com.baseoneonline.java.mediadb.events.DefaultEventSource;
import com.baseoneonline.java.mediadb.events.Event;
import com.baseoneonline.java.mediadb.events.EventListener;
import com.baseoneonline.java.mediadb.events.EventSource;
import com.baseoneonline.java.mediadb.events.EventType;

public class Application implements EventSource {
	private final Logger log = Logger.getLogger(getClass().getName());

	private static Application instance;

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
