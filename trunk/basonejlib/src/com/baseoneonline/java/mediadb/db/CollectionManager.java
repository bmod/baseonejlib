package com.baseoneonline.java.mediadb.db;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Logger;

import com.baseoneonline.java.mediadb.util.Conf;
import com.baseoneonline.java.mediadb.util.Const;
import com.baseoneonline.java.mediadb.util.SupportedFilenameFilter;



public class CollectionManager implements Const {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	
	private static CollectionManager instance;
	
	private final FilenameFilter supportedTypesFilter = new SupportedFilenameFilter();
	
	private CollectionManager() {
		
	}
	
	
	public void scanCollection() {
		log.info("Scanning collection...");
		final String[] mediaFolders = Conf.getStringArray(MEDIA_FOLDERS);
		Thread t = new Thread() {
			@Override
			public void run() {
				for (String fname : mediaFolders) {
					File f = new File(fname);
					scanFolder(f, 100);
				}
			}
			
		};
		t.start();
		
	}
	
	/**
	 * Recursively scan this folder
	 * @param dir
	 */
	private void scanFolder(File f, int max) {
		System.out.println("Scanning: "+f.getAbsolutePath());
		if (f.exists()) {
			if (f.isDirectory()) {
				for (File sub : f.listFiles(supportedTypesFilter)) {
					scanFolder(sub, max--);
				}
			} else {
			}
		}
		max--;
		if (--max == 0) System.exit(0);
	}
	
	public static CollectionManager getManager() {
		if (null == instance) {
			instance = new CollectionManager();
		}
		return instance;
	}
}	
