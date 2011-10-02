package com.baseoneonline.java.resourceMapper;

class IntFieldMarshaller implements FieldMarshaller<Integer>
{

	@Override
	public Integer unmarshall(final String value)
			throws ResourceMapperException
	{

		try
		{
			return Integer.parseInt(value);
		} catch (final Exception e)
		{
			throw new ResourceMapperException("Error parsing integer.");
		}
	}

	@Override
	public String marshall(final Object value)
	{
		return value.toString();
	}

}