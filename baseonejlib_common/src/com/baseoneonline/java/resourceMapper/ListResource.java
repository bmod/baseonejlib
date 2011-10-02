package com.baseoneonline.java.resourceMapper;

import java.util.ArrayList;

public class ListResource<T extends Resource> extends ArrayList<T> implements
		Resource
{
	public ListResource(final String name)
	{
		this.name = name;
	}

	public String name;
}
