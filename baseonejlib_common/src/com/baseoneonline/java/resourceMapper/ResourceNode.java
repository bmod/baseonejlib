package com.baseoneonline.java.resourceMapper;

import java.util.List;
import java.util.Set;

public abstract class ResourceNode
{

	abstract String getName();

	abstract String getAttribute(String name) throws ResourceMapperException;

	abstract List<ResourceNode> getChildren() throws ResourceMapperException;

	ResourceNode getChild(final String name) throws ResourceMapperException
	{
		for (final ResourceNode node : getChildren())
		{
			if (node.getName().equals(name))
				return node;
		}
		throw new ResourceMapperException("Could not find child '" + name
				+ "' on node '" + getName() + "'");
	}

	abstract Set<String> getAttributeKeys();

}
