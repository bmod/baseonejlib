package com.baseoneonline.java.resourceMapper;

/**
 * Base class for all resource descriptors requiring a unique id
 * 
 */
public abstract class IDResource implements Resource
{

	private static int uidCount = -1;

	public String id = getUID();

	private static String getUID()
	{
		uidCount++;
		return "Resource" + uidCount;
	}

}
