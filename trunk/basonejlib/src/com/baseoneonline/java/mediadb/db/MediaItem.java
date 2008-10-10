package com.baseoneonline.java.mediadb.db;

import java.io.File;

public class MediaItem {
	
	protected File file;
	
	
	public MediaItem(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}
	
	@Override
	public String toString(){
		return file.toString();
	}
	
}
