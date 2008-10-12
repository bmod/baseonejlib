package com.baseoneonline.java.mediadb.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.baseoneonline.java.jlib.utils.StringUtils;


public class Conf {
	private static final Logger log = Logger.getLogger(Conf.class.getName());

	private static String filename;

	private static final String ARRAY_DELIMITER = ";";

	private static volatile Properties props = new Properties();

	public static int getInt(final String key) {
		return Integer.parseInt(getString(key));
	}

	public static float getFloat(final String key) {
		return Float.parseFloat(getString(key));
	}

	public static void setStringArray(final String key, final String[] value) {
		setString(key, StringUtils.join(value, ARRAY_DELIMITER));
	}

	public static String[] getStringArray(final String key) {
		return getString(key).split(ARRAY_DELIMITER);
	}

	public static void setString(final String key, final String value) {
		props.put(key, value);
		write();
	}

	public static String getString(final String key) {
		final String re = props.getProperty(key);
		if (null == re) {
			throw new NullPointerException("Property '"+key+"' was not defined.");
		}
		return re;
	}

	public static void write() {
		try {
			props.store(new FileOutputStream(new java.io.File(filename)), "MediaDB Properties");
		} catch (final FileNotFoundException e) {
			log.warning("Failed to find "+filename);
		} catch (final IOException e) {
			log.warning("Failed to write "+filename);
		}
	}

	public static void read(final String fname) {
		filename = fname;
		try {
			props.load(new FileInputStream(new java.io.File(fname)));
		} catch (final FileNotFoundException e) {
			log.warning("Failed to find "+fname);
		} catch (final IOException e) {
			log.warning("Failed to load "+fname);
		}

	}

}
