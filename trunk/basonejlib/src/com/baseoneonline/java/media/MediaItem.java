package com.baseoneonline.java.media;

import java.io.File;

public class MediaItem {

	public File file;

	public MediaItem(final File f) {
		this.file = f;
	}

	@Override
	public String toString() {
		return file.toString();
	}

}
