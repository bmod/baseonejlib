package com.baseoneonline.java.resourceMapper.serializers;

public class StringSerializer implements Serializer<String>
{
	@Override
	public String deserialize(final String value)
	{
		return value;
	}

	@Override
	public String serialize(final Object value)
	{
		return (String) value;
	}

	@Override
	public Class<?> getType()
	{
		return String.class;
	}
}