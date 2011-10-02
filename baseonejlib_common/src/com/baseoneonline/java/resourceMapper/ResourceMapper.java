package com.baseoneonline.java.resourceMapper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResourceMapper
{

	private static ResourceMapper instance;

	private final List<Package> resourcePackages = new ArrayList<Package>();

	private final HashMap<Class<?>, FieldMarshaller<?>> fieldMarshallers = new HashMap<Class<?>, FieldMarshaller<?>>();

	private ResourceMapper()
	{
		fieldMarshallers.put(int.class, new IntFieldMarshaller());
		fieldMarshallers.put(File.class, new FileFieldMarshaller());
		fieldMarshallers.put(String[].class, new StringArrayFieldMarshaller());
		fieldMarshallers.put(String.class, new StringFieldMarshaller());
	}

	/**
	 * @return
	 */
	private static ResourceMapper get()
	{
		if (null == instance)
			instance = new ResourceMapper();
		return instance;
	}

	/**
	 * @param saver
	 * @param res
	 * @throws ResourceMapperException
	 */
	public static void saveResource(final ResourceSaver saver,
			final Resource res) throws ResourceMapperException
	{
		// TODO: Implement this chain properly
		get().marshallResource(saver, res);
	}

	/**
	 * @param saver
	 * @param res
	 */
	private void marshallResource(final ResourceSaver saver, final Resource res)
	{
		if (ListResource.class.isAssignableFrom(res.getClass()))
		{
			marshallListResource(saver, (ListResource<Resource>) res);
		} else if (Resource.class.isAssignableFrom(res.getClass()))
		{

		}
	}

	/**
	 * @param saver
	 * @param res
	 */
	private void marshallListResource(final ResourceSaver saver,
			final ListResource<Resource> res)
	{
		for (final Resource childRes : res)
		{
			saver.addChild(res.name);
		}
	}

	/**
	 * @param loader
	 * @return
	 * @throws ResourceMapperException
	 */
	public static Resource loadResource(final ResourceLoader loader)
			throws ResourceMapperException
	{
		return get().unmarshallResource(loader.getRootNode());
	}

	/**
	 * @param node
	 * @return
	 * @throws ResourceMapperException
	 */
	private Resource unmarshallResource(final ResourceNode node)
			throws ResourceMapperException
	{
		final Resource res = createInstance(node.getName());

		unmarshallFields(node, res);

		return res;
	}

	/**
	 * @param node
	 * @param res
	 * @throws ResourceMapperException
	 */
	@SuppressWarnings("unchecked")
	private void unmarshallFields(final ResourceNode node, final Resource res)
			throws ResourceMapperException
	{
		for (final Field f : res.getClass().getFields())
		{
			final Class<?> fieldType = f.getType();
			Object value = null;

			if (ListResource.class.equals(fieldType))
			{
				value = unmarshallListField(node, res, f);
			} else
			{
				final String stringValue = node.getAttribute(f.getName());
				try
				{
					value = getFieldMarshaller(fieldType).unmarshall(
							stringValue);
				} catch (final ResourceMapperException e)
				{
					throw new ResourceMapperException(
							"Error unmarshalling field: " + f.getName(), e);
				}
			}

			if (null == value)
				throw new ResourceMapperException("Value is null, debug!");

			try
			{
				f.set(res, value);
			} catch (final IllegalArgumentException e)
			{
				throw new ResourceMapperException(
						"Could not set value on node: " + node.getName()
								+ ", attribute: " + f.getName(), e);
			} catch (final IllegalAccessException e)
			{
				throw new ResourceMapperException(
						"Could not set value on node: " + node.getName()
								+ ", attribute: " + f.getName(), e);
			}
		}

	}

	/**
	 * @param node
	 * @param res
	 * @param f
	 * @return
	 * @throws ResourceMapperException
	 */
	private ListResource<Resource> unmarshallListField(final ResourceNode node,
			final Resource res, final Field f) throws ResourceMapperException
	{
		final ListResource<Resource> value = new ListResource<Resource>(
				f.getName());

		final ResourceNode arrayNode = node.getChild(f.getName());

		for (final ResourceNode childNode : arrayNode.getChildren())
		{
			final Resource childResource = unmarshallResource(childNode);
			((List<Resource>) value).add(childResource);
		}
		return value;
	}

	/**
	 * @param fieldType
	 * @return
	 * @throws ResourceMapperException
	 */
	private FieldMarshaller<?> getFieldMarshaller(final Class<?> fieldType)
			throws ResourceMapperException
	{
		final FieldMarshaller<?> proc = fieldMarshallers.get(fieldType);
		if (null == proc)
		{
			throw new ResourceMapperException("No support for field type: "
					+ fieldType);
		}
		return proc;
	}

	/**
	 * Attempt to find the target class and create an instance.
	 * 
	 * @param className
	 *            Find the class by this simple name
	 * @return A {@link Resource} instance.
	 * @throws ResourceMapperException
	 */
	@SuppressWarnings("unchecked")
	private Resource createInstance(final String className)
			throws ResourceMapperException

	{

		Class<? extends Resource> resourceType = null;

		// Cycle through defined resourcePackages and find the first matching
		// class
		for (final Package pack : resourcePackages)
		{
			String fullClassName = null;
			try
			{
				// Construct class name
				fullClassName = pack.getName() + '.' + className;

				// Find it.
				resourceType = (Class<? extends Resource>) Class
						.forName(fullClassName);

				final Resource res = resourceType.newInstance();

				return res;

			} catch (final ClassNotFoundException e)
			{
				// Fall through, throw at end if necessary
			} catch (final InstantiationException e)
			{
				throw new ResourceMapperException(
						"Matching class found, but could not instantiate: "
								+ fullClassName, e);
			} catch (final IllegalAccessException e)
			{
				throw new ResourceMapperException(
						"Matching class found, but could not instantiate: "
								+ fullClassName, e);
			}
		}
		throw new ResourceMapperException(
				"Could not resolve class '"
						+ className
						+ "', use addResourceClassPath() to add packages to resolve from.");
	}

	/**
	 * Add package for class name resolving.
	 * 
	 * @param p
	 */
	public static void addResourcePackage(final Package p)
	{
		get().resourcePackages.add(p);
	}

}
