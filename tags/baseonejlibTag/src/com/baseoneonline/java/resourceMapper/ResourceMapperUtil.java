package com.baseoneonline.java.resourceMapper;

import java.io.InputStream;

public class ResourceMapperUtil
{
	private ResourceMapperUtil()
	{
	}

	@SuppressWarnings("unchecked")
	public static <T extends Resource> T loadResource(final String url,
			final Class<T> referenceResourceType)
			throws ResourceMapperException
	{
		ResourceMapper.addResourceClassPath(referenceResourceType.getPackage());

		final InputStream is = referenceResourceType.getClassLoader()
				.getResourceAsStream(url);
		if (null == is)
			throw new RuntimeException("Error finding resource: " + url);

		final ResourceLoader loader = new XMLResourceLoader(is);
		return (T) ResourceMapper.loadResource(loader);
	}
}
