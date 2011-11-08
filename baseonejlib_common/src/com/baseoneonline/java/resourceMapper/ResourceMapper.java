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

	public <E extends Resource> E decode(final Class<E> type, String path)
			throws Exception
	{
		addResourcePackage(type.getPackage());
		return decode(resTree.load(path));
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
			return decode(resTree.getChild(node, type.getSimpleName()));

		Object value = resTree.get(node, name, type);

		return value;

	}

	private Object decodeListAttribute(final Resource res, final Object node,
			final Class<?> type, final String name) throws Exception
	{
		final ListResource<IDResource> list = new ListResource<IDResource>();
		final Object listNode = resTree.getChild(node, name);
		if (null != listNode)
		{
			for (int i = 0; i < resTree.getChildCount(listNode); i++)
			{
				final Resource childRes = decode(resTree.getChild(listNode, i));
				list.add((IDResource) childRes);
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

	public void encode(Resource res, String path)
	{

	}
}