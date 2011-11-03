package com.baseoneonline.java.resourceMapper.serializers;

public interface Serializer<T>
{
	public T deserialize(String value);

	public Class<?> getType();

	public String serialize(Object value);
}