package com.baseoneonline.java.test.media.library;

import com.baseoneonline.java.test.media.library.items.MediaItem;


public interface LibraryView {

	MediaItem get(int index);

	int size();
	
}
