package com.baseoneonline.java.mediadb.db;

import java.util.ArrayList;
import java.util.List;

public class MemoryDatabase extends AbstractDatabase {

	private final List<MediaItem> data = new ArrayList<MediaItem>();
	
	public MemoryDatabase() {
		
	}
	
	
	@Override
	public void addItem(MediaItem item) {
		data.add(item);
	}

	@Override
	public boolean contains(MediaItem item) {
		return data.contains(item); 
	}

	@Override
	public MediaItem getItem(int index) {
		return data.get(index);
	}

	@Override
	public void loadDatabase() {
		// Don't!
	}

	@Override
	public void removeItem(MediaItem item) {
		data.remove(item);
	}

	@Override
	public void storeDatabase() {
		// Don't!
	}

}
