package test.testResources;

import com.baseoneonline.java.resourceMapper.ListResource;
import com.baseoneonline.java.resourceMapper.Resource;

public class World implements Resource
{

	public float width;
	public float height;

	public God localGod;

	public ListResource<Plant> plants;

	public ListResource<Animal> animals;
}
