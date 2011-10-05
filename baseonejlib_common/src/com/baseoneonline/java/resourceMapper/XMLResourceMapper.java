package com.baseoneonline.java.resourceMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLResourceMapper extends ResourceMapper
{

	public XMLResourceMapper(Class<? extends Resource> rootResource)
	{
		super(rootResource);
	}

	@Override
	protected ResourceNode loadNode(final InputStream in) throws Exception
	{
		final XMLElement xml = new XMLElement();
		xml.parseFromReader(new InputStreamReader(in));
		return new XMLResourceNode(xml);
	}

	@Override
	protected ResourceNode createNode(final String name)
	{
		return new XMLResourceNode(name);
	}

	@Override
	protected void write(final ResourceNode node, final OutputStream out)
			throws IOException
	{
		final OutputStreamWriter writer = new OutputStreamWriter(out);
		final XMLResourceNode xNode = (XMLResourceNode) node;
		xNode.getXML().write(writer);
		writer.flush();

	}

}
