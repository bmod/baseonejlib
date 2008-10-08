package com.baseoneonline.java.mediadb.db;

import java.io.File;
import java.util.logging.Logger;

import com.baseoneonline.java.mediadb.util.Conf;
import com.baseoneonline.java.mediadb.util.Const;



public class CollectionManager implements Const {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	
	private static CollectionManager instance;
	
	
	private CollectionManager() {
		
	}
	
	
	public void scanCollection() {
		final String[] mediaFolders = Conf.getStringArray(MEDIA_FOLDERS);
		Thread t = new Thread() {
			@Override
			public void run() {
				for (String fname : mediaFolders) {
					File f = new File(fname);
					if (f.exists()) {
						log.info("Scanning folder: "+f.getName());
					} else {
						log.warning("Folder does not exist: "+f.getAbsolutePath());
					}
				}
			}
			
		};
		t.start();
		
	}
	
	public static CollectionManager getManager() {
		if (null == instance) {
			instance = new CollectionManager();
		}
		return instance;
	}
}	
