package test;

import test.testResources.World;

import com.baseoneonline.java.resourceMapper.ResourceMapper;
import com.baseoneonline.java.resourceMapper.XMLResourceMapper;

public class TestResourceMapper
{

	public static void main(final String[] args) throws Exception
	{

		final String inFile = "test/testResourceIn.xml";
		final String outFile = "test/testResourceOut.xml";

		ResourceMapper resio = new XMLResourceMapper(World.class);

		final World world = (World) resio.load(inFile);

		resio.write(world, outFile);
	}

}
