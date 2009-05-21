package com.baseoneonline.java.media;

import java.io.File;

public class MediaItem {

	private final File file;
	private final long mtime;

	public MediaItem(final File f) {
		this.file = f;
		mtime = f.lastModified();
	}

	@Override
	public String toString() {
		return file.toString();
	}

	public long getMtime() {
		return mtime;
	}
	
	public File getFile() {
		return file;
	}

}
