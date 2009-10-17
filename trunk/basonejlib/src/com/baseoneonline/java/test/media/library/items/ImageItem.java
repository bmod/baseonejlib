package com.baseoneonline.java.test.media.library.items;

import java.io.File;

public class ImageItem extends MediaItem {

	public ImageItem(final File f) {
		super(f);
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public void readMetadata() {
		title.value = file.value.getName().substring(0,
				file.value.getName().lastIndexOf("."));
	}

}
