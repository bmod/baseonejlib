package com.baseoneonline.java.importerexporter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLExporter extends Exporter {

	private final ClassRegister register;

	public XMLExporter(ClassRegister register) {
		this.register = register;
	}

	@Override
	public void write(OutputStream os, Storable storable) throws IOException {
		XMLElement xml = new XMLElement(register.getName(storable));
		XMLOutputCapsule cap = new XMLOutputCapsule(this, xml);

		storable.write(cap);

		Writer writer = new OutputStreamWriter(os);
		writer.write(xml.toString());
		writer.close();
	}

	public ClassRegister getRegister() {
		return register;
	}

}
