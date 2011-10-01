package com.baseoneonline.java.resourceMapper;

public class ResourceMapperException extends Exception
{

	public ResourceMapperException(final String message, final Exception e)
	{
		super(message, e);
	}

	public ResourceMapperException(final String string)
	{
		super(string);
	}

	public ResourceMapperException(final InstantiationException e)
	{
		super(e);
	}

}
