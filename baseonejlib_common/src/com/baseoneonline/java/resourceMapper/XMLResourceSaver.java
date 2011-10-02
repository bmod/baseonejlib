package com.baseoneonline.java.resourceMapper;

import java.io.File;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLResourceSaver implements ResourceSaver
{

	private final File file;

	public XMLResourceSaver(final File file)
	{
		this.file = file;
	}

	@Override
	public void write(final ResourceNode node) throws ResourceMapperException
	{
		final XMLElement xml;

		// System.out.println(xml);
	}

	@Override
	public void addChild(final String name)
	{
		// TODO Auto-generated method stub

	}

}
