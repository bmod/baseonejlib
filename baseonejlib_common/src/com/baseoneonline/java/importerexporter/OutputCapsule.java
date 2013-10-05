package com.baseoneonline.java.importerexporter;

public interface OutputCapsule {

	public void writeFloat(String key, float value);

	public void writeDouble(String key, double value);

	public void writeString(String key, String value);

	public void writeStorable(String key, Storable value);

}
