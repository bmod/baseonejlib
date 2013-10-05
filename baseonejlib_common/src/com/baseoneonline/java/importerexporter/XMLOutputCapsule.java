package com.baseoneonline.java.importerexporter;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLOutputCapsule implements OutputCapsule {

	private final XMLElement rootXML;
	private final XMLExporter exporter;

	public XMLOutputCapsule(XMLExporter exporter, XMLElement xml) {
		this.exporter = exporter;
		this.rootXML = xml;
	}

	public XMLElement getXml() {
		return rootXML;
	}

	@Override
	public void writeFloat(String key, float value) {
		rootXML.setAttribute(key, Float.toString(value));
	}

	@Override
	public void writeDouble(String key, double value) {
		rootXML.setAttribute(key, Double.toString(value));
	}

	@Override
	public void writeString(String key, String value) {
		rootXML.setAttribute(key, value);
	}

	@Override
	public void writeStorable(String key, Storable value) {
		XMLElement xChild = new XMLElement(exporter.getRegister()
				.getName(value));
		XMLOutputCapsule cap = new XMLOutputCapsule(exporter, xChild);
		value.write(cap);
		rootXML.addChild(xChild);
	}
}
