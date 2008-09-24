package com.baseoneonline.java.mediadb.db;

public class DatabaseRecord {
	
	private final String filename;
	
	public DatabaseRecord(String filename) {
		this.filename = filename;
	}
	
	@Override
	public String toString(){
		return filename;
	}
	
}
