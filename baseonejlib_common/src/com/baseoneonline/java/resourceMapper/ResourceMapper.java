package com.baseoneonline.java.resourceMapper;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.baseoneonline.java.tools.StringUtils;

public abstract class ResourceMapper
{

	private static final HashMap<Class<?>, FieldSerializer<?>> valueSerializers = new HashMap<Class<?>, FieldSerializer<?>>();
	static
	{
		valueSerializers.put(String.class, new StringFieldSerializer());
		valueSerializers.put(int.class, new IntFieldSerializer());
		valueSerializers.put(String[].class, new StringArrayFieldSerializer());
		valueSerializers.put(File.class, new FileFieldSerializer());
		valueSerializers.put(float.class, new FloatFieldSerializer());
		valueSerializers.put(boolean.class, new BooleanFieldSerializer());
	}

	private final HashMap<Class<? extends Resource>, String[]> resourceKeyCache = new HashMap<Class<? extends Resource>, String[]>();

	private final ResourceLocator resourceLocator;

	private static final String ID = "id";

	private final Set<Package> resourcePackages = new HashSet<Package>();

	private static final Logger LOG = Logger.getLogger(ResourceMapper.class
			.getName());

	public ResourceMapper(final ResourceLocator resourceLocator)
	{
		this.resourceLocator = resourceLocator;

	}

	/**
	 * Load a resource file and return the resulting {@link Resource}
	 * 
	 * 
	 * @param <T>
	 *            The type of {@link Resource} we want to process.
	 * @param type
	 *            Exact type of resource to process and return
	 * @param inFile
	 *            Relative path to the resource.
	 * @return A {@link Resource} subclass based on the file's contents.
	 * @throws Exception
	 *             When loading or parsing failed.
	 */
	public <T extends Resource> T load(final Class<T> type, final String inFile)
			throws Exception
	{
		return load(type, resourceLocator.getInputStream(inFile));
	}

	public ResourceNode loadNode(final String inFile) throws Exception
	{
		return loadNode(resourceLocator.getInputStream(inFile));
	}

	@SuppressWarnings("unchecked")
	private <T extends Resource> T load(final Class<T> type,
			final InputStream is) throws Exception
	{
		addResourcePackage(type.getPackage());
		final ResourceNode node = loadNode(is);
		return (T) unmarshallResource(node);
	}

	public void addResourcePackage(final Package pack)
	{
		resourcePackages.add(pack);
	}

	protected abstract ResourceNode loadNode(InputStream in) throws Exception;

	protected abstract ResourceNode createNode(String name);

	protected abstract void write(ResourceNode node, OutputStream out)
			throws Exception;

	protected ResourceNode marshallResource(final Resource res, String name)
			throws Exception
	{
		if (null == name)
			name = res.getClass().getSimpleName();

		final ResourceNode node = createNode(name);

		for (final Field field : res.getClass().getFields())
		{
			marshallField(node, field, res);
		}

		return node;
	}

