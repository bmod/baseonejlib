package com.baseoneonline.java.resourceMapper;

import java.io.File;

class FileFieldMarshaller implements FieldMarshaller<File>
{

	@Override
	public File unmarshall(final String value) throws ResourceMapperException
	{
		return new File(value);
	}

	@Override
	public String marshall(final Object value)
	{
		return ((File) value).getAbsolutePath();
	}
}