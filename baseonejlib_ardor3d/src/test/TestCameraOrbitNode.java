package test;

import test.util.CameraOrbitNode;
import test.util.Dot;
import test.util.Trail;
import test.util.WireBox;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.controllers.NoiseRotationController;
import com.baseoneonline.jlib.ardor3d.math.Simplex;

public class TestCameraOrbitNode extends GameBase
{

	public static void main(final String[] args)
	{
		final TestCameraOrbitNode app = new TestCameraOrbitNode();

		app.start(1280, 720, false);
	}

	private CameraOrbitNode orbiter;
	private Trail trail;

	@Override
	protected void init()
	{
		orbiter = new CameraOrbitNode(camera);

		root.attachChild(trail);

		final NoiseRotationController ctrl = new NoiseRotationController();
		ctrl.setSpeed(.01f);
		orbiter.addController(ctrl);
		root.attachChild(orbiter);

		for (int i = 0; i < 5; i++)
		{
			createDot(i);
		}

		final WireBox box = new WireBox();
		root.attachChild(box);
	}

	private Dot createDot(final double offset)
	{
		final Dot dot = new Dot();
		dot.addController(new SpatialController<Spatial>()
		{
			Vector3 pos = new Vector3();
			double t = 0;

			@Override
			public void update(final double time, final Spatial caller)
			{
				t += time * 2;
				noise(t, pos, offset * .04);
				caller.setTranslation(pos);
			}
		});
		root.attachChild(dot);

		trail = new Trail(dot, ColorRGBA.RED, 20, 3);
		root.attachChild(trail);
		return dot;
	}

	private Vector3 noise(final double t, final Vector3 store, final double seed)
	{
		final double x = Simplex.noise(t, 0 + seed);
		final double y = Simplex.noise(t, 22 + seed);
		final double z = Simplex.noise(t, 55 + seed);
		return store.set(x, y, z);
	}

	@Override
	protected void update(final ReadOnlyTimer timer)
	{
		// TODO Auto-generated method stub

	}

}
