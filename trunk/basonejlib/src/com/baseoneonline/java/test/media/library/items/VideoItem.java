package com.baseoneonline.java.test.media.library.items;

import java.io.File;

public class VideoItem extends MediaItem {

	public VideoItem(final File f) {
		super(f);

	}

	@Override
	public String getDisplayName() {
		return file.value.getName();
	}

	@Override
	public void readMetadata() {
		title.value = file.value.getName().substring(0,
				file.value.getName().lastIndexOf("."));
	}

}
