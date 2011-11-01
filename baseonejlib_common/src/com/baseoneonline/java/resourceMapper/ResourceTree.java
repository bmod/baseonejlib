package com.baseoneonline.java.resourceMapper;

import java.io.File;
import java.lang.reflect.Array;
import java.util.HashMap;

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
	public void addSerializer(Serializer<?> serializer) {
		Class<?> type = serializer.getType();
		serializers.put(type, serializer);

		// And add array serializer as well
		Class<?> arrayType = Array.newInstance(type, 0).getClass();
		serializers.put(arrayType, new ArraySerializer(serializer, type));
		serializers.put(type, serializer);
	}

	public void put(Object node, String name, Object value) {
		try {
			String serialized = getSerializer(value.getClass())
					.serialize(value);
			putString(node, name, serialized);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object get(Object node, String name, Class<?> type) {
		String value = getString(node, name);
		if (null == value)
			return null;
		try {
			Serializer<?> ser = getSerializer(type);
			if (null == ser)
				throw new RuntimeException("Type not supported: " + type);
			return ser.deserialize(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public abstract Object addChild(Object parent, String name);

	public abstract Object getChild(Object node, String name);

	public abstract Object load(String path) throws Exception;

	public abstract void save(Object node, String path);

	private Serializer<?> getSerializer(Class<?> type) {
		for (Class<?> t : serializers.keySet()) {

			if (t.isAssignableFrom(type)) {
				return serializers.get(t);
			}
		}
		throw new RuntimeException("Could not find serializer for type: "
				+ type);
	}

}

interface Serializer<T> {
	public T deserialize(String value);

	public Class<?> getType();

	public String serialize(Object value);
}

class BooleanSerializer implements Serializer<Boolean> {
	@Override
	public Boolean deserialize(String value) {
		return Boolean.parseBoolean(value);
	}

	@Override
	public String serialize(Object value) {
		return Boolean.toString((Boolean) value);
	}

	@Override
	public Class<?> getType() {
		return boolean.class;
	}
};

class StringSerializer implements Serializer<String> {
	@Override
	public String deserialize(String value) {
		return value;
	}

	@Override
	public String serialize(Object value) {
		return (String) value;
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}
};

class IntSerializer implements Serializer<Integer> {

	@Override
	public Integer deserialize(String value) {
		return Integer.parseInt(value);
	}

	@Override
	public String serialize(Object value) {
		return Integer.toString((Integer) value);
	}

	@Override
	public Class<?> getType() {
		return int.class;
	}

};

class FloatSerializer implements Serializer<Float> {
	@Override
	public Float deserialize(String value) {
		return Float.parseFloat(value);
	}

	@Override
	public String serialize(Object value) {
		return Float.toString((Float) value);
	}

	@Override
	public Class<?> getType() {
		return float.class;
	}
};

class DoubleSerializer implements Serializer<Double> {
	@Override
	public Double deserialize(String value) {
		return Double.parseDouble(value);
	}

	@Override
	public String serialize(Object value) {
		return Double.toString((Double) value);
	}

	@Override
	public Class<?> getType() {
		return double.class;
	}

};

class FileSerializer implements Serializer<File> {
	@Override
	public File deserialize(String value) {
		return new File(value);
	};

	@Override
	public String serialize(Object value) {
		return ((File) value).getAbsolutePath();
	}

	@Override
	public Class<?> getType() {
		return File.class;
	};
};

class ArraySerializer<T> implements Serializer<T[]> {

	private final Serializer<T> serializer;
	private final Class<T> componentType;

	public ArraySerializer(Serializer<T> serializer, Class<T> componentType) {
		if (null == componentType)
			throw new NullPointerException();
		this.componentType = componentType;
		this.serializer = serializer;
	}

	@Override
	public T[] deserialize(String value) {
		String[] arr = value.split(",");
		@SuppressWarnings("unchecked")
		T[] re = (T[]) Array.newInstance(componentType, arr.length);
		for (int i = 0; i < re.length; i++) {
			re[i] = serializer.deserialize(arr[i]);
		}
		return re;
	}

	@Override
	public String serialize(Object value) {
		Object[] arr = (Object[]) value;
		String[] re = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			re[i] = serializer.serialize(arr[i]);
		}
		return StringUtils.join(re, ",");
	}

	@Override
	public Class<?> getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
