package com.baseoneonline.java.resourceMapper;

import java.lang.reflect.Array;
import java.util.HashMap;

import com.baseoneonline.java.resourceMapper.serializers.BooleanSerializer;
import com.baseoneonline.java.resourceMapper.serializers.DoubleSerializer;
import com.baseoneonline.java.resourceMapper.serializers.FileSerializer;
import com.baseoneonline.java.resourceMapper.serializers.FloatSerializer;
import com.baseoneonline.java.resourceMapper.serializers.IntSerializer;
import com.baseoneonline.java.resourceMapper.serializers.Serializer;
import com.baseoneonline.java.resourceMapper.serializers.StringSerializer;
import com.baseoneonline.java.tools.StringUtils;

public abstract class ResourceTree {
	private static final HashMap<Class<?>, Serializer<?>> serializers = new HashMap<Class<?>, Serializer<?>>();

	public abstract int getChildCount(Object parent);

	public abstract Object getChild(Object parent, int index);

	public abstract String getName(Object node);

	public abstract String getString(Object node, String name);

	public abstract void putString(Object node, String name, String value);

	public ResourceTree() {
		addSerializer(new StringSerializer());
		addSerializer(new IntSerializer());
		addSerializer(new DoubleSerializer());
		addSerializer(new FloatSerializer());
		addSerializer(new FileSerializer());
		addSerializer(new BooleanSerializer());
	}

	/**
	 * Add type serialization support to this {@link ResourceTree}. Adding a
	 * {@link Serializer} will also add support for arrays of the specified
	 * type.
	 * 
	 * @param componentType
	 *            The super type to add supprt for.
	 * @param serializer
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addSerializer(final Serializer<?> serializer) {
		final Class<?> type = serializer.getType();
		serializers.put(type, serializer);

		// And add array serializer as well
		final Class<?> arrayType = Array.newInstance(type, 0).getClass();
		serializers.put(arrayType, new ArraySerializer(serializer, type));
		serializers.put(type, serializer);
	}

	public void put(final Object node, final String name, final Object value) {
		try {
			final String serialized = getSerializer(value.getClass())
					.serialize(value);
			putString(node, name, serialized);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object get(final Object node, final String name, final Class<?> type) {
		final String value = getString(node, name);
		if (null == value)
			return null;
		try {
			final Serializer<?> ser = getSerializer(type);
			if (null == ser)
				throw new RuntimeException("Type not supported: " + type);
			return ser.deserialize(value);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public abstract Object addChild(Object parent, String name);

	public abstract Object requireChild(Object node, String name);

	public abstract Object getChild(Object node, String name);

	public abstract Object load(String path) throws Exception;

	public abstract void save(Object node, String path);

	private Serializer<?> getSerializer(final Class<?> type) {
		for (final Class<?> t : serializers.keySet())
			if (t.isAssignableFrom(type))
				return serializers.get(t);
		throw new RuntimeException("Could not find serializer for type: "
				+ type);
	}

}

class ArraySerializer implements Serializer<Object> {

	private final Serializer<?> serializer;
	private final Class<?> componentType;

	public ArraySerializer(final Serializer<?> serializer,
			final Class<?> componentType) {
		if (null == componentType)
			throw new NullPointerException();
		this.componentType = componentType;
		this.serializer = serializer;
	}

	@Override
	public Object deserialize(final String value) {
		final String[] arr = value.split(",");

		// Same as: type[] re = new type[arr.length]
		final Object re = Array.newInstance(componentType, arr.length);
		for (int i = 0; i < Array.getLength(re); i++) {
			// Same as: re[i] = arr[i].trim();
			final Object val = serializer.deserialize(arr[i].toString().trim());
			Array.set(re, i, val);
		}
		return re;
	}

	@Override
	public String serialize(final Object value) {
		final Object[] arr = (Object[]) value;
		final String[] re = new String[arr.length];
		for (int i = 0; i < arr.length; i++)
			re[i] = serializer.serialize(arr[i]);
		return StringUtils.join(re, ",");
	}

	@Override
	public Class<?> getType() {
		// Don't need type
		return null;
	}

}
