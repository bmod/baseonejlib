package com.baseoneonline.java.resourceMapper;

import java.io.InputStreamReader;
import java.util.List;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLResourceTree extends ResourceTree
{

	private static final String LINK = "link";

	private final ResourceLocator locator;

	private final XMLElement root;

	public XMLResourceTree(final ResourceLocator locator, final String path)
			throws Exception
	{
		this.locator = locator;

		root = loadElement(path);
	}

	private XMLElement loadElement(final String path) throws Exception
	{
		final XMLElement root = new XMLElement();
		root.parseFromReader(new InputStreamReader(locator.getInputStream(path)));
		return root;
	}

	private List<XMLElement> children(final XMLElement parent)
	{

		for (final XMLElement xml : parent.getChildren())
		{
			if (xml.getName().equals(LINK))
			{
				parent.removeChild(xml);
				try
				{
					final XMLElement replacement = loadElement(xml
							.getStringAttribute("src"));
					parent.addChild(replacement);
				} catch (final Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return parent.getChildren();
	}

	@Override
	public int getChildCount(final Object parent)
	{
		return children((XMLElement) parent).size();
	}

	@Override
	public Object getChild(final Object parent, final int index)
	{
		return children((XMLElement) parent).get(index);
	}

	@Override
	public Object getChild(final Object parent, final String name)
	{
		return ((XMLElement) parent).getChild(name);
	}

	@Override
	public String getName(final Object node)
	{
		return ((XMLElement) node).getName();
	}

	@Override
	public String getAttribute(final Object node, final String name)
	{
		return ((XMLElement) node).getStringAttribute(name);
	}

	@Override
	public Object getRoot()
	{
		return root;
	}

	@Override
	public Object addChild(final Object parent, final String name)
	{
		final XMLElement xml = new XMLElement(name);
		((XMLElement) parent).addChild(xml);
		return xml;
	}

}
