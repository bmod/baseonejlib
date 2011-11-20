package com.baseoneonline.java.resourceMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListResource<T extends IDResource> extends Resource implements
		Iterable<T>
{

	private final List<T> list = new ArrayList<T>();
	private final Class<? extends T> elementType;

	public ListResource(Class<? extends T> elementType)
	{
		this.elementType = elementType;
	}

	@Override
	public List<ResourceNode> getChildren()
	{
		List<ResourceNode> children = new ArrayList<ResourceNode>();
		for (IDResource res : list)
		{
			children.add(new ResourceNode(res.id, res));
		}
		return children;
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

	@Override
	public Iterator<T> iterator()
	{
		return list.iterator();
	}

	public void add(T child)
	{
		list.add(child);
	}
}
