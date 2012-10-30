package com.baseoneonline.java.iocapsule;

import java.util.List;

public interface OutputCapsule {

	public void write(String key, List<? extends Storable> mediaSources);

	public void write(String key, String value);

}
