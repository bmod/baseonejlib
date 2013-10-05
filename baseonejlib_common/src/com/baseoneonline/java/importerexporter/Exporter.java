package com.baseoneonline.java.importerexporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Exporter {

	public void write(File file, Storable storable) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		write(fos, storable);
		fos.close();
	}

	public abstract void write(OutputStream os, Storable storable)
			throws IOException;
}
