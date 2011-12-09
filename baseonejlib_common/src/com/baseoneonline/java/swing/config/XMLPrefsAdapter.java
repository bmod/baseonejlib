package com.baseoneonline.java.swing.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.tools.StringUtils;

public class XMLPrefsAdapter implements PrefsAdapter
{

	private XMLElement xml;
	private File file;

	@Override
	public void setReferenceClass(Class<?> clazz)
	{
		String userHome = System.getProperty("user.home");
		file = new File(userHome + "/" + clazz.getName() + ".conf");
		xml = new XMLElement();
		try
		{
			xml.parseFromReader(new FileReader(file));
		} catch (Exception e)
		{
			Logger.getLogger(getClass().getName()).warning(
					"Could not read config file: " + file);
		}
	}

	@Override
	public void flush() throws Exception
	{
		xml.write(new FileWriter(file));
	}

	@Override
	public String get(String key, String defaultValue)
	{
		return xml.getStringAttribute(key, defaultValue);
	}

	@Override
	public void put(String key, String value)
	{
		xml.setAttribute(key, value);
	}

	@Override
	public int getInt(String key, int defaultValue)
	{
		return xml.getIntAttribute(key, defaultValue);
	}

	@Override
	public void putInt(String key, int value)
	{
		xml.setIntAttribute(key, value);
	}

	@Override
	public void putIntArray(String key, int[] value)
	{
		xml.setAttribute(key, StringUtils.join(value, ","));
	}

	@Override
	public int[] getIntArray(String key, int[] defaultValue)
	{
		String value = xml.getStringAttribute(key, null);
		if (null == value)
			return defaultValue;

		String[] values = value.split(",");

		int[] ints = new int[values.length];
		for (int i = 0; i < ints.length; i++)
		{
			try
			{
				ints[i] = Integer.parseInt(values[i]);
			} catch (Exception e)
			{
				Logger.getLogger(getClass().getName()).warning(
						"Could not convert value to int: " + values[i]);
			}
		}
		return ints;
	}

	@Override
	public void putBytes(String key, byte[] value)
	{
		put(key, Base64.encodeBase64String(value));
	}

	@Override
	public byte[] getBytes(String key, byte[] defaultValue)
	{
		String data = get(key, null);
		if (null == data)
			return defaultValue;

		return Base64.decodeBase64(data);
	}
}
