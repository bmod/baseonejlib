package com.baseoneonline.java.resourceMapper.serializers;

public class FloatSerializer implements Serializer<Float>
{
	@Override
	public Float deserialize(final String value)
	{
		return Float.parseFloat(value);
	}

	@Override
	public String serialize(final Object value)
	{
		return Float.toString((Float) value);
	}

	@Override
	public Class<?> getType()
	{
		return float.class;
	}
}