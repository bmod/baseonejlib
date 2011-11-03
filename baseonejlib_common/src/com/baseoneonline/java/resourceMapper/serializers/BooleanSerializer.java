package com.baseoneonline.java.resourceMapper.serializers;

public class BooleanSerializer implements Serializer<Boolean>
{
	@Override
	public Boolean deserialize(final String value)
	{
		return Boolean.parseBoolean(value);
	}

	@Override
	public String serialize(final Object value)
	{
		return Boolean.toString((Boolean) value);
	}

	@Override
	public Class<?> getType()
	{
		return boolean.class;
	}
}