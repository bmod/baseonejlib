package test;

import java.nio.FloatBuffer;

import test.util.CameraOrbitNode;
import test.util.Trail;
import test.util.WireBox;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.geom.BufferUtils;
import com.baseoneonline.jlib.ardor3d.ArdorUtil;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.controllers.NoiseRotationController;
import com.baseoneonline.jlib.ardor3d.math.BSpline3;
import com.baseoneonline.jlib.ardor3d.math.Curve3;

public class TestPolyStrip extends GameBase {

	public static void main(final String[] args) {
		final TestPolyStrip app = new TestPolyStrip();

		app.start(1280, 720, false);
	}

	private CameraOrbitNode orbiter;
	private Trail trail;

	@Override
	protected void init() {
		orbiter = new CameraOrbitNode(camera);

		root.attachChild(trail);

		final NoiseRotationController ctrl = new NoiseRotationController();
		ctrl.setSpeed(.01f);
		orbiter.addController(ctrl);
		root.attachChild(orbiter);
		final WireBox box = new WireBox();
		root.attachChild(box);

		// /
		Vector3[] cvs = new Vector3[4];
		for (int i = 0; i < cvs.length; i++) {
			double t = (double) i / (double) (cvs.length - 1);
			double z = (t - 0) * 5;
			Vector3 v = new Vector3(0, 0, z);
			ArdorUtil.randomize(v, .3);
			cvs[i] = v;
		}

		Curve3 cv = new BSpline3(cvs);
		root.attachChild(new Ribbon(cv));
	}

	@Override
	protected void update(ReadOnlyTimer timer) {
		// TODO Auto-generated method stub

	}

}

class Ribbon extends Mesh {
	private final Curve3 curve;

	private final int width = 3;
	private final int height = 3;
	private final int depth = 20;
	private final Vector3 scale = new Vector3(.2, .2, 1);

	private final ReadOnlyVector3[] vertices;

	public Ribbon(Curve3 curve) {
		this.curve = curve;

		_meshData.setIndexMode(IndexMode.Points);

		vertices = createBaseGrid(width, height, depth);

		_meshData.setVertexBuffer(BufferUtils.createFloatBuffer(vertices));
		_meshData.setColorBuffer(ArdorUtil.createFloatBuffer(_defaultColor,
				vertices.length));

		rebuild();
	}

	private ReadOnlyVector3[] createBaseGrid(int w, int h, int d) {
		ReadOnlyVector3[] vertices = new Vector3[width * height * depth];
		int i = 0;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				for (int z = 0; z < d; z++) {
					double vx = (((float) x / (float) (w - 1)) - .5);
					double vy = (((float) y / (float) (h - 1)) - .5);
					double vz = (((float) z / (float) (d - 1)) - .5);
					vertices[i++] = new Vector3(vx, vy, vz)
							.multiplyLocal(scale);
				}
			}
		}
		return vertices;
	}

	private void rebuild() {
		Vector3 vec = Vector3.fetchTempInstance();
		FloatBuffer buf = _meshData.getVertexBuffer();
		buf.rewind();

		for (int i = 0; i < vertices.length; i++) {
			curve.getPoint(vertices[i].getZ(), vec);
			buf.put(vec.getXf()).put(vec.getYf()).put(vec.getZf());
		}
		Vector3.releaseTempInstance(vec);
	}
}