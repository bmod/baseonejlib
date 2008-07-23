package com.baseoneonline.java.jlib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {
	Logger log = Logger.getLogger(getClass().getName());
	private static Configuration instance;

	private final Properties props = new Properties();

	private final String prefix;

	private final File file;

	private final boolean autoWrite = true;

	private Configuration(File f, String pfx) {
		file = f;
		prefix = pfx;
	}

	/**
	 * Attempt to read the configuration file.
	 */
	public void read() {
		try {
			props.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			log.warning("Configuration file not found, creating new one.");
			write();
		} catch (IOException e) {
			log.warning("Exception while reading file: " + file.getName());
		}
	}

	/**
	 * Attempt to write the configuration file.
	 */
	public void write() {
		try {
			props.store(new FileOutputStream(file), prefix + " Configuration");
		} catch (FileNotFoundException e) {
			log.warning("Configuration file not found: " + file.getName());
		} catch (IOException e) {
			log.warning("Exception while writing to file: " + file.getName());
		}
	}

	/**
	 * Put a property in the configuration
	 * 
	 * @param key
	 *            The key by which the value is retrieved.
	 * @param n
	 *            The value to set.
	 */
	public void setInt(String key, int n) {
		set(key, "" + n);
	}

	/**
	 * Retrieve a property from the configuration. If the key was not found,
	 * return the supplied default value and store that in the configuration.
	 * 
	 * @param key
	 *            The key by which the value is retrieved.
	 * @param n
	 *            The default value if no value was defined.
	 */
	public int getInt(String key, int n) {
		return Integer.parseInt(get(key, "" + n));
	}

	/**
	 * Put a property in the configuration
	 * 
	 * @param key
	 *            The key by which the value is retrieved.
	 * @param n
	 *            The value to set.
	 */
	public void setFloat(String key, float n) {
		set(key, "" + n);
	}

	/**
	 * Retrieve a property from the configuration. If the key was not found,
	 * return the supplied default value and store that in the configuration.
	 * 
	 * @param key
	 *            The key by which the value is retrieved.
	 * @param n
	 *            The default value if no value was defined.
	 */
	public Float getFloat(String key, float n) {
		return Float.parseFloat(get(key, "" + n));
	}

	/**
	 * Put a property in the configuration
	 * 
	 * @param key
	 *            The key by which the value is retrieved.
	 * @param n
	 *            The value to set.
	 */
	public void set(String key, String n) {
		props.setProperty(key, n);
		if (autoWrite) {
			write();
		}
	}

	/**
	 * Retrieve a property from the configuration. If the key was not found,
	 * return the supplied default value and store that in the configuration.
	 * 
	 * @param key
	 *            The key by which the value is retrieved.
	 * @param n
	 *            The default value if no value was defined.
	 */
	public String get(String key, String n) {
		String re = props.getProperty(key);
		if (null == re) {
			log.warning("Property " + key
					+ " not found in configuration, setting to default: " + n);
			set(key, n);
			return n;
		}
		return re;
	}

	public static Configuration getConfiguration(File f, String prefix) {
		if (null == instance)
			instance = new Configuration(f, prefix);
		return instance;
	}

	public static Configuration getConfiguration(File f) {
		return getConfiguration(f, "");
	}
}
