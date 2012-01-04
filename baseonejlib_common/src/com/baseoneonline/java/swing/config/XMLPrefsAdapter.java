package com.baseoneonline.java.swing.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.tools.StringUtils;

public class XMLPrefsAdapter implements PrefsAdapter {

	private XMLElement xml;
	private File file;

	@Override
	public void setReferenceClass(final Class<?> clazz) {
		final String userHome = System.getProperty("user.home");
		file = new File(userHome + "/" + clazz.getName() + ".conf");

		try {
			xml = new XMLElement();
			xml.parseFromReader(new FileReader(file));
		} catch (final Exception e) {
			Logger.getLogger(getClass().getName()).warning(
					"Could not read config file: " + file + "\n"
							+ e.getMessage());
			xml = new XMLElement("Config");
		}
	}

	@Override
	public void flush() throws Exception {
		System.out.println("Flushing");
		final FileWriter writer = new FileWriter(file);
		xml.write(writer);
		writer.flush();
		writer.close();
	}

	@Override
	public String get(final String key, final String defaultValue) {
		return xml.getStringAttribute(key, defaultValue);
	}

	@Override
	public void put(final String key, final String value) {
		xml.setAttribute(key, value);
	}

	@Override
	public int getInt(final String key, final int defaultValue) {
		return xml.getIntAttribute(key, defaultValue);
	}

	@Override
	public void putInt(final String key, final int value) {
		xml.setIntAttribute(key, value);
	}

	@Override
	public void putIntArray(final String key, final int[] value) {
		xml.setAttribute(key, StringUtils.join(value, ","));
	}

	@Override
	public int[] getIntArray(final String key, final int[] defaultValue) {
		final String value = xml.getStringAttribute(key, null);
		if (null == value)
			return defaultValue;

		final String[] values = value.split(",");

		final int[] ints = new int[values.length];
		for (int i = 0; i < ints.length; i++) {
			try {
				ints[i] = Integer.parseInt(values[i]);
			} catch (final Exception e) {
				Logger.getLogger(getClass().getName()).warning(
						"Could not convert value to int: " + values[i]);
			}
		}
		return ints;
	}

	@Override
	public void putBytes(final String key, final byte[] value) {
		put(key, Base64.encodeBase64String(value));
	}

	@Override
	public byte[] getBytes(final String key, final byte[] defaultValue) {
		final String data = get(key, null);
		if (null == data)
			return defaultValue;

		return Base64.decodeBase64(data);
	}
}
