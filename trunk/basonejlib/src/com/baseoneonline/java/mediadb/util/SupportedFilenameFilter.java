package com.baseoneonline.java.mediadb.util;

import java.io.File;
import java.io.FileFilter;

public class SupportedFilenameFilter implements FileFilter{

	private final String[] supportedTypes = SupportedTypes.getAllSupportedExtensions();
	
	public boolean accept(File dir) {
		for (String t : supportedTypes) {
			if (dir.getName().endsWith(t)) {
				return true;
			}
		}
		return false;
	}


}
