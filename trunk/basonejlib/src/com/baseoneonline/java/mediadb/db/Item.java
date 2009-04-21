package com.baseoneonline.java.mediadb.db;

import java.io.File;

public class Item {
	
	protected File file;
	
	
	public Item(File file) {
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
