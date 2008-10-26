package com.baseoneonline.java.jlib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;



public class Config {
	Logger log = Logger.getLogger(getClass().getName());


	private static HashMap<String, Config> instanceMap = new HashMap<String, Config>();

	private static Config instance;

	private final Properties props = new Properties();

	private final String arrayDelimiter = ",";

	private File file;

	private final boolean autoWrite = true;

	private Config() {
	}

	public void setFile(final String filename) {
		file = new File(filename);
	}

	/**
	 * Attempt to read the configuration file.
	 */
	public void read() {
		try {
			props.load(new FileInputStream(file));
		} catch (final FileNotFoundException e) {
			log.warning("Configuration file not found, creating new one.");
			write();
		} catch (final IOException e) {
			log.warning("Exception while reading file: " + file.getName());
		}
	}

	/**
	 * Attempt to write the configuration file.
	 */
	public void write() {
		try {
			props.store(new FileOutputStream(file), "Configuration");
		} catch (final FileNotFoundException e) {
			log.warning("Configuration file not found: " + file.getName());
		} catch (final IOException e) {
			log.warning("Exception while writing to file: " + file.getName());
		}
	}

	public void set(final String key, final String[] v) {
		set(key, StringUtils.join(v, arrayDelimiter));
	}

	public String[] get(final String key, final String[] defaultArray) {
		return get(key, StringUtils.join(defaultArray, arrayDelimiter)).split(arrayDelimiter);
	}

	public String get(final String key) {
		return get(key, "");
	}

	/**
	 * Put a property in the configuration
	 *
	 * @param key
	 *            The key by which the value is retrieved.
	 * @param n
	 *            The value to set.
	 */
	public void set(final String key, final int n) {
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
	public int get(final String key, final int n) {
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
	public void set(final String key, final float n) {
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
	public Float get(final String key, final float n) {
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
	public void set(final String key, final String n) {
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
	public String get(final String key, final String n) {
		final String re = props.getProperty(key);
		if (null == re) {
			log.warning("Property " + key
					+ " not found in configuration, setting to default: " + n);
			set(key, n);
			return n;
		}
		return re;
	}

	public static Config getConfig() {
		if (null == instance) {
			instance = new Config();
		}
		return instance;
	}

	public static Config getConfig(final String filename) {
		Config inst = instanceMap.get(filename);
		if (null == inst) {
			inst = new Config();
			inst.setFile(filename);
			inst.read();
			instanceMap.put(filename, inst);
		}
		return inst;
	}

}
