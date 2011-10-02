package com.baseoneonline.java.resourceMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLResourceLoader implements ResourceLoader
{

	private final XMLElement xml;

	private final HashMap<XMLElement, ResourceNode> nodeCache = new HashMap<XMLElement, ResourceNode>();

	public XMLResourceLoader(final InputStream is)
			throws ResourceMapperException
	{
		if (null == is)
			throw new NullPointerException("Parameter should not be null");
		xml = new XMLElement();
		xml.ignoreCase = false;
		try
		{
			xml.parseFromReader(new InputStreamReader(is));
		} catch (final Exception e)
		{
			throw new ResourceMapperException(
					"Error while initializing resource loader.", e);
		}
	}

	public XMLResourceLoader(final File mediaConfigFile)
			throws FileNotFoundException, ResourceMapperException
	{
		this(new FileInputStream(mediaConfigFile));
	}

	@Override
	public ResourceNode getRootNode()
	{
		return nodeFromXMLElement(xml);
	}

	private ResourceNode nodeFromXMLElement(final XMLElement x)
	{
		ResourceNode node = nodeCache.get(x);
		if (null == node)
		{

			node = new ResourceNode()
			{

				@Override
				public String getName()
				{
					return x.getName();
				}

				@Override
				public String getAttribute(final String name)
						throws ResourceMapperException
				{
					final String value = x.getStringAttribute(name);

					if (null == value)
						throw new ResourceMapperException(
								"Could not find attribute '" + name
										+ "' on node '" + getName() + "'.");
					return value;
				}

				@Override
				public ResourceNode getChild(final String name)
						throws ResourceMapperException
				{
					final ResourceNode child = nodeFromXMLElement(x
							.getChild(name));
					if (null == child)
						throw new ResourceMapperException(
								"Expected a child named '" + name
										+ "' in node '" + getName() + ".");
					return child;
				}

				@Override
				public Set<String> getAttributeKeys()
				{
					return x.getAttributeKeys();
				}

				@Override
				List<ResourceNode> getChildren() throws ResourceMapperException
				{
					final List<ResourceNode> children = new ArrayList<ResourceNode>();
					for (final XMLElement xChild : x.getChildren())
					{
						children.add(nodeFromXMLElement(xChild));
					}
					return children;
				}

			};
			nodeCache.put(x, node);
		}
		return node;
	}
}
