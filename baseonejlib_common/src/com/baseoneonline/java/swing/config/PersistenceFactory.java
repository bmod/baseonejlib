package com.baseoneonline.java.swing.config;

public interface PersistenceFactory
{

	public void store(Config conf, String key, Object value);

	public void restore(Config conf, String key, Object value);

	public Class<?> getType();

}
