package com.baseoneonline.java.test.xmlatts;

import com.baseoneonline.java.nanoxml.XMLElement;

public abstract class Attribute<T> {

	private String name;
	private T value;

	public Attribute(final String name, final T value) {
		this.name = name;
		this.value = value;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(final T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public XMLElement serialize() {
		final XMLElement xml = new XMLElement(name);
		xml.setContent(value.toString());
		return xml;
	}

	public abstract void deSerialize(final XMLElement xml);

}
