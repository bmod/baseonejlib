package com.baseoneonline.java.mediadb.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.baseoneonline.java.jlib.utils.StringUtils;


public class Conf {
	private static final Logger log = Logger.getLogger(Conf.class.getName());
	
	private static String filename;
	
	private static final String ARRAY_DELIMITER = ";";
	
	private static volatile Properties props = new Properties();
	
	public static int getInt(String key) {
		return Integer.parseInt(getString(key));
	}
	
	public static float getFloat(String key) {
		return Float.parseFloat(getString(key));
	}
	
	public static void setStringArray(String key, String[] value) {
		setString(key, StringUtils.join(value, ARRAY_DELIMITER));
	}
	
	public static String[] getStringArray(String key) {
		return getString(key).split(ARRAY_DELIMITER);
	}
	
	public static void setString(String key, String value) {
		props.put(key, value);
		write();
	}
	
	public static String getString(String key) {
		String re = props.getProperty(key);
		if (null == re) {
			throw new NullPointerException("Property '"+key+"' was not defined.");
		}
		return re;
	}
	
	public static void write() {
		try {
			props.load(new FileInputStream(new java.io.File(filename)));
		} catch (FileNotFoundException e) {
			log.warning("Failed to find "+filename);
		} catch (IOException e) {
			log.warning("Failed to write "+filename);
		}
	}
	
	public static void read(String fname) {
		filename = fname;
		try {
			props.load(new FileInputStream(new java.io.File(fname)));
		} catch (FileNotFoundException e) {
			log.warning("Failed to find "+fname);
		} catch (IOException e) {
			log.warning("Failed to load "+fname);
		}
		
	}

}
