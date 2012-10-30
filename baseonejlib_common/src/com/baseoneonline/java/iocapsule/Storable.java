package com.baseoneonline.java.iocapsule;

public interface Storable {
	public void write(OutputCapsule out);

	public void read(InputCapsule in);
}
