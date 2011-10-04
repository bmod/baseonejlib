package com.baseoneonline.java.resourceMapper;

// TODO: Move the node functionality into this interface and make this an abstract class
public interface ResourceIO
{

	ResourceNode loadFile(String inFile) throws Exception;

	ResourceNode createNode(String name);

	void write(ResourceNode node, String outFile) throws Exception;

}
