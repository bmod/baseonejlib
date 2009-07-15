package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;

public class FileExensionFilter implements FileFilter {

	String[] extensions;

	public FileExensionFilter(final String[] ext) {
		extensions = new String[ext.length];
		for (int i = 0; i < ext.length; i++) {
			if (!ext[i].startsWith(".")) {
				extensions[i] = "." + ext[i];
			} else {
				extensions[i] = ext[i];
			}
		}
	}

	@Override
	public boolean accept(final File f) {
		if (f.isDirectory()) return true;
		for (final String ext : extensions) {
			if (f.getName().toLowerCase().endsWith(ext)) return true;
		}
		return false;
	}
}
