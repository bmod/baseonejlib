package com.baseoneonline.java.resourceMapper.serializers;

public class DoubleSerializer implements Serializer<Double>
{
	@Override
	public Double deserialize(final String value)
	{
		return Double.parseDouble(value);
	}

	@Override
	public String serialize(final Object value)
	{
		return Double.toString((Double) value);
	}

	@Override
	public Class<?> getType()
	{
		return double.class;
	}

}