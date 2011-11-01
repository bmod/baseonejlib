package test;

import test.testResources.World;

import com.baseoneonline.java.resourceMapper.FileResourceLocator;
import com.baseoneonline.java.resourceMapper.ResourceLocator;
import com.baseoneonline.java.resourceMapper.ResourceMapper;
import com.baseoneonline.java.resourceMapper.ResourceTree;
import com.baseoneonline.java.resourceMapper.XMLResourceTree;

public class TestResourceMapper {

	public static void main(final String[] args) throws Exception {

		final String inFile = "test/testResourceIn.xml";
		final String outFile = "test/testResourceOut.xml";

		final ResourceLocator locator = new FileResourceLocator();

		final ResourceTree tree = new XMLResourceTree(locator);

		final ResourceMapper mapper = new ResourceMapper(tree);

		final World world = mapper.decode(World.class, inFile);

	}

}
