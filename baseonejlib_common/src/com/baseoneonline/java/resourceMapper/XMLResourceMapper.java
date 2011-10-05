package com.baseoneonline.java.resourceMapper;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLResourceMapper<E extends Resource> extends ResourceMapper<E>
{

	public XMLResourceMapper(Class<E> rootResource)
	{
		super(rootResource);
	}

	@Override
	protected ResourceNode loadNode(final String inFile) throws Exception
	{
		final XMLElement xml = new XMLElement();
		xml.parseFromReader(new InputStreamReader(new FileInputStream(inFile)));
		return new XMLResourceNode(xml);
	}

	@Override
	protected ResourceNode createNode(final String name)
	{
		return new XMLResourceNode(name);
	}

	@Override
	protected void write(final ResourceNode node, final String outFile)
			throws IOException
	{
		final FileWriter writer = new FileWriter(outFile);
		final XMLResourceNode xNode = (XMLResourceNode) node;
		xNode.getXML().write(writer);
		writer.flush();

	}

}
