package com.baseoneonline.java.importerexporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class Importer {

	public Storable load(File testFile) throws IOException {
		return load(new FileInputStream(testFile));
	}

	public abstract Storable load(InputStream is) throws IOException;

}
