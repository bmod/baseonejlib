package com.baseoneonline.java.resourceMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Resource
{

	private List<ResourceNode> children;
	private List<ResourceProperty> properties;

	public List<ResourceNode> getChildren()
	{
		if (null == children)
		{
			children = new ArrayList<ResourceNode>();

			for (Field f : getClass().getFields())
			{
				if (Resource.class.isAssignableFrom(f.getType()))
				{
					try
					{
						children.add(new ResourceNode(f.getName(), (Resource) f
								.get(this)));
					} catch (Exception e)
					{
						throw new RuntimeException(e);
					}
				}
			}

		}
		return children;
	}

	public List<ResourceProperty> getProperties()
	{
		if (null == properties)
		{
			properties = new ArrayList<ResourceProperty>();

			for (Field f : getClass().getFields())
			{
				if (!Resource.class.isAssignableFrom(f.getType()))
				{
					try
					{
						properties.add(new ResourceProperty(f.getName(), f
								.get(this)));
					} catch (Exception e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		}

		return properties;
	}

}
