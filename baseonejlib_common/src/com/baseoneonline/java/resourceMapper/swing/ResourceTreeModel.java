package com.baseoneonline.java.resourceMapper.swing;

import com.baseoneonline.java.resourceMapper.Resource;
import com.baseoneonline.java.resourceMapper.ResourceNode;
import com.baseoneonline.java.resourceMapper.ResourceUtil;
import com.baseoneonline.java.swing.AbstractTreeModel;

public class ResourceTreeModel extends AbstractTreeModel
{

	private final ResourceNode rootResource;

	public ResourceTreeModel(Resource rootResource)
	{
		this.rootResource = new ResourceNode("Root", rootResource);
	}

	@Override
	public Object getRoot()
	{

		return rootResource;
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		return ResourceUtil.getChildren((Resource) parent).get(index);
	}

	@Override
	public int getChildCount(Object parent)
	{
		return ResourceUtil.getChildren((Resource) parent).size();
	}

	@Override
	public boolean isLeaf(Object node)
	{
		return getChildCount(node) == 0;
	}

}