	protected void marshallField(final ResourceNode parent, final Field field,
			final Resource res) throws Exception
	{
		final Class<?> fieldType = field.getType();

		if (ListResource.class.isAssignableFrom(fieldType))
		{

			final ListResource<?> child = (ListResource<?>) field.get(res);
			@SuppressWarnings("unchecked")
			final ResourceNode node = marshallListResource(
					(ListResource<Resource>) child, field.getName());
			parent.addChild(node);

		} else if (Resource.class.isAssignableFrom(fieldType))
		{
			final Resource child = (Resource) field.get(res);
			final ResourceNode childNode = marshallResource(child,
					fieldType.getSimpleName());
			childNode.setAttribute(ID, field.getName());
			parent.addChild(childNode);

		} else
		{
			final Object value = field.get(res);

			parent.setAttribute(field.getName(), getFieldConverter(fieldType)
					.serialize(value));

		}
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

	/**
	 * Convert a {@link ResourceNode} into a {@link Resource} subclass.
	 * 
	 * @param node
	 *            The node to be converted.
	 * @return A {@link ResourceNode} sub type containing all expected data.
	 * @throws Exception
	 */
	private Resource unmarshallResource(final ResourceNode node)
			throws Exception
	{
		final Resource res = createResource(node.getName());

		for (final Field field : res.getClass().getFields())
		{
			final Class<?> fieldType = field.getType();
			final String name = field.getName();

			final Object value = getFieldValue(node, fieldType, name);

			if (null == value)
			{
				// Didn't find attribute on ResourceNode
				if (field.get(res) == null)
				{
					// Didn't find default value on Resource either, fail!
					throw new RuntimeException(String.format(
							"No attribute '%s' found on node '%s'"
									+ " and no default value was defined.",
							field.getName(), node.getName()));
				}
			} else
			{
				// Attribute found, overriding any default values.
				field.set(res, value);
			}

		}

		return res;
	}

	/**
	 * Create a {@link Resource} subclass instance using the provided class
	 * name.
	 * 
	 * @param name
	 *            The type name (without package name) of the class to be
	 *            instantiated.
	 * @return A bare {@link Resource} subclass as the defined by the provided
	 *         name.
	 * @throws Exception
	 */
	private Resource createResource(final String name) throws Exception
	{
		final Class<? extends Resource> type = resolveType(name);

		Resource res = null;
		try
		{
			res = type.newInstance();
		} catch (final InstantiationException e)
		{
			throw new Exception(
					"Instantiation error, maybe no parameterless constructor? "
							+ type.getName());
		}
		return res;
	}

	/**
	 * Retrieve a value from a node, either an attribute or a {@link Resource}.
	 * 
	 * @param node
	 *            The node to be searched.
	 * @param fieldType
	 *            The expected value type.
	 * @param name
	 *            The name of the attribute or child
	 * @return A value as defined by this method's parameters.
	 * @throws Exception
	 *             When the value was not found.
	 */
	private Object getFieldValue(final ResourceNode node,
			final Class<?> fieldType, final String name) throws Exception
	{
		// First, see if we encountered a Resource type.

		if (ListResource.class.isAssignableFrom(fieldType))
		{
			return unmarshallListResource(node, name);

		}
		if (Resource.class.isAssignableFrom(fieldType))
		{
			// A Resource
			final ResourceNode childNode = node.getChild(fieldType
					.getSimpleName());
			if (null == childNode)
				throw new RuntimeException(String.format(
						"Child '%s' not found in node '%s'.",
						fieldType.getSimpleName(), node.getName()));
			return unmarshallResource(childNode);
		}

		// A primitive value or other
		final String attribute = node.getAttribute(name);

		if (null == attribute)
		{
			LOG.info(String.format(
					"Attribute '%s' not found on node '%s', using default.",
					name, node.getName()));
			return null;
		}

		try
		{
			return getFieldConverter(fieldType).deserialize(attribute);
		} catch (final UnsupportedOperationException e)
		{
			throw new RuntimeException("Error while evaluating: "
					+ node.getName(), e);
		}

	}

	/**
	 * 
	 * 
	 * @param <T>
	 * @param parent
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private <T extends Resource> ListResource<T> unmarshallListResource(
			final ResourceNode parent, final String name) throws Exception
	{

		// A List
		final ResourceNode node = parent.getChild(name);
		if (null == node)
		{
			throw new Exception(String.format(
					"Child '%s' not found on node '%s'.", name, node.getName()));
		}

		final ListResource<T> list = new ListResource<T>();
		for (final ResourceNode childNode : node.getChildren())
		{
			list.add((T) unmarshallResource(childNode));
		}
		return list;
	}

	/* ##### UTILS ##### */

	// private static ResourceNode getChildByIDAndType(final ResourceNode node,
	// final String type, final String id) throws Exception {
	// for (final ResourceNode childNode : node.getChildren(type)) {
	// if (childNode.getAttribute(ID).equals(id))
	// return childNode;
	// }
	// throw new Exception("No child found with id '" + id + "' and type '"
	// + type + "' on node '" + node.getName());
	// }

	@SuppressWarnings("unchecked")
	protected Class<? extends Resource> resolveType(final String type)
			throws ClassNotFoundException
	{
		for (final Package pack : resourcePackages)
		{
			try
			{
				return (Class<? extends Resource>) Class.forName(pack.getName()
						+ '.' + type);
			} catch (final ClassNotFoundException e)
			{
				LOG.warning("Failed to find type '" + type + "' in package '"
						+ pack.getName() + "'.");
			}
		}
		throw new RuntimeException(
				"Could not resolve type in any of the resource packages: "
						+ type);

	}

	public void write(final Resource res, final OutputStream out)
			throws Exception
	{
		final ResourceNode xMarshalled = marshallResource(res, null);
		write(xMarshalled, out);
	}

	private FieldSerializer<?> getFieldConverter(final Class<?> type)
			throws Exception
	{
		if (!valueSerializers.containsKey(type))
			throw new UnsupportedOperationException(
					"No field converter for type: " + type.getName());
		return valueSerializers.get(type);
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
		write(res, resourceLocator.getOutputSteam(outFile));
	}

	public String[] getResourceKeys(final Resource res)
	{
		String[] keys = resourceKeyCache.get(res.getClass());
		if (null == keys)
		{
			keys = new String[res.getClass().getFields().length];
			for (int i = 0; i < keys.length; i++)
			{
				keys[i] = res.getClass().getFields()[i].getName();
			}
			resourceKeyCache.put(res.getClass(), keys);
		}
		return keys;
	}

	public Object getResourceValue(final Resource res, final String key)
	{
		try
		{
			return res.getClass().getField(key).get(res);
		} catch (final Exception e)
		{
			throw new RuntimeException(
					String.format("Key '%s' not found on resource '%s'.", key,
							res.getClass()));
		}
	}

	public Object getResourceValue(final ResourceNode node, final String key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String serializeValue(final Object value)
	{
		try
		{
			return getFieldConverter(value.getClass()).serialize(value);
		} catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public ResourceLocator getResourceLocator()
	{
		return resourceLocator;
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

class BooleanFieldSerializer implements FieldSerializer<Boolean>
{

	@Override
	public Boolean deserialize(final String value) throws Exception
	{
		return Boolean.parseBoolean(value);
	}

	@Override
	public String serialize(final Object value)
	{
		return value.toString();
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