package com.baseoneonline.java.resourceMapper.serializers;

public class IntSerializer implements Serializer<Integer>
{

	@Override
	public Integer deserialize(final String value)
	{
		return Integer.parseInt(value);
	}

	@Override
	public String serialize(final Object value)
	{
		return Integer.toString((Integer) value);
	}

	@Override
	public Class<?> getType()
	{
		return int.class;
	}

}