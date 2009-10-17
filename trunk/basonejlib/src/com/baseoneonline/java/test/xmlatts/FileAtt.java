package com.baseoneonline.java.xmlatts;

import java.io.File;

import com.baseoneonline.java.nanoxml.XMLElement;

public class FileAtt extends Attribute<File> {

	public FileAtt(final String name, final File value) {
		super(name, value);
	}

	public FileAtt(final String name) {
		super(name, new File("."));
	}

	@Override
	public void deSerialize(final XMLElement xml) {
		setValue(new File(xml.getChild(getName()).getContent()));
	}
}
