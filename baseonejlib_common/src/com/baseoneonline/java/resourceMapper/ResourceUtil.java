package com.baseoneonline.java.resourceMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResourceUtil
{
	public static List<String> getProperties(Resource parent)
	{
		List<String> list = new ArrayList<String>();

		for (Field f : parent.getClass().getFields())
		{
			if (!Resource.class.isAssignableFrom(f.getType()))
			{
				list.add(f.getName());
			}
		}
		return list;
	}

	public static List<String> getChildren(Resource parent)
	{
		List<String> list = new ArrayList<String>();

		for (Field f : parent.getClass().getFields())
		{
			if (Resource.class.isAssignableFrom(f.getType()))
			{
				list.add(f.getName());
			}
		}
		return list;
	}
}
