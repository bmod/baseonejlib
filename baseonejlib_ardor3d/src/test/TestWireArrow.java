package test;


import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.controllers.NoiseRotationController;
import com.baseoneonline.jlib.ardor3d.spatials.WireArrow;

public class TestWireArrow extends TestBase
{
	public static void main(String[] args)
	{
		new TestWireArrow().start();
	}

	@Override
	protected void init()
	{
		camera.setLocation(1, 1, 5);
		camera.lookAt(Vector3.ZERO, Vector3.UNIT_Y);

		WireArrow arrow = new WireArrow();
		arrow.addController(new NoiseRotationController());
		arrow.setColor(ColorRGBA.YELLOW);
		root.attachChild(arrow);
	}

	@Override
	protected void update(ReadOnlyTimer timer)
	{

	}
}
