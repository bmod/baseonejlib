package com.baseoneonline.java.resourceMapper;

public class StringFieldMarshaller implements FieldMarshaller<String>
{

	@Override
	public String unmarshall(final String value) throws ResourceMapperException
	{
		return value;
	}

	@Override
	public String marshall(final Object value)
	{
		return value.toString();
	}

}
