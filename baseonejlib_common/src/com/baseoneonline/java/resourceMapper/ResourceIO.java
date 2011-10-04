package com.baseoneonline.java.resourceMapper;


public interface ResourceIO
{

	ResourceNode loadFile(String inFile) throws Exception;

	ResourceNode createNode(String name);

	void write(ResourceNode node, String outFile) throws Exception;

}
