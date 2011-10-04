package com.baseoneonline.java.resourceMapper;

import java.util.List;

public interface ResourceNode
{

	void addChild(ResourceNode child);

	void setAttribute(String name, Object value);

	ResourceNode create(String name);

	ResourceNode getChild(String name) throws Exception;

	String getName();

	String getAttribute(String name);

	List<ResourceNode> getChildren();

	List<ResourceNode> getChildren(String name);

}