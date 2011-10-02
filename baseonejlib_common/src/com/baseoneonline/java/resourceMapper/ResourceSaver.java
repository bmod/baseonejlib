package com.baseoneonline.java.resourceMapper;

public interface ResourceSaver
{

	void write(ResourceNode node) throws ResourceMapperException;

	void addChild(String name);

}
