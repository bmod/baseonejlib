package com.baseoneonline.java.resourceMapper;

public class ResourceNode
{
	public final String id;
	public final Resource res;

	public ResourceNode(String id, Resource res)
	{
		this.id = id;
		this.res = res;
	}

	@Override
	public String toString()
	{
		return id + " (" + res.getClass().getSimpleName() + ")";
	}

}