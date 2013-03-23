package test;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.functions.SimplexNoise;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.hint.LightCombineMode;
import com.ardor3d.scenegraph.shape.Sphere;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.geom.BufferUtils;
import com.baseoneonline.java.math.CurveAlgorithms;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.controllers.EditorCameraController;

public class TestSplinePatch extends GameBase {
	public static void main(final String[] args) {
		new TestSplinePatch().start();
	}

	private final SimplexNoise simplex = new SimplexNoise();

	int w = 7;
	int h = 7;
	private Vector3[][] vtc;
	private Spatial[][] dots;
	private double time = 0;

	private BSpline3Patch patch;
	private SplinePatchMesh patchMesh;

	@Override
	protected void init() {

		final EditorCameraController ctrl = new EditorCameraController(
				logicalLayer);
		ctrl.update(camera);

		createPoints();

		patch = new BSpline3Patch(vtc);

		patchMesh = new SplinePatchMesh(patch, 32, 32);

		final WireframeState ws = new WireframeState();
		patchMesh.setRenderState(ws);
		patchMesh.getSceneHints().setLightCombineMode(LightCombineMode.Off);
		patchMesh.setDefaultColor(ColorRGBA.CYAN);

		root.attachChild(patchMesh);
	}

	private void createPoints() {
		vtc = new Vector3[w][];
		dots = new Spatial[w][];
		for (int x = 0; x < w; x++) {
			vtc[x] = new Vector3[h];
			dots[x] = new Spatial[h];
			for (int y = 0; y < h; y++) {
				final Vector3 v = new Vector3(x - ((double) w - 1) / 2, 0, y
						- ((double) h - 1) / 2);
				v.multiplyLocal(2);
				vtc[x][y] = v;

				final Spatial dot = new Sphere("Sphere" + x + "-" + y, 5, 5,
						.1f);
				dots[x][y] = dot;

				dot.setTranslation(v);
				root.attachChild(dot);
			}
		}
	}

	@Override
	protected void update(final ReadOnlyTimer timer) {
		final double speed = .2;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				final Vector3 dot = vtc[x][y];
				final double vx = dot.getX();
				final double vy = dot.getZ();

				final double d = simplex.noise(vx, vy + time * speed);
				dot.set(vx, d, vy);
				dots[x][y].setTranslation(dot);
			}
		}
		patchMesh.rebuild();
		time += timer.getTimePerFrame();
	}

}

class BSpline3Patch {

	private Vector3[][] vtc;
	private int width;
	private int depth;
	private final int degreeU = 3;
	private final int degreeV = degreeU;
	private int numUKnots;
	private int numVKnots;
	private double[] knotsU = new double[0];
	private double[] knotsV = new double[0];

	public BSpline3Patch(final Vector3[][] vtc) {
		setVertices(vtc);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return depth;
	}

	public void setVertices(final Vector3[][] vtc) {
		width = vtc.length;
		depth = vtc[0].length;
		this.vtc = vtc;
		generateKnots();
	}

	public void getPoint(final double u, final double v, final Vector3 store) {
		final Vector3 tmp = Vector3.fetchTempInstance();
		// TODO: Calculate these values in logical order instead of this mess
		final int spansU = width - degreeU;
		final int spansV = depth - degreeV;

		double tu = u * spansU;
		double tv = v * spansV;

		int spanU = (int) MathUtils.floor(tu);
		int spanV = (int) MathUtils.floor(tv);

		if (spanU >= spansU)
			spanU = spansU - 1;
		if (spanV >= spansV)
			spanV = spansV - 1;

		double ttu = tu - spanU;
		double ttv = tv - spanV;

		store.set(0, 0, 0);

		for (int ku = 0; ku < 4; ku++) {
			for (int kv = 0; kv < 4; kv++) {

				// int iu = spanU + ku;
				// int iv = spanV + kv;
				// if (tu >= spansU)
				// iu = spansU - 1;
				//
				// if (tv >= spansV)
				// iv = spansV - 1;

				double bu = CurveAlgorithms.bSplinePoint(ku, ttu);
				double bv = CurveAlgorithms.bSplinePoint(kv, ttv);

				tmp.set(vtc[ku][kv]);
				tmp.multiplyLocal(bu * bv);
				store.addLocal(tmp);
			}
		}

		Vector3.releaseTempInstance(tmp);
	}

