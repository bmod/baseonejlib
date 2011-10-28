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

public abstract class ResourceMapper {

	private static final HashMap<Class<?>, FieldSerializer<?>> fieldConverters = new HashMap<Class<?>, FieldSerializer<?>>();
	static {
		fieldConverters.put(String.class, new StringFieldSerializer());
		fieldConverters.put(int.class, new IntFieldSerializer());
		fieldConverters.put(String[].class, new StringArrayFieldSerializer());
		fieldConverters.put(File.class, new FileFieldSerializer());
		fieldConverters.put(float.class, new FloatFieldSerializer());
		fieldConverters.put(boolean.class, new BooleanFieldSerializer());
	}

	private final ResourceLocator resourceLocator;

	private static final String ID = "id";

	private final Set<Package> resourcePackages = new HashSet<Package>();

	private static final Logger LOG = Logger.getLogger(ResourceMapper.class
			.getName());

	public ResourceMapper(final ResourceLocator resourceLocator) {
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
			throws Exception {
		return load(type, resourceLocator.getInputStream(inFile));
	}

	public ResourceNode loadNode(String inFile) throws Exception {
		return loadNode(resourceLocator.getInputStream(inFile));
	}

	@SuppressWarnings("unchecked")
	private <T extends Resource> T load(final Class<T> type,
			final InputStream is) throws Exception {
		addResourcePackage(type.getPackage());
		final ResourceNode node = loadNode(is);
		return (T) unmarshallResource(node);
	}

	public void addResourcePackage(Package pack) {
		resourcePackages.add(pack);
	}

	protected abstract ResourceNode loadNode(InputStream in) throws Exception;

	protected abstract ResourceNode createNode(String name);

	protected abstract void write(ResourceNode node, OutputStream out)
			throws Exception;

	private ResourceNode marshallResource(final Resource res, String name)
			throws Exception {
		if (null == name)
			name = res.getClass().getSimpleName();

		final ResourceNode node = createNode(name);

		for (final Field field : res.getClass().getFields()) {
			final Class<?> fieldType = field.getType();

			if (ListResource.class.isAssignableFrom(fieldType)) {

				final ListResource<?> child = (ListResource<?>) field.get(res);
				@SuppressWarnings("unchecked")
				final ResourceNode childNode = marshallListResource(
						(ListResource<Resource>) child, field.getName());
				node.addChild(childNode);

			} else if (Resource.class.isAssignableFrom(fieldType)) {
				final Resource child = (Resource) field.get(res);
				final ResourceNode childNode = marshallResource(child,
						fieldType.getSimpleName());
				childNode.setAttribute(ID, field.getName());
				node.addChild(childNode);

			} else {
				final Object value = field.get(res);

				node.setAttribute(field.getName(), getFieldConverter(fieldType)
						.serialize(value));

			}
		}

		return node;
	}

	private ResourceNode marshallListResource(final ListResource<Resource> res,
			final String name) throws Exception {
		final ResourceNode node = createNode(name);
		for (final Resource child : res) {
			node.addChild(marshallResource(child, child.getClass()
					.getSimpleName()));
		}
		return node;
	}

	/* ##### UNMARSHALL ##### */

	private Resource unmarshallResource(final ResourceNode node)
			throws Exception {
		final Class<? extends Resource> type = resolveType(node.getName());

		Resource res = null;
		try {
			res = type.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"Instantiation error, maybe no parameterless constructor? "
							+ type.getName());
		}

		for (final Field field : type.getFields()) {
			final Class<?> fieldType = field.getType();
			final Object value = getFieldValue(fieldType, node, field);

			if (null == value) {
				// Didn't find attribute on ResourceNode
				if (field.get(res) == null) {
					// Didn't find default value on Resource either, fail!
					throw new RuntimeException(
							String.format(
									"No attribute '%s' found on node '%s' and no default value was defined.",
									field.getName(), node.getName()));
				}
			} else {
				// Attribute found, overriding any default values.
				field.set(res, value);
			}

		}

		return res;
	}

