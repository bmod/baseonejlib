package com.baseoneonline.java.mediadb.db;

import java.io.File;

import com.baseoneonline.java.mediadb.util.Config;
import com.baseoneonline.java.mediadb.util.Const;



public class CollectionManager implements Const {
	
	private static CollectionManager instance;
	
	
	private CollectionManager() {
		
	}
	
	
	public void scanCollection() {
		final String[] mediaFolders = Config.getStringArray(MEDIA_FOLDERS);
		Thread t = new Thread() {
			@Override
			public void run() {
				for (String fname : mediaFolders) {
					File f = new File(fname);
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
