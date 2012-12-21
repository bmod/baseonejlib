package test;

import java.util.Random;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.ArdorUtil;
import com.baseoneonline.jlib.ardor3d.Line;
import com.baseoneonline.jlib.ardor3d.math.BSpline3;
import com.baseoneonline.jlib.ardor3d.math.Nurbs3;

public class TestNurbs extends TestBase
{

	private static final Random random = new Random();
	private BSpline3 curve;
	private Spatial traveller;

	public static void main(final String[] args)
	{
		new TestNurbs().start();
	}

	@Override
	protected void init()
	{
		setShadowsEnabled(false);

		camera.setLocation(0, 15, 1);
		camera.lookAt(0, 0, 0, Vector3.UNIT_Y);

		canvas.getCanvasRenderer().getRenderer()
				.setBackgroundColor(ColorRGBA.DARK_GRAY);

		// final Vector3[] cvs = new Vector3[10];
		// for (int i = 0; i < cvs.length; i++)
		// {
		// cvs[i] = randomVector3(10);
		// addPoint(cvs[i]);
		// }

		final Vector3[] cvs = { new Vector3(0, 0, 0), new Vector3(0, 0, 5),
				new Vector3(5, 0, 5), new Vector3(5, 0, 0), };

		for (final Vector3 cv : cvs)
		{
			addPoint(cv);
		}

		curve = new BSpline3(cvs);

		createCurve(100);

		traveller = new Box("Traveller", Vector3.ZERO, .1, .1, .1);

		// traveller.addController(new SpatialController<Spatial>()
		// {
		// final double travelSpeed = 1;
		//
		// double curvePos = 0;
		//
		// @Override
		// public void update(final double time, final Spatial caller)
		// {
		// if (curvePos > curve.getSegmentCount())
		// curvePos %= curve.getSegmentCount();
		//
		// final Vector3 tmp = Vector3.fetchTempInstance();
		// curve.getPoint(curvePos, tmp);
		// caller.setTranslation(tmp);
		//
		// Vector3.releaseTempInstance(tmp);
		//
		// curvePos += time * travelSpeed;
		//
		// }
		// });
		root.attachChild(traveller);

	}

	@Override
	protected void update(final ReadOnlyTimer timer)
	{
	}

	private void createCurve(final int samples)
	{
		final ReadOnlyVector3[] curvePoints = new ReadOnlyVector3[samples + 1];
		for (int i = 0; i <= samples; i++)
		{
			final double t = (double) i / (double) samples * curve.getCVCount();

			final Vector3 pos = curve.getPoint(t, null);
			curvePoints[i] = pos;
		}

		final Line line = ArdorUtil.createLine("CurveLine", curvePoints,
				ColorRGBA.GREEN);

		root.attachChild(line);
	}

	private void addPoint(final ReadOnlyVector3 position)
	{
		final double radius = .05;
		final Box s = new Box("Point", Vector3.ZERO, radius, radius, radius);
		s.setTranslation(position);
		root.attachChild(s);
	}

	private static Vector3 randomVector3(final double extents)
	{
		return randomize(new Vector3(), extents);
	}

	private static Vector3 randomize(final Vector3 store, final double extents)
	{
		return store.set(randRange(extents), randRange(extents),
				randRange(extents));
	}

	private static double randRange(final double extents)
	{
		return (random.nextDouble() * 2 - 1) * extents;
	}

}

class Curve extends Node
{

	public Curve(final Nurbs3 nurbs)
	{

	}

}
