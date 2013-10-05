package com.baseoneonline.java.importerexporter;

public interface Storable {
	public void read(InputCapsule cap);

	public void write(OutputCapsule cap);
}
