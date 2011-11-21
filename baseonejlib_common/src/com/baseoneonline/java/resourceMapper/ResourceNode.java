package com.baseoneonline.java.resourceMapper;

public class ResourceNode
{
	private final String id;
	private final Resource resource;

	public ResourceNode(String id, Resource resource)
	{
		this.id = id;
		this.resource = resource;
	}

	public Resource getResource()
	{
		return resource;
	}

	public String getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return id + " (" + resource.getClass().getSimpleName() + ")";
	}

}