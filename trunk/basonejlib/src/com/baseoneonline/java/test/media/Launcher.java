package com.baseoneonline.java.media;

import java.io.File;
import java.io.FileFilter;

public class Launcher {

	public String[] extensions;

	public FileFilter fileFilter = new FileFilter() {

		@Override
		public boolean accept(final File file) {
			if (file.isDirectory()) return true;
			for (final String ext : extensions) {
				if (file.getName().toLowerCase().endsWith(ext)) { return true; }
			}
			return false;
		}
	};

}
