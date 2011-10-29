package com.baseoneonline.java.resourceMapper;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ResourceMapper
{

	private final ResourceTree resTree;

	private final Set<Package> resourcePackages = new HashSet<Package>();

	public ResourceMapper(final ResourceTree resTree)
	{
		this.resTree = resTree;
	}

	public void addResourcePackage(final Package pack)
	{
		resourcePackages.add(pack);
	}

	public <E extends Resource> E decode(final Class<E> type) throws Exception
	{
		addResourcePackage(type.getPackage());
		return decode(resTree.getRoot());
	}

	private <E extends Resource> E decode(final Object node) throws Exception
	{
		final String typeName = resTree.getName(node);

		@SuppressWarnings("unchecked")
		final E resource = (E) createInstance(typeName);

		decodeAttributes(resource, node);

		return resource;
	}

	private void decodeAttributes(final Resource resource, final Object node)
			throws Exception
	{
		for (final Field field : resource.getClass().getFields())
		{
			final Class<?> attributeType = field.getType();
			final String attributeName = field.getName();

			final Object value = decodeAttribute(resource, node, attributeType,
					attributeName);

			if (null == value)
			{
				if (null == field.get(resource))
				{
					throw new Exception(
							String.format(
									"Attribute '%s' of type '%s' not found on node '%s' and no default value was assigned.",
									attributeName, attributeType,
									resTree.getName(node)));
				}
			} else
			{
				field.set(resource, value);
			}

		}
	}

	private Object decodeAttribute(final Resource res, final Object node,
			final Class<?> type, final String name) throws Exception
	{
		// Order is important

		if (ListResource.class.isAssignableFrom(type))
			return decodeListAttribute(res, node, type, name);

		if (Resource.class.isAssignableFrom(type))
			return decode(resTree.getChild(node, name));

		if (String.class.isAssignableFrom(type))
			return resTree.getAttribute(node, name);

		if (String[].class.isAssignableFrom(type))
			return resTree.getArrayAttribute(node, name);

		if (double.class.isAssignableFrom(type))
			return resTree.getDoubleAttribute(node, name);

		if (double[].class.isAssignableFrom(type))
			return resTree.getDoubleArrayAttribute(node, name);

		if (float.class.isAssignableFrom(type))
			return resTree.getFloatAttribute(node, name);

		if (float[].class.isAssignableFrom(type))
			return resTree.getFloatArrayAttribute(node, name);

		if (int.class.isAssignableFrom(type))
			return resTree.getIntAttribute(node, name);

		if (int[].class.isAssignableFrom(type))
			return resTree.getIntArrayAttribute(node, name);

		if (boolean.class.isAssignableFrom(type))
			return resTree.getBooleanAttribute(node, name);

		throw new UnsupportedOperationException("Cannot convert type: " + type);

	}

	private Object decodeListAttribute(final Resource res, final Object node,
			final Class<?> type, final String name) throws Exception
	{
		final ListResource<Resource> list = new ListResource<Resource>();
		final Object listNode = resTree.getChild(node, name);
		if (null != listNode)
		{
			for (int i = 0; i < resTree.getChildCount(listNode); i++)
			{
				final Resource childRes = decode(resTree.getChild(listNode, i));
				list.add(childRes);
			}
		}
		return list;
	}

	private Resource createInstance(final String typeName) throws Exception
	{
		for (final Package pack : resourcePackages)
		{
			final String className = pack.getName() + '.' + typeName;

			try
			{
				@SuppressWarnings("unchecked")
				final Class<? extends Resource> type = (Class<? extends Resource>) Class
						.forName(className);
				return type.newInstance();
			} catch (final Exception e)
			{
				Logger.getLogger(getClass().getName()).warning(
						String.format("Class '%s' not found in package '%s'.",
								typeName, pack.getName()));
			}

		}
		throw new Exception("Could not create type instance: " + typeName);
	}
}