package test;

import java.util.ArrayList;
import java.util.List;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Point;
import com.ardor3d.scenegraph.Point.PointType;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.ArdorUtil;
import com.baseoneonline.jlib.ardor3d.controllers.EditorCameraController;

public class TestDistPoints extends TestBase
{

	public static void main(final String[] args)
	{
		new TestDistPoints().start();
	}

	private final Solver solver = new Solver();
	private EditorCameraController orbCam;

	@Override
	protected void init()
	{
		createPoints(100);

		solver.setConstraint(new PlanarConstraint());

		lightState.setEnabled(false);
		orbCam = new EditorCameraController(logicalLayer, camera);
	}

	private void createPoints(final int num)
	{
		for (int i = 0; i < num; i++)
		{

			createPoint();
		}

	}

	private void createPoint()
	{
		final Vector3 pos = Vector3.fetchTempInstance().zero();

		final Particle p = new Particle();
		ArdorUtil.randomize(pos, 10);
		p.translation.set(pos);
		solver.add(p);
		root.attachChild(p.getSpatial());
		Vector3.releaseTempInstance(pos);
	}

	@Override
	protected void update(final ReadOnlyTimer timer)
	{
		solver.update();
		orbCam.update();
	}
}

class PlanarConstraint implements Constraint
{
	@Override
	public void apply(final Vector3 p)
	{
		p.setY(0);
	}
}

class AABBConstraint implements Constraint
{

	public AABBConstraint(final Vector3 min, final Vector3 max)
	{
	}

	@Override
	public void apply(final Vector3 p)
	{

	}
}

interface Constraint
{
	public void apply(Vector3 p);
}

class Solver
{

	private final List<Particle> particles = new ArrayList<Particle>();

	private Constraint constraint;

	public void add(final Particle p)
	{
		particles.add(p);
	}

	public void setConstraint(final Constraint c)
	{
		constraint = c;
	}

	public void update()
	{
		final int len = particles.size();
		for (int i = 0; i < len - 1; i++)
		{

			final Particle p1 = particles.get(i); // the first particle

			for (int j = i + 1; j < particles.size(); j++)
			{

				final Particle p2 = particles.get(j); // the second particle
				solve(p1, p2);
			}

			if (null != constraint)
				constraint.apply(p1.translation);
			p1.update();
		}
	}

	private void solve(final Particle a, final Particle b)
	{
		final Vector3 repelForce = Vector3.fetchTempInstance();

		a.translation.subtract(b.translation, repelForce);

		final double mag = repelForce.length();
		final double repelstrength = 40 - mag;

		repelForce.multiplyLocal(repelstrength * 0.000025 / mag);

		a.translation.addLocal(repelForce);
		b.translation.subtractLocal(repelForce);

		Vector3.releaseTempInstance(repelForce);
	}
}

class Particle
{
	private static int COUNT = 0;
	private final int index;
	public final Vector3 translation = new Vector3();

	private final Point spatial;
	public final Vector3 force = new Vector3();

	public Particle()
	{
		index = COUNT++;

		spatial = new Point("Point" + index, ArdorUtil.createArray(
				Vector3.ZERO, 1), ArdorUtil.createArray(Vector3.UNIT_Y, 1),
				ArdorUtil.createArray(ColorRGBA.YELLOW, 1),
				ArdorUtil.createArray(Vector2.ZERO, 1));
		spatial.setAntialiased(true);
		spatial.setPointSize(1);
		spatial.setPointType(PointType.Point);
	}

	public Spatial getSpatial()
	{
		return spatial;
	}

	public void update()
	{
		spatial.setTranslation(translation);
	}

	@Override
	public String toString()
	{
		return String.format("[Particle id=%s]", index);
	}
}
