package com.baseoneonline.java.swing.config;

import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class Config
{

	private static Class<?> applicationClass;

	private static Config instance;
	private final PrefsAdapter prefs;

	@SuppressWarnings("rawtypes")
	private final List<PersistenceFactory> persistenceFactories = new ArrayList<PersistenceFactory>();
	private final HashMap<String, Object> persistentObjects = new HashMap<String, Object>();

	private Config()
	{
		prefs = new PreferencesAdapter();
		prefs.setReferenceClass(applicationClass);

		addPersistenceFactory(new WindowPersistenceFactory());
		addPersistenceFactory(new SplitPanePersistenceFactory());
		addPersistenceFactory(new JTablePersistenceFactory());
		addPersistenceFactory(new JTreePersistenceFactory());
		addPersistenceFactory(new DockingDesktopPersistenceFactory());

	}

	/**
	 * Warning! Use this only if the component appears once in the application.
	 * 
	 * @param comp
	 */
	public void store(final Object comp)
	{
		store(comp.getClass().getName(), comp);
	}

	public void store(final String id, final Object comp)
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
	public void addPersistenceFactory(
			@SuppressWarnings("rawtypes") final PersistenceFactory factory)
	{
		persistenceFactories.add(factory);
	}

	public void flush()
	{
		try
		{
			storePersistentObjects();
			prefs.flush();
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	private void storePersistentObjects()
	{
		for (final String key : persistentObjects.keySet())
		{
			final Object o = persistentObjects.get(key);

			@SuppressWarnings("rawtypes")
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
		final String filename = prefs.get(key, null);
		if (null == filename)
			return null;
		return new File(filename);
	}

	public File getFile(final String key, final File defaultValue)
	{
		return new File(prefs.get(key, defaultValue.getAbsolutePath()));
	}

	public void setFile(final String key, final File f)
	{
		prefs.put(key, f.getAbsolutePath());
	}

	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings("rawtypes")
	public void persist(final String key, final Object value)
	{

		final PersistenceFactory factory = getFactory(value);
		factory.restore(this, key, value);
		persistentObjects.put(key, value);
	}

	public Rectangle getRectangle(final String key, final Rectangle defaultValue)
	{
		final Rectangle rect = new Rectangle();
		rect.x = prefs.getInt(key + "X", defaultValue.x);
		rect.y = prefs.getInt(key + "Y", defaultValue.y);
		rect.width = prefs.getInt(key + "W", defaultValue.width);
		rect.height = prefs.getInt(key + "H", defaultValue.height);
		return rect;
	}

	public void putRectangle(final String key, final Rectangle bounds)
	{
		prefs.putInt(key + "X", bounds.x);
		prefs.putInt(key + "Y", bounds.y);
		prefs.putInt(key + "W", bounds.width);
		prefs.putInt(key + "H", bounds.height);

	}

	public int getInt(final String key, final int defaultValue)
	{
		return prefs.getInt(key, defaultValue);
	}

	public int[] getIntArray(final String key, final int[] defaultValue)
	{
		return prefs.getIntArray(key, defaultValue);
	}

	public void putInt(final String key, final int value)
	{
		prefs.putInt(key, value);
	}

	public void putIntArray(final String key, final int[] columnWidths)
	{
		prefs.putIntArray(key, columnWidths);
	}

	public String getString(final String key, final String defaultvalue)
	{
		return prefs.get(key, defaultvalue);
	}

	public void putString(final String key, final String value)
	{
		prefs.put(key, value);
	}

	public void putBytes(String key, byte[] value)
	{
		prefs.putBytes(key, value);
	}

	public byte[] getBytes(String key, byte[] defaultValue)
	{
		return prefs.getBytes(key, defaultValue);
	}
}
