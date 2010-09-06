package com.baseoneonline.java.mediabrowser.core;

public class MediaFile implements MediaNode {

	final public String file;
	public long lastModified;
	public FileType type;

	public MediaFile(final String file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return file;
	}

}
