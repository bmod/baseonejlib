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

	private final HashMap<Class<?>, FieldProcessor<?>> fieldProcessors = new HashMap<Class<?>, FieldProcessor<?>>();

	private ResourceMapper()
	{
		fieldProcessors.put(int.class, new IntFieldProcessor());
		fieldProcessors.put(File.class, new FileFieldProcessor());
	}

	private static ResourceMapper get()
	{
		if (null == instance)
			instance = new ResourceMapper();
		return instance;
	}

	public static Resource loadResource(final ResourceLoader loader)
			throws ResourceMapperException
	{
		return get().processResource(loader.getRootNode());
	}

	private Resource processResource(final ResourceNode node)
			throws ResourceMapperException
	{
		final Resource res = createInstance(node.getName());

		processFields(node, res);

		return res;
	}

	@SuppressWarnings("unchecked")
	private void processFields(final ResourceNode node, final Resource res)
			throws ResourceMapperException
	{
		for (final Field f : res.getClass().getFields())
		{
			final Class<?> fieldType = f.getType();
			Object value = null;

			if (List.class.equals(fieldType))
			{
				value = new ArrayList<Object>();
				final ResourceNode arrayNode = node.getChild(f.getName());

				for (int i = 0; i < arrayNode.getChildCount(); i++)
				{
					final ResourceNode childNode = arrayNode.getChild(i);
					final Resource childResource = processResource(childNode);
					((List<Resource>) value).add(childResource);
				}
			} else
			{
				final String stringValue = node.getAttribute(f.getName());
				value = getFieldProcessor(fieldType).process(stringValue);
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

	private FieldProcessor<?> getFieldProcessor(final Class<?> fieldType)
			throws ResourceMapperException
	{
		final FieldProcessor<?> proc = fieldProcessors.get(fieldType);
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
	public static void addResourceClassPath(final Package p)
	{
		get().resourcePackages.add(p);
	}

	interface FieldProcessor<T>
	{

		T process(String value) throws ResourceMapperException;

	}

	class IntFieldProcessor implements FieldProcessor<Integer>
	{

		@Override
		public Integer process(final String value)
				throws ResourceMapperException
		{

			try
			{
				return Integer.parseInt(value);
			} catch (final Exception e)
			{
				throw new ResourceMapperException("Error parsing integer.");
			}
		}

	}

	class FileFieldProcessor implements FieldProcessor<File>
	{

		@Override
		public File process(final String value) throws ResourceMapperException
		{
			return new File(value);
		}
	}

}
