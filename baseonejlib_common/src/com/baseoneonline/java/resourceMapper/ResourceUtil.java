package com.baseoneonline.java.resourceMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResourceUtil
{
	public static List<ResourceNode> getChildren(Resource res)
	{
		List<ResourceNode> children = new ArrayList<ResourceNode>();
		if (res instanceof ListResource<?>)
		{
			ListResource<?> listRes = (ListResource<?>) res;
			for (IDResource child : listRes)

				children.add(new ResourceNode(child.id, child));
		} else if (res instanceof SetResource<?>) {

		} else
		{
			for (Field f : res.getClass().getFields())
			{
				if (Resource.class.isAssignableFrom(f.getType()))
				{
					try
					{
						children.add(new ResourceNode(f.getName(), (Resource) f
								.get(res)));
					} catch (Exception e)
					{
						throw new RuntimeException(e);
					}
				}
			}

		}
		return children;
	}

	public static List<ResourceProperty> getProperties(Resource res)
	{
		List<ResourceProperty> properties = new ArrayList<ResourceProperty>();

		for (Field f : res.getClass().getFields())
		{
			if (!Resource.class.isAssignableFrom(f.getType()))
			{
				try
				{
					properties.add(new ResourceProperty(res, f.getName(), f
							.get(res)));
				} catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		}

		return properties;
	}
}
