package com.baseoneonline.java.test.rombrowser;
import java.io.File;
import java.io.FileFilter;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.test.xmlatts.Attribute;
import com.baseoneonline.java.test.xmlatts.FileAtt;
import com.baseoneonline.java.test.xmlatts.StringArrayAtt;
import com.baseoneonline.java.test.xmlatts.StringAtt;

public class GameSystem {

	private final StringAtt title = new StringAtt("Title");
	private final FileAtt path = new FileAtt("Path");
	private final StringArrayAtt fileExtensions = new StringArrayAtt(
			"Extensions");
	private final StringAtt emulator = new StringAtt("Emulator");
	private final StringAtt parameters = new StringAtt("Parameters");

	private final Attribute<?>[] series = { title, path, emulator, parameters,
			fileExtensions };

	public String getTitle() {
		return title.getValue();
	}

	public void setTitle(final String title) {
		this.title.setValue(title);
	}

	public File getPath() {
		return path.getValue();
	}

	public void setPath(final File path) {
		this.path.setValue(path);
	}

	@Override
	public String toString() {
		return getTitle();
	}

	public FileFilter getFileFilter() {
		return fileFilter;
	}

	public void setParameters(final String parameters) {
		this.parameters.setValue(parameters);
	}

	public String getParameters() {
		return parameters.getValue();
	}

	public void setEmulator(final String emulator) {
		this.emulator.setValue(emulator);
	}

	public String getEmulator() {
		return emulator.getValue();
	}

	public XMLElement serialize() {
		final XMLElement xml = new XMLElement("GAMESYSTEM");
		for (final Attribute<?> at : series) {
			xml.addChild(at.serialize());
		}
		return xml;
	}

	public void deSerialize(final XMLElement xml) {
		for (final Attribute<?> at : series) {
			at.deSerialize(xml);
		}
	}

	private final FileFilter fileFilter = new FileFilter() {

		@Override
		public boolean accept(final File pathname) {
			if (pathname.isDirectory()) return false;
			for (final String e : fileExtensions.getValue()) {
				if (pathname.getName().toLowerCase().endsWith(e.toLowerCase())) return true;
			}
			return false;
		}
	};

}
