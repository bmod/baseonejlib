package com.baseoneonline.java.media.library;

import com.baseoneonline.java.media.library.items.MediaItem;

public interface LibraryView {

	MediaItem get(int index);

	int size();
	
}
