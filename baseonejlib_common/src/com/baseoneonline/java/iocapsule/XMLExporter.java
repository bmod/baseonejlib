package com.baseoneonline.java.iocapsule;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLExporter implements Exporter {

	@Override
	public void write(final OutputStream os, final Storable stor)
			throws IOException {
		final XMLOutputCapsule cap = new XMLOutputCapsule(os);
		stor.write(cap);
		os.flush();
		os.close();
	}
}

class XMLOutputCapsule implements OutputCapsule {

	private final XMLElement xml;

	public XMLOutputCapsule(final OutputStream os) {
		xml = new XMLElement();
	}

	@Override
	public void write(final String key, final List<? extends Storable> storables) {
		for (final Storable stor : storables) {
			final XMLElement x = new XMLElement(stor.getClass().getName());

			xml.addChild(x);
		}
	}

	@Override
	public void write(final String key, final String value) {
		// TODO Auto-generated method stub

	}

}
