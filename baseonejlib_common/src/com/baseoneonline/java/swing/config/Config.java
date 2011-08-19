package com.baseoneonline.java.swing.config;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JSplitPane;

public class Config
{

	private static Class<?> applicationClass;

	private static Config instance;
	private final Preferences prefs;

	private final List<PersistenceFactory> persistenceFactories = new ArrayList<PersistenceFactory>();
	private final HashMap<String, Object> persistentObjects = new HashMap<String, Object>();

	private static final Rectangle DEFAULT_RECT = new Rectangle(10, 10, 500,
			500);

	private Config()
	{
		if (null == applicationClass)
			Logger.getLogger(getClass().getName())
					.warning(
							"Application class was not set! Use Config.setApplicationClass() to do so. Now using generic app class, may collide.");
		if (null != applicationClass)
		{
			prefs = Preferences.userNodeForPackage(applicationClass);
		} else
		{
			prefs = Preferences.userNodeForPackage(getClass());
		}

	}

	public void store(String id, Component comp)
	{
		if (comp instanceof Window)
		{
			store(id + "Bounds", ((Window) comp).getBounds());

		} else if (comp instanceof JSplitPane)
		{
			prefs.putInt(id + "DividerLocation",
					((JSplitPane) comp).getDividerLocation());
		} else
		{
			throw new UnsupportedOperationException(
					"Storage of type not supported: " + comp.getClass());
		}
	}

	public void restore(String id, Component comp)
	{
		if (comp instanceof Window)
		{
			Rectangle rect = new Rectangle();
			restore(id + "Bounds", rect);
			((Window) comp).setBounds(rect);

		} else if (comp instanceof JSplitPane)
		{
			((JSplitPane) comp).setDividerLocation(prefs.getInt(id
					+ "DividerLocation", -1));
		} else
		{
			throw new UnsupportedOperationException(
					"Storage of type not supported: " + comp.getClass());
		}
	}

	public void addPersistenceFactory(PersistenceFactory factory)
	{
		persistenceFactories.add(factory);
	}

	private void store(String id, Rectangle rect)
	{
		prefs.putInt(id + "X", rect.x);
		prefs.putInt(id + "Y", rect.y);
		prefs.putInt(id + "W", rect.width);
		prefs.putInt(id + "H", rect.height);
	}

	private void restore(String id, Rectangle rect)
	{
		rect.x = prefs.getInt(id + "X", DEFAULT_RECT.x);
		rect.y = prefs.getInt(id + "Y", DEFAULT_RECT.y);
		rect.width = prefs.getInt(id + "W", DEFAULT_RECT.width);
		rect.height = prefs.getInt(id + "H", DEFAULT_RECT.height);
	}

	public void flush()
	{
		try
		{
			storePersistentObjects();
			prefs.flush();
		} catch (BackingStoreException e)
		{
			e.printStackTrace();
		}
	}

	private void storePersistentObjects()
	{
		for (String key : persistentObjects.keySet())
		{
			Object o = persistentObjects.get(key);
			PersistenceFactory factory = getFactory(o);
			factory.store(this, key, o);
		}
	}

	public static void setApplicationClass(Class<?> appClass)
	{
		applicationClass = appClass;
	}

	public static Config get()
	{

		if (null == instance)
			instance = new Config();
		return instance;
	}

	public File getFile(String key)
	{
		String filename = prefs.get(key, null);
		if (null == filename)
			return null;
		return new File(filename);
	}

	public File getFile(String key, File defaultValue)
	{
		return new File(prefs.get(key, defaultValue.getAbsolutePath()));
	}

	public void setFile(String key, File f)
	{
		prefs.put(key, f.getAbsolutePath());
	}

	private PersistenceFactory getFactory(Object value)
	{
		for (PersistenceFactory factory : persistenceFactories)
		{
			if (factory.getType().isInstance(value))
				return factory;
		}
		throw new UnsupportedOperationException(
				"No persistence factory found for class: " + value.getClass());
	}

	public void persist(String key, Object value)
	{

		PersistenceFactory factory = getFactory(value);
		factory.restore(this, key, value);
		persistentObjects.put(key, value);
	}

	public Rectangle getRectangle(String key, Rectangle defaultValue)
	{
		Rectangle rect = new Rectangle();
		rect.x = prefs.getInt(key + "X", defaultValue.x);
		rect.y = prefs.getInt(key + "Y", defaultValue.y);
		rect.width = prefs.getInt(key + "Width", defaultValue.width);
		rect.height = prefs.getInt(key + "Height", defaultValue.height);
		return rect;
	}

	public void putRectangle(String key, Rectangle bounds)
	{
		prefs.putInt(key + "X", bounds.x);
		prefs.putInt(key + "Y", bounds.y);
		prefs.putInt(key + "Width", bounds.width);
		prefs.putInt(key + "Height", bounds.height);
	}

	public int getInt(String key, int defaultValue)
	{
		return prefs.getInt(key, defaultValue);
	}

	public void putInt(String key, int value)
	{
		prefs.putInt(key, value);
	}
}
