package test;

import java.lang.reflect.Field;

import test.testResources.World;

import com.baseoneonline.java.resourceMapper.Resource;
import com.baseoneonline.java.resourceMapper.ListResource;
import com.baseoneonline.java.resourceMapper.ResourceIO;
import com.baseoneonline.java.resourceMapper.ResourceNode;
import com.baseoneonline.java.resourceMapper.XMLResourceIO;
import com.baseoneonline.java.resourceMapper.XMLResourceNode;
import com.baseoneonline.java.tools.StringUtils;

public class TestResourceMapper
{

	private static final String ARRAY_DELIMITER = ",";
	private static final String ID = "id";

	private final Package resourcePackage = World.class.getPackage();

	public static void main(final String[] args) throws Exception
	{
		new TestResourceMapper();
	}

	private final ResourceIO resio;

	public TestResourceMapper() throws Exception
	{
		final String inFile = "test/testResourceIn.xml";
		final String outFile = "test/testResourceOut.xml";

		resio = new XMLResourceIO();
		final ResourceNode xml = resio.loadFile(inFile);

		final World world = (World) unmarshallResource(xml);

		final ResourceNode xMarshalled = marshallResource(world, null);

		resio.write(xMarshalled, outFile);
	}

	/* ##### MARSHALL ##### */

	private ResourceNode marshallResource(final Resource res, String name)
			throws Exception
	{
		if (null == name)
			name = res.getClass().getSimpleName();

		final ResourceNode xml = new XMLResourceNode(name);

		for (final Field field : res.getClass().getFields())
		{
			final Class<?> fieldType = field.getType();

			if (ListResource.class.isAssignableFrom(fieldType))
			{

				final ListResource<?> child = (ListResource<?>) field.get(res);
				@SuppressWarnings("unchecked")
				final ResourceNode xChild = marshallListResource(
						(ListResource<Resource>) child, field.getName());
				xml.addChild(xChild);

			} else if (Resource.class.isAssignableFrom(fieldType))
			{
				final Resource child = (Resource) field.get(res);
				final ResourceNode xChild = marshallResource(child,
						fieldType.getSimpleName());
				xChild.setAttribute(ID, field.getName());
				xml.addChild(xChild);

			} else
			{
				Object value = field.get(res);

				if (String[].class.isAssignableFrom(fieldType))
				{
					value = StringUtils.join((String[]) value, ARRAY_DELIMITER);
				}
				xml.setAttribute(field.getName(), value);

			}
		}

		return xml;
	}

	private ResourceNode marshallListResource(final ListResource<Resource> res,
			final String name) throws Exception
	{
		final ResourceNode xml = resio.createNode(name);
		for (final Resource child : res)
		{
			xml.addChild(marshallResource(child, child.getClass()
					.getSimpleName()));
		}
		return xml;
	}

	/* ##### UNMARSHALL ##### */

	private Resource unmarshallResource(final ResourceNode xml) throws Exception
	{
		final Class<? extends Resource> type = resolveType(xml.getName());
		final Resource res = type.newInstance();

		for (final Field field : type.getFields())
		{
			final Class<?> fieldType = field.getType();

			final Object value;

			if (ListResource.class.isAssignableFrom(fieldType))
			{
				// A List
				final ResourceNode xChild = xml.getChild(field.getName());
				value = unmarshallResourceArray(xChild, fieldType);

			} else if (Resource.class.isAssignableFrom(fieldType))
			{
				// A Resource
				final ResourceNode xChild = getChildByIDAndType(xml,
						fieldType.getSimpleName(), field.getName());
				value = unmarshallResource(xChild);

			} else
			{
				// A primitive value or other
				final String attribute = xml.getAttribute(field.getName());
				value = unmarshallAttribute(attribute, field);
			}

			field.set(res, value);

		}

		return res;
	}

	@SuppressWarnings("unchecked")
	private <T extends Resource> ListResource<T> unmarshallResourceArray(
			final ResourceNode xml, final Class<?> type) throws Exception
	{

		final ListResource<T> list = new ListResource<T>();
		for (final ResourceNode xChild : xml.getChildren())
		{
			list.add((T) unmarshallResource(xChild));
		}
		return list;
	}

	private Object unmarshallAttribute(final String value, final Field field)
			throws Exception
	{
		final Class<?> fieldType = field.getType();
		if (int.class.equals(fieldType))
		{
			try
			{
				return Integer.parseInt(value);
			} catch (final Exception e)
			{
				throw new Exception("Error while parsing integer '"
						+ field.getName() + "' = '" + value + "'");
			}
		} else if (float.class.equals(fieldType))
		{
			try
			{
				return Float.parseFloat(value);
			} catch (final Exception e)
			{
				throw new Exception("Error while parsing float '"
						+ field.getName() + "' = '" + value + "'");
			}
		} else if (String.class.equals(fieldType))
		{
			return value;
		} else if (String[].class.equals(fieldType))
		{
			return StringUtils.splitAndTrim(value, ARRAY_DELIMITER);
		} else
		{
			throw new UnsupportedOperationException(field.getName() + " = "
					+ value);
		}
	}

	/* ##### UTILS ##### */

	private ResourceNode getChildByIDAndType(final ResourceNode xml, final String type,
			final String id) throws Exception
	{
		for (final ResourceNode xChild : xml.getChildren(type))
		{
			if (xChild.getAttribute(ID).equals(id))
				return xChild;
		}
		throw new Exception("No child found with id '" + id + "' and type '"
				+ type + "' on node '" + xml.getName());
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Resource> resolveType(final String type)
			throws ClassNotFoundException
	{
		return (Class<? extends Resource>) Class.forName(resourcePackage
				.getName() + '.' + type);
	}

}
