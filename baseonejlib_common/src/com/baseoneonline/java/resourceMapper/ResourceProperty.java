package com.baseoneonline.java.resourceMapper;

public class ResourceProperty
{
	private String id;
	private Object value;
	private final Resource owner;

	public ResourceProperty(Resource owner, String id, Object value)
	{
		this.id = id;
		this.value = value;
		this.owner = owner;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}
}
