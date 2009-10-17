package com.baseoneonline.java.test.media.library.items;

import java.io.File;


public class UnknownItem extends MediaItem {

	public UnknownItem(final File f) {
		super(f);
	}

	@Override
	public String getDisplayName() {
		return file.value.getName();
	}

	@Override
	public void readMetadata() {}

}
