package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

public class FileExtensionFilter extends javax.swing.filechooser.FileFilter
		implements FileFilter, Serializable {

	private boolean includeDirectories = true;
	String[] extensions;
	String description = "Files";

	public FileExtensionFilter(final String[] ext, final String description) {
		this(ext);
		this.description = description;
	}

	public FileExtensionFilter(final String[] ext) {
		extensions = new String[ext.length];
		for (int i = 0; i < ext.length; i++) {
			if (!ext[i].startsWith(".")) {
				extensions[i] = "." + ext[i];
			} else {
				extensions[i] = ext[i];
			}
		}
	}

	public FileExtensionFilter(final String extensionsString) {
		extensions = extensionsString.split("[,| |;]");
	}

	public FileExtensionFilter(final String extensionsString,
			final boolean includeDirs) {
		this(extensionsString);
		this.includeDirectories = includeDirs;
	}

	@Override
	public boolean accept(final File f) {
		if (f.isDirectory() && includeDirectories) return true;
		for (final String ext : extensions) {
			if (f.getName().toLowerCase().endsWith(ext)) return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description + " (" + StringUtils.join(extensions, ", ") + ")";
	}
}
