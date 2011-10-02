package com.baseoneonline.java.resourceMapper;

import com.baseoneonline.java.tools.StringUtils;

public class StringArrayFieldMarshaller implements FieldMarshaller<String[]>
{

	@Override
	public String[] unmarshall(final String value)
			throws ResourceMapperException
	{
		return value.split("[,|;]");
	}

	@Override
	public String marshall(final Object value)
	{
		return StringUtils.join((String[]) value, ",");
	}
}
