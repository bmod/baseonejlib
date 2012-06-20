package com.baseoneonline.java.importerexporter;

import java.util.List;

public interface Capsule
{

	public <T extends Storable> List<T> read(String string, Class<T> elementType);

	public void write(String string, List<? extends Storable> list);

	public String read(String key, String defaultValue);

	public void write(String key, String value);

	public Storable read(String key, Storable defaultValue);

	public void write(String key, Storable stor);

	public boolean read(String key, boolean defaultValue);

	public void write(String key, boolean value);

}
