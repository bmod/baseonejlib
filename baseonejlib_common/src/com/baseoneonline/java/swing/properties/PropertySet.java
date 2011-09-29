package com.baseoneonline.java.swing.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PropertySet
{

	private final HashMap<String, List<Property<?>>> properties = new HashMap<String, List<Property<?>>>();

	public PropertySet()
	{

	}

	public void addProperty(String group, Property<?> prop)
	{
		List<Property<?>> list = properties.get(group);
		if (null == list)
		{
			list = new ArrayList<Property<?>>();
			properties.put(group, list);
		}

		list.add(prop);
	}

	public void addProperty(String group, String propertyName, int value)
	{
		addProperty(group, new IntProperty(propertyName, value));
	}

	public void addProperty(String group, String propertyName, String value)
	{
		addProperty(group, new StringProperty(propertyName, value));
	}

	public void addProperty(String group, String propertyName, float value)
	{
		addProperty(group, new FloatProperty(propertyName, value));
	}
}
