package com.baseoneonline.java.iocapsule;

import java.io.IOException;
import java.io.OutputStream;

public interface Exporter {
	public void write(OutputStream os, Storable stor) throws IOException;
}
