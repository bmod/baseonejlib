package com.baseoneonline.java.swing.config;

public interface PersistenceFactory<T>
{

	/**
	 * @param conf
	 *            A reference to the config, use this to store your values.
	 * @param key
	 *            An identifier for this specific storable object.
	 * @param storable
	 *            An object to be stored.
	 */
	public void store(Config conf, String key, T storable);

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

	public void restore(Config conf, String key, T storable);

	/**
	 * @return The type of storable object this factory supports.
	 */
	public Class<? extends T> getType();

}
