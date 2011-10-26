package com.baseoneonline.java.resourceMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClassLoaderResourceLocator implements ResourceLocator
{

	private final ClassLoader classLoader;

	public ClassLoaderResourceLocator(final ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	@Override
	public InputStream getInputStream(final String path) throws IOException
	{
		return classLoader.getResourceAsStream(path);
	}

	@Override
	public OutputStream getOutputSteam(final String path) throws IOException
	{
		throw new UnsupportedOperationException();
	}

}
