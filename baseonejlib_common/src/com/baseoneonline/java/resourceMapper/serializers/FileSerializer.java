package com.baseoneonline.java.resourceMapper.serializers;

import java.io.File;

public class FileSerializer implements Serializer<File>
{
	@Override
	public File deserialize(final String value)
	{
		return new File(value);
	};

	@Override
	public String serialize(final Object value)
	{
		return ((File) value).getAbsolutePath();
	}

	@Override
	public Class<?> getType()
	{
		return File.class;
	};
}