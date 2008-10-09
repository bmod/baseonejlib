package com.baseoneonline.java.mediadb.util;

import java.io.File;
import java.io.FilenameFilter;

public class SupportedFilenameFilter implements FilenameFilter{

	private final String[] supportedTypes = SupportedTypes.getAllSupportedExtensions();
	
	public boolean accept(File dir, String name) {
		for (String t : supportedTypes) {
			if (name.endsWith(t)) {
				return true;
			}
		}
		return false;
	}

}
