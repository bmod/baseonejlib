package com.baseoneonline.java.swing.config;

import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.baseoneonline.java.tools.NumberUtils;

public class Config
{

	private static Class<?> applicationClass;

	private static Config instance;
	private final Preferences prefs;

	private final List<PersistenceFactory> persistenceFactories = new ArrayList<PersistenceFactory>();
	private final HashMap<String, Object> persistentObjects = new HashMap<String, Object>();

	private Config()
	{
		if (null == applicationClass)
			Logger.getLogger(getClass().getName())
					.severe("Application class was not set! Use Config.setApplicationClass() to do so. Now using generic app class, may collide.");
		if (null != applicationClass)
		{
			prefs = Preferences.userNodeForPackage(applicationClass);
		} else
		{
			prefs = Preferences.userNodeForPackage(getClass());
		}

		addPersistenceFactory(new WindowPersistenceFactory());
		addPersistenceFactory(new SplitPanePersistenceFactory());
		addPersistenceFactory(new JTablePersistenceFactory());

	}

	/**
	 * Warning! Use this only if the component appears once in the application.
	 * 
	 * @param comp
	 */
	public void store(final Component comp)
	{
		store(comp.getClass().getName(), comp);
	}

	public void store(final String id, final Component comp)
	{
		getFactory(comp).store(this, id, comp);
	}

	/**
	 * Warning! Use this only if the component appears once in your application.
	 * 
	 * @param comp
	 */
	public void restore(final Component comp)
	{
		restore(comp.getClass().getName(), comp);
	}

	/**
	 * Restore a component's attributes
	 * 
	 * @param id
	 * @param comp
	 */
	public void restore(final String id, final Component comp)
	{
		getFactory(comp).restore(this, id, comp);
	}

	/**
	 * Add storage support for a specific type of object.
	 * 
	 * @param factory
	 */
	public void addPersistenceFactory(final PersistenceFactory factory)
	{
		persistenceFactories.add(factory);
	}

	public void flush()
	{
		try
		{
			storePersistentObjects();
			prefs.flush();
		} catch (final BackingStoreException e)
		{
			e.printStackTrace();
		}
	}

	private void storePersistentObjects()
	{
		for (final String key : persistentObjects.keySet())
		{
			final Object o = persistentObjects.get(key);
			final PersistenceFactory factory = getFactory(o);
			factory.store(this, key, o);
		}
	}

	public static void setApplicationClass(final Class<?> appClass)
	{
		if (null != applicationClass)
			throw new RuntimeException("Application class was already set!");
		applicationClass = appClass;
	}

	public static Config get()
	{

		if (null == instance)
			instance = new Config();
		return instance;
	}

	public File getFile(final String key)
	{
		final String filename = prefs.get(truncateKey(key), null);
		if (null == filename)
			return null;
		return new File(filename);
	}

	public File getFile(final String key, final File defaultValue)
	{
		return new File(prefs.get(truncateKey(key),
				defaultValue.getAbsolutePath()));
	}

	public void setFile(final String key, final File f)
	{
		prefs.put(truncateKey(key), f.getAbsolutePath());
	}

	private PersistenceFactory getFactory(final Object value)
	{
		for (final PersistenceFactory factory : persistenceFactories)
		{
			if (factory.getType().isInstance(value))
				return factory;
		}
		throw new UnsupportedOperationException(
				"No persistence factory found for class: " + value.getClass());
	}

	public void persist(final Object value)
	{
		persist(value.getClass().getName(), value);
	}

	public void persist(final String key, final Object value)
	{

		final PersistenceFactory factory = getFactory(value);
		factory.restore(this, key, value);
		persistentObjects.put(key, value);
	}

	public Rectangle getRectangle(final String key, final Rectangle defaultValue)
	{
		final Rectangle rect = new Rectangle();
		rect.x = prefs.getInt(truncateKey(key + "X"), defaultValue.x);
		rect.y = prefs.getInt(truncateKey(key + "Y"), defaultValue.y);
		rect.width = prefs.getInt(truncateKey(key + "W"), defaultValue.width);
		rect.height = prefs.getInt(truncateKey(key + "H"), defaultValue.height);
		return rect;
	}

	public void putRectangle(final String key, final Rectangle bounds)
	{
		prefs.putInt(truncateKey(key + "X"), bounds.x);
		prefs.putInt(truncateKey(key + "Y"), bounds.y);
		prefs.putInt(truncateKey(key + "W"), bounds.width);
		prefs.putInt(truncateKey(key + "H"), bounds.height);

	}

	public int getInt(final String key, final int defaultValue)
	{
		return prefs.getInt(truncateKey(key), defaultValue);
	}

	public int[] getIntArray(final String key, final int[] defaultValue)
	{
		byte[] bytes = new byte[0];
		if (null != defaultValue)
			bytes = NumberUtils.intsToBytes(defaultValue);
		return NumberUtils.bytesToInts(prefs.getByteArray(truncateKey(key),
				bytes));
	}

	public void putInt(final String key, final int value)
	{
		prefs.putInt(truncateKey(key), value);
	}

	public void putIntArray(final String key, final int[] columnWidths)
	{
		prefs.putByteArray(truncateKey(key),
				NumberUtils.intsToBytes(columnWidths));
	}

	private static String truncateKey(final String key)
	{
		if (key.length() > Preferences.MAX_KEY_LENGTH)
		{
			final int lastIndex = key.length() - 1;
			return key.substring(lastIndex - Preferences.MAX_KEY_LENGTH,
					lastIndex);
		}
		return key;
	}

	public String getString(final String key, final String defaultvalue)
	{
		return prefs.get(key, defaultvalue);
	}

	public void putString(final String key, final String value)
	{
		prefs.put(key, value);
	}
}
