package com.baseoneonline.java.iocapsule;

import java.io.InputStream;

public interface Importer {
	public void read(InputStream is, Storable stor);
}
