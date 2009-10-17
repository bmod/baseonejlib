package com.baseoneonline.java.xmlatts;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.tools.StringUtils;

public class StringArrayAtt extends Attribute<String[]> {

	public StringArrayAtt(final String name, final String[] value) {
		super(name, value);
	}

	public StringArrayAtt(final String name) {
		super(name, new String[0]);
	}

	@Override
	public void deSerialize(final XMLElement xml) {
		setValue(xml.getChild(getName()).getContent().split("[,| |;]"));
	}

	@Override
	public XMLElement serialize() {
		final XMLElement xml = new XMLElement(getName());
		xml.setContent(StringUtils.join(getValue(), ","));
		return xml;
	}

}
