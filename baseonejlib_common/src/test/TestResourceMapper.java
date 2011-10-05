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

		ResourceMapper<World> resio = new XMLResourceMapper<World>(World.class);

		final World world = resio.load(inFile);

		resio.write(world, outFile);
	}

}
