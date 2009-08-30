package com.baseoneonline.java.xmlatts;

import com.baseoneonline.java.nanoxml.XMLElement;

public class StringAtt extends Attribute<String> {

	public StringAtt(final String name, final String value) {
		super(name, value);
	}

	public StringAtt(final String name) {
		super(name, "");
	}

	@Override
	public void deSerialize(final XMLElement xml) {
		setValue(xml.getChild(getName()).getContent());
	}

}
