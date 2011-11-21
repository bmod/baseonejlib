package com.baseoneonline.java.resourceMapper;

import java.util.ArrayList;
import java.util.List;

public class ListResource<T extends IDResource> extends ArrayList<T> implements
		Resource
{

	private final List<T> list = new ArrayList<T>();
	private final Class<? extends T> elementType;

	public ListResource(Class<? extends T> elementType)
	{
		this.elementType = elementType;
	}

	public T findByID(String id)
	{
		for (T res : this)
		{
			if (res.id.equals(id))
				return res;
		}
		throw new RuntimeException(String.format(
				"Resource with id '%s' not found.", id));
	}

	public Class<? extends T> getElementType()
	{
		return elementType;
	}

}
