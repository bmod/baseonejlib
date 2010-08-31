package com.baseoneonline.java.mediabrowser;


public class MediaFile {
	
	final public String file;
	
	public MediaFile(final String file) {
		this.file = file;
	}
	
	@Override
	public String toString() {
		return file;
	}
	
}