	public void getNormal(final double u, final double v, final Vector3 vtx) {
		vtx.set(0, 1, 0);
	}

	private void generateKnots() {
		// resize if necessary
		numUKnots = width + degreeU + 1;
		if (knotsU.length < numUKnots)
			knotsU = new double[numUKnots];

		int j;

		for (j = 0; j < numUKnots; j++) {
			if (j <= degreeU)
				knotsU[j] = 0;
			else if (j < width)
				knotsU[j] = j - degreeU + 1;
			else if (j >= width)
				knotsU[j] = width - degreeU + 1;
		}

		numVKnots = depth + degreeV + 1;
		if (knotsV.length < numVKnots)
			knotsV = new double[numVKnots];

		for (j = 0; j < numVKnots; j++) {
			if (j <= degreeV)
				knotsV[j] = 0;
			else if (j < depth)
				knotsV[j] = j - degreeV + 1;
			else if (j >= depth)
				knotsV[j] = depth - degreeV + 1;
		}
	}

	private double splineBlend(final int i, final int k,
			final boolean useDepth, final double t) {
		double ret_val;

		// Do this just to make the maths traceable with the std algorithm
		final double[] u = useDepth ? knotsV : knotsU;

		if (k == 1) {
			ret_val = u[i] <= t && t < u[i + 1] ? 1 : 0;
		} else {
			final double b1 = splineBlend(i, k - 1, useDepth, t);
			final double b2 = splineBlend(i + 1, k - 1, useDepth, t);

			final double d1 = u[i + k - 1] - u[i];
			final double d2 = u[i + k] - u[i + 1];

			double e, f;

			e = b1 != 0 ? (t - u[i]) / d1 * b1 : 0;
			f = b2 != 0 ? (u[i + k] - t) / d2 * b2 : 0;

			ret_val = e + f;
		}

		return ret_val;
	}

}

class SplinePatchMesh extends Mesh {

	private final BSpline3Patch patch;

	private final int uSamples;
	private final int vSamples;

	public SplinePatchMesh(final BSpline3Patch patch, final int uSamples,
			final int vSamples) {
		this.patch = patch;
		this.uSamples = uSamples;
		this.vSamples = vSamples;
		initBuffers();
		rebuild();
	}

	private void initBuffers() {
		final int verts = (uSamples + 1) * (vSamples + 1);
		final int quads = uSamples * vSamples;
		_meshData.setVertexBuffer(BufferUtils.createVector3Buffer(
				_meshData.getVertexBuffer(), verts));

		_meshData.setNormalBuffer(BufferUtils.createVector3Buffer(
				_meshData.getNormalBuffer(), verts));

		_meshData.setTextureBuffer(BufferUtils.createVector2Buffer(verts), 0);

		// Indexbuffer
		final IntBuffer idc = BufferUtils.createIntBuffer(quads * 6);
		for (int x = 0; x < uSamples; x++) {
			for (int y = 0; y < vSamples; y++) {
				final int v = vSamples + 1;
				final int a = y + v * x;
				final int b = a + 1;
				final int d = y + v * (x + 1);
				final int c = d + 1;
				idc.put(a).put(b).put(d);
				idc.put(b).put(c).put(d);
			}
		}
		_meshData.setIndexBuffer(idc);

	}

	public void rebuild() {
		final FloatBuffer vtxBuf = _meshData.getVertexBuffer();
		final FloatBuffer nmlBuf = _meshData.getNormalBuffer();
		final FloatBuffer texBuf = _meshData.getTextureBuffer(0);

		vtxBuf.rewind();
		nmlBuf.rewind();
		texBuf.rewind();

		final Vector3 vtx = Vector3.fetchTempInstance();
		for (int iu = 0; iu <= uSamples; iu++) {
			for (int iv = 0; iv <= vSamples; iv++) {
				final double u = (double) iu / (double) uSamples;
				final double v = (double) iv / (double) vSamples;

				patch.getPoint(u, v, vtx);
				vtxBuf.put(vtx.getXf()).put(vtx.getYf()).put(vtx.getZf());
				patch.getNormal(u, v, vtx);
				nmlBuf.put(vtx.getXf()).put(vtx.getYf()).put(vtx.getZf());
				texBuf.put((float) u).put((float) v);
			}
		}
		Vector3.releaseTempInstance(vtx);
	}

}