package com.baseoneonline.java.mediabrowser;

public class MediaFileType {

	private final String name;
	private final String[] extensions;

	public MediaFileType(final String name, final String[] extensions) {
		this.name = name;
		this.extensions = extensions;
	}

	public String getName() {
		return name;
	}

	public String[] getExtensions() {
		return extensions;
	}

}
