package com.baseoneonline.java.resourceMapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileResourceLocator implements ResourceLocator
{

	@Override
	public InputStream getInputStream(final String path) throws IOException
	{
		return new FileInputStream(path);
	}

	@Override
	public OutputStream getOutputSteam(final String path) throws IOException
	{
		return new FileOutputStream(path);
	}

}
