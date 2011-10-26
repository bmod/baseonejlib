package com.baseoneonline.java.resourceMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ResourceLocator
{
	public InputStream getInputStream(String path) throws IOException;

	public OutputStream getOutputSteam(String path) throws IOException;
}
