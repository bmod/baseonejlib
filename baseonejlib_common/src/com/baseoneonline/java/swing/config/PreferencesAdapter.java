package com.baseoneonline.java.swing.config;

import java.util.logging.Logger;
import java.util.prefs.Preferences;

import com.baseoneonline.java.tools.NumberUtils;

public class PreferencesAdapter implements PrefsAdapter
{
	private Preferences prefs;

	@Override
	public void setReferenceClass(final Class<?> applicationClass)
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
	}

	@Override
	public void flush() throws Exception
	{
		prefs.flush();
	}

	@Override
	public String get(final String key, final String defaultValue)
	{
		return prefs.get(truncateKey(key), defaultValue);
	}

	@Override
	public void put(final String key, final String value)
	{
		prefs.put(key, value);
	}

	@Override
	public int getInt(final String key, final int defaultValue)
	{
		return prefs.getInt(key, defaultValue);
	}

	@Override
	public void putInt(final String key, final int value)
	{
		prefs.putInt(truncateKey(key), value);
	}

	@Override
	public void putIntArray(final String key, final int[] value)
	{
		prefs.putByteArray(truncateKey(key), NumberUtils.intsToBytes(value));
	}

	@Override
	public int[] getIntArray(final String key, final int[] defaultValue)
	{
		byte[] bytes = new byte[0];
		if (null != defaultValue)
			bytes = NumberUtils.intsToBytes(defaultValue);
		return NumberUtils.bytesToInts(prefs.getByteArray(truncateKey(key),
				bytes));
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

	@Override
	public void putBytes(final String key, final byte[] value)
	{
		prefs.putByteArray(key, value);
	}

	@Override
	public byte[] getBytes(final String key, final byte[] defaultValue)
	{
		return prefs.getByteArray(key, defaultValue);
	}
}
