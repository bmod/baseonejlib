package test;

import test.testResources.World;

import com.baseoneonline.java.resourceMapper.FileResourceLocator;
import com.baseoneonline.java.resourceMapper.ResourceMapper;
import com.baseoneonline.java.resourceMapper.XMLResourceMapper;

public class TestResourceMapper {

	public static void main(final String[] args) throws Exception {

		final String inFile = "test/testResourceIn.xml";
		final String outFile = "test/testResourceOut.xml";

		final ResourceMapper resio = new XMLResourceMapper(
				new FileResourceLocator());

		final World world = resio.load(World.class, inFile);

		resio.write(world, outFile);
	}

}
