package test;

import javax.vecmath.Vector3f;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.controllers.NoiseRotationController;
import com.baseoneonline.jlib.ardor3d.jbullet.PhysicsWorld;
import com.bulletphysics.dynamics.RigidBody;

public class TestJBullet extends TestBase
{

	public static void main(String[] args)
	{
		new TestJBullet().start();
	}

	private final PhysicsWorld physWorld = new PhysicsWorld();

	private Interval spawnInterval;

	@Override
	protected void init()
	{
		setShadowDistance(300);

		camera.setLocation(1, 5, 10);
		camera.lookAt(new Vector3(0, 3, 0), Vector3.UNIT_Y);

		Box floor = new Box("Floor", Vector3.ZERO, 30, 1, 30);
		floor.setTranslation(0, -.5, 0);
		root.attachChild(floor);

		RigidBody body = physWorld.add(floor);
		body.setMassProps(0, new Vector3f());

		Box obstacle = new Box("Obstacle", Vector3.ZERO, 2, 2, 2);
		obstacle.addController(new NoiseRotationController());
		root.attachChild(obstacle);

		spawnInterval = new Interval(1)
		{
			@Override
			public void call()
			{
				spawnBlock();
			}
		};

	}

	private void spawnBlock()
	{
		double size = .5;
		Box box = new Box("Block", Vector3.ZERO, size, size, size);
		root.attachChild(box);
		physWorld.add(box);
	}

	@Override
	protected void update(ReadOnlyTimer timer)
	{
		double t = timer.getTimePerFrame();
		spawnInterval.update(t);
		physWorld.update(t);
	}

}

abstract class Interval
{

	private final double interval;
	private double elapsedTime = 0;

	public Interval(double ival)
	{
		this.interval = ival;
	}

	public void update(double t)
	{
		if (elapsedTime >= interval)
		{
			elapsedTime %= interval;
			call();
		}

		elapsedTime += t;
	}

	public abstract void call();
}

class SineRotationController implements SpatialController<Spatial>
{
	private double time = 0;
	private final Matrix3 rotation = new Matrix3();
	private final Vector3 angles = new Vector3();
	private final Vector3 amplitude = new Vector3(MathUtils.DEG_TO_RAD * 10,
			MathUtils.DEG_TO_RAD * 90, MathUtils.DEG_TO_RAD * 10);

	private final Vector3 speed = new Vector3(.7, .2, 1.1);

	@Override
	public void update(double t, Spatial caller)
	{
		time += t;
		angles.set(MathUtils.sin(time * speed.getX()),
				MathUtils.sin(time * speed.getY()),
				MathUtils.cos(time * speed.getZ()));
		angles.multiplyLocal(amplitude);
		rotation.fromAngles(angles.getX(), angles.getY(), angles.getZ());
		caller.setRotation(rotation);
	}
}
