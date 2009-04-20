package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;

public class FileExensionFilter implements FileFilter {
	
	String[] extensions;
	
	public FileExensionFilter(String[] ext) {
		extensions = ext;
	}
	
	@Override
	public boolean accept(File f) {
		for (String ext : extensions) {
			if (f.getName().endsWith("."+ext)) return true;
		}
		return false;
	}
}
