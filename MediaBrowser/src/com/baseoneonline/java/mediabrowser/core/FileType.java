package com.baseoneonline.java.mediabrowser.core;

public class FileType {

	private String name;
	private String[] extensions;

	public FileType(final String name, final String[] extensions) {
		this.name = name;
		this.extensions = extensions;
	}

	public String getName() {
		return name;
	}

	public String[] getExtensions() {
		return extensions;
	}

	@Override
	public String toString() {
		return getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExtensions(String[] extensions) {
		this.extensions = extensions;
	}

}
