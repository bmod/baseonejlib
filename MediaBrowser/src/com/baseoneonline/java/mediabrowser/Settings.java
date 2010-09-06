package com.baseoneonline.java.mediabrowser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.Timer;

import com.baseoneonline.java.mediabrowser.core.FileType;
import com.baseoneonline.java.mediabrowser.util.Util;
import com.baseoneonline.java.mediabrowser.util.XMLElement;

public class Settings {

	private final String settingsFile = "settings.xml";

	private static Settings instance;

	private File[] mediaSources;
	private FileType[] fileTypes;

	private final int flushDelay = 200;

	private final Timer timer = new Timer(flushDelay, new ActionListener() {

		@Override
		public void actionPerformed(final ActionEvent e) {
			flush();
		}
	});

	private static String SETTINGS = "settings", MEDIASOURCES = "mediasources",
			DIR = "dir", PATH = "path", FILETYPES = "filetypes", TYPE = "type",
			NAME = "name", EXTENSIONS = "extensions", SEPARATOR = ",", UID = "uid";

	private Settings() {
		timer.setRepeats(false);
		try {

			final XMLElement xml = new XMLElement();
			xml.parseFromReader(new FileReader(settingsFile));

			mediaSources = deserializeMediaSources(xml);
			fileTypes = deserializeFileTypes(xml);

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setMediaSources(final File[] mediaSources) {
		this.mediaSources = mediaSources;
		flushDelayed();
	}

	public File[] getMediaSources() {
		return mediaSources;
	}

	public void setFileTypes(final FileType[] fileTypes) {
		this.fileTypes = fileTypes;
		flushDelayed();
	}

	public FileType[] getMediaFileTypes() {
		return fileTypes;
	}

	/* SERIALIZE / DESERIALIZE */

	private File[] deserializeMediaSources(final XMLElement xml) {
		// Get directories to scan
		final ArrayList<File> dirs = new ArrayList<File>();
		for (final XMLElement xDir : xml.getChild(MEDIASOURCES)
				.getChildren(DIR)) {

			final File dir = new File(xDir.getStringAttribute(PATH));

			dirs.add(dir);

		}
		return dirs.toArray(new File[dirs.size()]);
	}

	private XMLElement serializeMediaSources(final File[] mediaSources) {
		final XMLElement xml = new XMLElement(MEDIASOURCES);
		for (final File f : mediaSources) {
			final XMLElement xDir = new XMLElement(DIR);
			xDir.setAttribute(PATH, f.getAbsolutePath());
			xml.addChild(xDir);
		}
		return xml;
	}

	private FileType[] deserializeFileTypes(final XMLElement xml) {
		final ArrayList<FileType> types = new ArrayList<FileType>();
		
		
		
		for (final XMLElement xType : xml.getChild(FILETYPES).getChildren(TYPE)) {
			
			final FileType type = new FileType();
			
			type.name = xType.getStringAttribute(NAME);
			type.extensions =
					Util.split(xType.getStringAttribute(EXTENSIONS), SEPARATOR);
			
			types.add(type);
		}
		return types.toArray(new FileType[types.size()]);
	}

	private XMLElement serializeFileTypes(final FileType[] types) {
		final XMLElement xml = new XMLElement(FILETYPES);
		for (final FileType type : types) {
			final XMLElement xType = new XMLElement(TYPE);
			xType.setAttribute(NAME, type.name);
			xType.setAttribute(EXTENSIONS,
					Util.join(type.extensions, SEPARATOR));
			xType.setAttribute(UID, type.uid);
			xml.addChild(xType);
		}
		return xml;
	}

	private void flushDelayed() {
		timer.restart();
	}

	public void flush() {

		final XMLElement xml = new XMLElement(SETTINGS);

		xml.addChild(serializeMediaSources(mediaSources));
		xml.addChild(serializeFileTypes(fileTypes));

		try {
			final FileWriter writer = new FileWriter(settingsFile);
			xml.write(writer);
			writer.flush();
			writer.close();
			Logger.getLogger(getClass().getName()).info("Settings written.");
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Settings get() {
		if (null == instance)
			instance = new Settings();
		return instance;
	}

}
