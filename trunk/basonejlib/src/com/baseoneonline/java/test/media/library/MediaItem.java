package com.baseoneonline.java.media.library;

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
