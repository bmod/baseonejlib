package com.baseoneonline.java.mediabrowser.core;

public class FileType {

	public String name;
	public String[] extensions = {};
	public int uid;

	public FileType() {

	}

	@Override
	public String toString() {
		return name;
	}

}
