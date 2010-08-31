package com.baseoneonline.java.mediabrowser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Settings {

	private static Settings instance;

	private File[] mediaSources;
	private MediaFileType[] types;

	private Settings() {
		try {

			final XMLElement xml = new XMLElement();
			xml.parseFromReader(new FileReader("settings.xml"));

			mediaSources = parseMediaSources(xml);
			types = parseMediaFileTypes(xml);

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public File[] getMediaSources() {
		return mediaSources;
	}

	public MediaFileType[] getMediaFileTypes() {
		return types;
	}

	private File[] parseMediaSources(final XMLElement xml) {
		// Get directories to scan
		final ArrayList<File> dirs = new ArrayList<File>();
		for (final XMLElement xDir : xml.getChild("MEDIASOURCES").getChildren(
				"DIR")) {

			final File dir = new File(xDir.getStringAttribute("PATH"));

			if (dir.exists()) {
				dirs.add(dir);
			} else {
				Logger.getLogger(getClass().getName()).warning(
						"Media path not found: " + dir.getAbsolutePath());
			}

		}
		if (dirs.size() < 1)
			Logger.getLogger(getClass().getName()).warning(
					"No media dirs to scan!");
		return dirs.toArray(new File[dirs.size()]);
	}

	private MediaFileType[] parseMediaFileTypes(final XMLElement xml) {
		final ArrayList<MediaFileType> types = new ArrayList<MediaFileType>();
		for (final XMLElement xType : xml.getChild("FILETYPES").getChildren(
				"TYPE")) {
			final String name = xType.getStringAttribute("NAME");
			final String exts = xType.getStringAttribute("EXTENSIONS");
			final String[] extensions = trim(exts.split(","));
			final MediaFileType type = new MediaFileType(name, extensions);
			types.add(type);
		}
		return types.toArray(new MediaFileType[types.size()]);
	}

	private static String[] trim(final String[] a) {
		final String[] b = new String[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = a[i].trim();
		}
		return b;
	}

	public static Settings get() {
		if (null == instance)
			instance = new Settings();
		return instance;
	}

}
