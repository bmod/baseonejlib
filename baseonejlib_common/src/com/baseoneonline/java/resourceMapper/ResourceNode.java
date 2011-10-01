package com.baseoneonline.java.resourceMapper;

public interface ResourceNode
{

	String getName();

	String getAttribute(String name) throws ResourceMapperException;

	ResourceNode getChild(int i);

	int getChildCount();

	ResourceNode getChild(String name) throws ResourceMapperException;

}
