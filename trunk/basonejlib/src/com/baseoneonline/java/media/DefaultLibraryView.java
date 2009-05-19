package com.baseoneonline.java.media;

import java.util.ArrayList;
import java.util.List;

public class DefaultLibraryView implements LibraryView {

	Library lib;
	List<MediaItem> items;

	public DefaultLibraryView(Library lib) {
		this.lib = lib;
		refresh();
	}
	
	private void refresh() {
		items = new ArrayList<MediaItem>();
	}

	@Override
	public MediaItem get(int index) {
		return lib.get(index);
	}

	@Override
	public int size() {
		return lib.size();
	}

}
