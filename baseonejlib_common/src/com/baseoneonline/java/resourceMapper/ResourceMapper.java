package com.baseoneonline.java.resourceMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.baseoneonline.java.tools.StringUtils;

public abstract class ResourceMapper
{

	private final HashMap<Class<?>, FieldSerializer<?>> fieldConverters = new HashMap<Class<?>, FieldSerializer<?>>();

	public ResourceMapper(final Class<? extends Resource> rootResource)
	{
		resourcePackage = rootResource.getPackage();

		fieldConverters.put(String.class, new StringFieldSerializer());
		fieldConverters.put(int.class, new IntFieldSerializer());
		fieldConverters.put(String[].class, new StringArrayFieldSerializer());
		fieldConverters.put(File.class, new FileFieldSerializer());
		fieldConverters.put(float.class, new FloatFieldSerializer());
	}

	public Resource load(final File inFile) throws Exception
	{
		return load(new FileInputStream(inFile));
	}

	public Resource load(final String inFile) throws Exception
	{
		return load(new FileInputStream(inFile));
	}

	public Resource load(final InputStream is) throws Exception
	{
		final ResourceNode node = loadNode(is);
		return unmarshallResource(node);
	}

	protected abstract ResourceNode loadNode(InputStream in) throws Exception;

	protected abstract ResourceNode createNode(String name);

	protected abstract void write(ResourceNode node, OutputStream out)
			throws Exception;

	private static final String ID = "id";

	private final Package resourcePackage;

	/* ##### MARSHALL ##### */

	private ResourceNode marshallResource(final Resource res, String name)
			throws Exception
	{
		if (null == name)
			name = res.getClass().getSimpleName();

		final ResourceNode node = createNode(name);

		for (final Field field : res.getClass().getFields())
		{
			final Class<?> fieldType = field.getType();

			if (ListResource.class.isAssignableFrom(fieldType))
			{

				final ListResource<?> child = (ListResource<?>) field.get(res);
				@SuppressWarnings("unchecked")
				final ResourceNode childNode = marshallListResource(
						(ListResource<Resource>) child, field.getName());
				node.addChild(childNode);

			} else if (Resource.class.isAssignableFrom(fieldType))
			{
				final Resource child = (Resource) field.get(res);
				final ResourceNode childNode = marshallResource(child,
						fieldType.getSimpleName());
				childNode.setAttribute(ID, field.getName());
				node.addChild(childNode);

			} else
			{
				final Object value = field.get(res);

				node.setAttribute(field.getName(), getFieldConverter(fieldType)
						.serialize(value));

			}
		}

		return node;
	}

	private ResourceNode marshallListResource(final ListResource<Resource> res,
			final String name) throws Exception
	{
		final ResourceNode node = createNode(name);
		for (final Resource child : res)
		{
			node.addChild(marshallResource(child, child.getClass()
					.getSimpleName()));
		}
		return node;
	}

	/* ##### UNMARSHALL ##### */

	private Resource unmarshallResource(final ResourceNode node)
			throws Exception
	{
		final Class<? extends Resource> type = resolveType(node.getName());
		final Resource res = type.newInstance();

		for (final Field field : type.getFields())
		{
			final Class<?> fieldType = field.getType();

			final Object value;

			if (ListResource.class.isAssignableFrom(fieldType))
			{
				// A List
				final ResourceNode childNode = node.getChild(field.getName());
				value = unmarshallResourceArray(childNode, fieldType);

			} else if (Resource.class.isAssignableFrom(fieldType))
			{
				// A Resource
				final ResourceNode childNode = getChildByIDAndType(node,
						fieldType.getSimpleName(), field.getName());
				value = unmarshallResource(childNode);

			} else
			{
				// A primitive value or other
				final String attribute = node.getAttribute(field.getName());

				if (null == attribute)
					throw new Exception("Attribute '" + field.getName()
							+ "' not found on node: " + node.getName());
				value = getFieldConverter(field.getType()).deserialize(
						attribute);
			}

			field.set(res, value);

		}

		return res;
	}

