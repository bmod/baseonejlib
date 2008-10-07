package com.baseoneonline.java.mediadb.db;

import java.util.ArrayList;

public class Collection {
	
	
	private final ArrayList<MediaItem> data = new ArrayList<MediaItem>();
	private static Collection instance;
	
	private Collection() {
		
	}
	
	public void add(MediaItem record) {
		for (MediaItem r : data) {
			if (r.filename == record.filename) {
				return;
			}
		}
		data.add(record);
	}

	public static Collection getInstance() {
		if (null == instance) instance = new Collection();
		return instance;
	}
	
	
}