	private Object getFieldValue(final Class<?> fieldType,
			final ResourceNode node, final Field field) throws Exception {

		if (ListResource.class.isAssignableFrom(fieldType)) {
			// A List
			final ResourceNode childNode = node.getChild(field.getName());
			if (null == childNode) {
				LOG.info(String.format("Child '%s' not found on node '%s'.",
						field.getName(), node.getName()));
				return null;
			}

			return unmarshallResourceArray(childNode, fieldType);

		} else if (Resource.class.isAssignableFrom(fieldType)) {
			// A Resource
			final ResourceNode childNode = node.getChild(fieldType
					.getSimpleName());
			return unmarshallResource(childNode);
		} else {
			// A primitive value or other
			final String attribute = node.getAttribute(field.getName());

			if (null == attribute) {
				LOG.info(String
						.format("Attribute '%s' not found on node '%s', using default.",
								field.getName(), node.getName()));
				return null;
			}

			try {
				return getFieldConverter(field.getType())
						.deserialize(attribute);
			} catch (final UnsupportedOperationException e) {
				throw new RuntimeException("Error while evaluating: "
						+ node.getName(), e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Resource> ListResource<T> unmarshallResourceArray(
			final ResourceNode node, final Class<?> type) throws Exception {

		final ListResource<T> list = new ListResource<T>();
		for (final ResourceNode childNode : node.getChildren()) {
			list.add((T) unmarshallResource(childNode));
		}
		return list;
	}

	/* ##### UTILS ##### */

	private static ResourceNode getChildByIDAndType(final ResourceNode node,
			final String type, final String id) throws Exception {
		for (final ResourceNode childNode : node.getChildren(type)) {
			if (childNode.getAttribute(ID).equals(id))
				return childNode;
		}
		throw new Exception("No child found with id '" + id + "' and type '"
				+ type + "' on node '" + node.getName());
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Resource> resolveType(final String type)
			throws ClassNotFoundException {
		for (Package pack : resourcePackages) {
			try {
				return (Class<? extends Resource>) Class.forName(pack.getName()
						+ '.' + type);
			} catch (ClassNotFoundException e) {
				LOG.warning("Failed to find type '" + type + "' in package '"
						+ pack.getName() + "'.");
			}
		}
		throw new RuntimeException(
				"Could not resolve type in any of the resource packages: "
						+ type);

	}

	public void write(final Resource res, final OutputStream out)
			throws Exception {
		final ResourceNode xMarshalled = marshallResource(res, null);
		write(xMarshalled, out);
	}

	private FieldSerializer<?> getFieldConverter(final Class<?> type)
			throws Exception {
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
	public void write(final Resource res, final String outFile)
			throws Exception {
		write(res, resourceLocator.getOutputSteam(outFile));
	}

}

interface FieldSerializer<T> {

	T deserialize(String value) throws Exception;

	String serialize(Object value);
}

class IntFieldSerializer implements FieldSerializer<Integer> {

	@Override
	public Integer deserialize(final String value) throws Exception {
		try {
			return Integer.parseInt(value);
		} catch (final Exception e) {
			throw new Exception("Error while parsing value: " + value);
		}
	}

	@Override
	public String serialize(final Object value) {
		return value.toString();
	}

}

class FloatFieldSerializer implements FieldSerializer<Float> {

	@Override
	public Float deserialize(final String value) throws Exception {
		try {
			return Float.parseFloat(value);
		} catch (final Exception e) {
			throw new Exception("Error while parsing value: " + value);
		}
	}

	@Override
	public String serialize(final Object value) {
		return value.toString();
	}

}

class StringFieldSerializer implements FieldSerializer<String> {
	@Override
	public String deserialize(final String value) throws Exception {
		return value;
	}

	@Override
	public String serialize(final Object value) {
		return (String) value;
	}
}

class FileFieldSerializer implements FieldSerializer<File> {
	@Override
	public File deserialize(final String value) throws Exception {
		return new File(value);
	}

	@Override
	public String serialize(final Object value) {
		return ((File) value).getAbsolutePath();
	}
}

class BooleanFieldSerializer implements FieldSerializer<Boolean> {

	@Override
	public Boolean deserialize(final String value) throws Exception {
		return Boolean.parseBoolean(value);
	}

	@Override
	public String serialize(final Object value) {
		return value.toString();
	}

}

class StringArrayFieldSerializer implements FieldSerializer<String[]> {
	private static String DELIMITER = ",";

	@Override
	public String[] deserialize(final String value) throws Exception {
		return StringUtils.splitAndTrim(value, DELIMITER);
	}

	@Override
	public String serialize(final Object value) {
		return StringUtils.join((String[]) value, DELIMITER);
	}
}