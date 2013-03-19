package test;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.functions.SimplexNoise;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.shape.Sphere;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.controllers.EditorCameraController;

public class TestSplinePatch extends GameBase {
	public static void main(final String[] args)
	{
		new TestSplinePatch().start();
	}

	int w = 5;
	int h = 5;
	private NoiseVector3[][] vtc;

	@Override
	protected void init()
	{
		final EditorCameraController ctrl = new EditorCameraController(
				logicalLayer);

		vtc = new NoiseVector3[w][];
		for (int x = 0; x < w; x++)
		{
			vtc[x] = new NoiseVector3[h];
			for (int y = 0; y < h; y++)
			{
				final NoiseVector3 v = new NoiseVector3(x - ((double) w - 1)
						/ 2, 0, y - ((double) h - 1) / 2);
				v.setMultiplier(.1, .7, .1);
				v.update();
				vtc[x][y] = v;

				final Spatial dot = new Sphere("Sphere" + x + "-" + y, 5, 5,
						.1f);
				dot.setTranslation(v);
				root.attachChild(dot);
			}
		}

	}

	@Override
	protected void update(final ReadOnlyTimer timer)
	{

	}

}

class NoiseVector3 extends Vector3 {

	private final double time = MathUtils.rand.nextFloat() * 100.14;
	private final double speed = 1;
	private static SimplexNoise noise = new SimplexNoise();
	private final Vector3 base = new Vector3();
	private final Vector3 mult = new Vector3();

	public NoiseVector3(final double x, final double y, final double z) {
		base.set(x, y, z);
	}

	public void setMultiplier(final double x, final double y, final double z)
	{
		mult.set(x, y, z);
	}

	public void update()
	{
		final double bx = base.getX();
		final double by = base.getY();
		final double bz = base.getZ();
		final double t = time * speed;

		_x = bx + noise.noise(bx + t, t, t) * mult.getX();
		_y = by + noise.noise(t, by + t, t) * mult.getY();
		_z = bz + noise.noise(t, t, bz + t) * mult.getZ();
	}
}

class SplinePatch extends Mesh {

	private final Vector3[][] vtc;

	public SplinePatch(final Vector3[][] vtc) {
		this.vtc = vtc;
		rebuild();
	}

	public void rebuild()
	{

	}

}