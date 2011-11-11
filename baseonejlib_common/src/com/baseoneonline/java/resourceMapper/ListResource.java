package com.baseoneonline.java.resourceMapper;

import java.util.ArrayList;

public class ListResource<T extends IDResource> extends ArrayList<T> implements
		Resource
{
	public ListResource()
	{

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

}
