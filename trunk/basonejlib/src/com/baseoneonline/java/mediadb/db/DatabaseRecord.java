package com.baseoneonline.java.mediadb.db;

public class DatabaseRecord {
	
	public static final int TYPE_MUSIC = 0;
	public static final int TYPE_MOVIE = 1;
	public static final int TYPE_IMAGE = 2;
	
	
	public String filename;
	
	public int type;
	
	public DatabaseRecord(String filename, int type) {
		this.filename = filename;
		this.type = type;
	}
	
	@Override
	public String toString(){
		return filename;
	}
	
}
