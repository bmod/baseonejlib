package com.baseoneonline.java.swing.config;

public interface PrefsAdapter
{

	public void setReferenceClass(Class<?> clazz);

	public void flush() throws Exception;

	public String get(String key, String defaultValue);

	public void put(String key, String value);

	public int getInt(String key, int defaultValue);

	public void putInt(String key, int value);

	public void putIntArray(String key, int[] value);

	public int[] getIntArray(String key, int[] defaultValue);

}
