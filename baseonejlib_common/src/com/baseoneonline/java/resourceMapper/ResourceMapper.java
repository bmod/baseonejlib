package com.baseoneonline.java.resourceMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import com.baseoneonline.java.tools.StringUtils;

public abstract class ResourceMapper
{

	public ResourceMapper(Class<? extends Resource> rootResource)
	{
		resourcePackage = rootResource.getPackage();

	}

	public Resource load(File inFile) throws Exception
	{
		return load(new FileInputStream(inFile));
	}

	public Resource load(String inFile) throws Exception
	{
		return load(new FileInputStream(inFile));
	}

	public Resource load(InputStream is) throws Exception
	{
		ResourceNode node = loadNode(is);
		return unmarshallResource(node);
	}

	protected abstract ResourceNode loadNode(InputStream in) throws Exception;

	protected abstract ResourceNode createNode(String name);

	protected abstract void write(ResourceNode node, OutputStream out)
			throws Exception;

	private static final String ARRAY_DELIMITER = ",";
	private static final String ID = "id";

	private final Package resourcePackage;

	/* ##### MARSHALL ##### */

	private ResourceNode marshallResource(final Resource res, String name)
			throws Exception
	{
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
				Object value = field.get(res);

				if (String[].class.isAssignableFrom(fieldType)) {
					value = StringUtils.join((String[]) value, ARRAY_DELIMITER);
				}
				node.setAttribute(field.getName(), value);

			}
		}

		return node;
	}

	private ResourceNode marshallListResource(final ListResource<Resource> res,
			final String name) throws Exception
	{
		final ResourceNode node = createNode(name);
		for (final Resource child : res) {
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

		for (final Field field : type.getFields()) {
			final Class<?> fieldType = field.getType();

			final Object value;

			if (ListResource.class.isAssignableFrom(fieldType)) {
				// A List
				final ResourceNode childNode = node.getChild(field.getName());
				value = unmarshallResourceArray(childNode, fieldType);

			} else if (Resource.class.isAssignableFrom(fieldType)) {
				// A Resource
				final ResourceNode childNode = getChildByIDAndType(node,
						fieldType.getSimpleName(), field.getName());
				value = unmarshallResource(childNode);

			} else {
				// A primitive value or other
				final String attribute = node.getAttribute(field.getName());
				value = unmarshallAttribute(attribute, field);
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
		for (final ResourceNode childNode : node.getChildren()) {
			list.add((T) unmarshallResource(childNode));
		}
		return list;
	}

	private static Object unmarshallAttribute(final String value,
			final Field field) throws Exception
	{
		final Class<?> fieldType = field.getType();
		if (int.class.equals(fieldType)) {
			try {
				return Integer.parseInt(value);
			} catch (final Exception e) {
				throw new Exception("Error while parsing integer '"
						+ field.getName() + "' = '" + value + "'");
			}
		} else if (float.class.equals(fieldType)) {
			try {
				return Float.parseFloat(value);
			} catch (final Exception e) {
				throw new Exception("Error while parsing float '"
						+ field.getName() + "' = '" + value + "'");
			}
		} else if (String.class.equals(fieldType)) {
			return value;
		} else if (String[].class.equals(fieldType)) {
			return StringUtils.splitAndTrim(value, ARRAY_DELIMITER);
		} else {
			throw new UnsupportedOperationException(field.getName() + " = "
					+ value);
		}
	}

	/* ##### UTILS ##### */

	private static ResourceNode getChildByIDAndType(final ResourceNode node,
			final String type, final String id) throws Exception
	{
		for (final ResourceNode childNode : node.getChildren(type)) {
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

	public void write(Resource res, OutputStream out) throws Exception
	{
		final ResourceNode xMarshalled = marshallResource(res, null);
		write(xMarshalled, out);
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
	public void write(Resource res, File outFile) throws Exception
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
	public void write(Resource res, String outFile) throws Exception
	{
		write(res, new FileOutputStream(outFile));
	}

}
