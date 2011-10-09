package com.baseoneonline.java.swing.config;

public interface PersistenceFactory
{

	/**
	 * @param conf
	 *            A reference to the config, use this to store your values.
	 * @param key
	 *            An identifier for this specific storable object.
	 * @param storable
	 *            An object to be stored.
	 */
	public void store(Config conf, String key, Object storable);

	/**
	 * /**
	 * 
	 * @param conf
	 *            A reference to the config, use this restore your values from.
	 * @param key
	 *            An identifier for this specific storable object.
	 * @param storable
	 *            An object to be stored.
	 */

	public void restore(Config conf, String key, Object storable);

	/**
	 * @return The type of storable object this factory supports.
	 */
	public Class<?> getType();

}
