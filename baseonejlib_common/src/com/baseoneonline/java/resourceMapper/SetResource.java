package com.baseoneonline.java.resourceMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class SetResource<T extends Resource> extends Resource implements
		Iterable<T>
{

	private final HashSet<T> set = new HashSet<T>();

	@Override
	public List<ResourceNode> getChildren()
	{

		List<ResourceNode> children = new ArrayList<ResourceNode>();
		for (Resource res : set)
		{
			children.add(new ResourceNode(res.getClass().getSimpleName(), res));
		}
		return children;

	}

	@Override
	public Iterator<T> iterator()
	{
		return set.iterator();
	}

	public void add(T childRes)
	{
		set.add(childRes);
	}

}
