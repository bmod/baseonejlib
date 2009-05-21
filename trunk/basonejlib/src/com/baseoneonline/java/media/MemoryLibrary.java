package com.baseoneonline.java.media;

import java.util.HashMap;

public class MemoryLibrary extends Library {

	private final HashMap<String, MediaItem> items = new HashMap<String, MediaItem>();

	@Override
	public void add(final MediaItem item) {
		items.put(item.getFile().getAbsolutePath(), item);
	}

	@Override
	public MediaItem get(int index) {
		return items.get(index);
	}

	@Override
	public int size() {
		return items.size();
	}
	
	
	



	
}
