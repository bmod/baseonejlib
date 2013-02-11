package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;

public class FileExensionFilter extends javax.swing.filechooser.FileFilter
		implements FileFilter {

	String[] extensions;
	String description = "Files";

	public FileExensionFilter(final String[] ext, final String description) {
		this(ext);
		this.description = description;
	}

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

	public FileExensionFilter(String ext) {
		this(new String[] { ext });
	}

	@Override
	public boolean accept(final File f) {
		if (f.isDirectory())
			return true;
		for (final String ext : extensions) {
			if (f.getName().toLowerCase().endsWith(ext))
				return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description + " (" + StringUtils.join(extensions, ", ") + ")";
	}
}