	@SuppressWarnings("unchecked")
	private <T extends Resource> ListResource<T> unmarshallResourceArray(
			final ResourceNode node, final Class<?> type) throws Exception
	{

		final ListResource<T> list = new ListResource<T>();
		for (final ResourceNode childNode : node.getChildren())
		{
			list.add((T) unmarshallResource(childNode));
		}
		return list;
	}

	/* ##### UTILS ##### */

	private static ResourceNode getChildByIDAndType(final ResourceNode node,
			final String type, final String id) throws Exception
	{
		for (final ResourceNode childNode : node.getChildren(type))
		{
			if (childNode.getAttribute(ID).equals(id))
				return childNode;
		}
		throw new Exception("No child found with id '" + id + "' and type '"
				+ type + "' on node '" + node.getName());
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Resource> resolveType(final String type)
			throws ClassNotFoundException
	{
		return (Class<? extends Resource>) Class.forName(resourcePackage
				.getName() + '.' + type);
	}

	public void write(final Resource res, final OutputStream out)
			throws Exception
	{
		final ResourceNode xMarshalled = marshallResource(res, null);
		write(xMarshalled, out);
	}

	private FieldSerializer<?> getFieldConverter(final Class<?> type)
	{
		if (!fieldConverters.containsKey(type))
			throw new UnsupportedOperationException(
					"No field converter for type: " + type.getName());
		return fieldConverters.get(type);
	}

	/**
	 * Write a resource tree to a file.
	 * 
	 * @param res
	 *            The resource root to serialize.
	 * @param outFile
	 *            The file to write to on the file system.
	 * @throws Exception
	 */
	public void write(final Resource res, final File outFile) throws Exception
	{
		write(res, new FileOutputStream(outFile));
	}

	/**
	 * Write a resource tree to a file.
	 * 
	 * @param res
	 *            The resource root to serialize.
	 * @param outFile
	 *            The file to write to on the file system.
	 * @throws Exception
	 */
	public void write(final Resource res, final String outFile)
			throws Exception
	{
		write(res, new FileOutputStream(outFile));
	}

}

interface FieldSerializer<T>
{

	T deserialize(String value) throws Exception;

	String serialize(Object value);
}

class IntFieldSerializer implements FieldSerializer<Integer>
{

	@Override
	public Integer deserialize(final String value) throws Exception
	{
		try
		{
			return Integer.parseInt(value);
		} catch (final Exception e)
		{
			throw new Exception("Error while parsing value: " + value);
		}
	}

	@Override
	public String serialize(final Object value)
	{
		return value.toString();
	}

}

class FloatFieldSerializer implements FieldSerializer<Float>
{

	@Override
	public Float deserialize(final String value) throws Exception
	{
		try
		{
			return Float.parseFloat(value);
		} catch (final Exception e)
		{
			throw new Exception("Error while parsing value: " + value);
		}
	}

	@Override
	public String serialize(final Object value)
	{
		return value.toString();
	}

}

class StringFieldSerializer implements FieldSerializer<String>
{
	@Override
	public String deserialize(final String value) throws Exception
	{
		return value;
	}

	@Override
	public String serialize(final Object value)
	{
		return (String) value;
	}
}

class FileFieldSerializer implements FieldSerializer<File>
{
	@Override
	public File deserialize(final String value) throws Exception
	{
		return new File(value);
	}

	@Override
	public String serialize(final Object value)
	{
		return ((File) value).getAbsolutePath();
	}
}

class StringArrayFieldSerializer implements FieldSerializer<String[]>
{
	private static String DELIMITER = ",";

	@Override
	public String[] deserialize(final String value) throws Exception
	{
		return StringUtils.splitAndTrim(value, DELIMITER);
	}

	@Override
	public String serialize(final Object value)
	{
		return StringUtils.join((String[]) value, DELIMITER);
	}
}