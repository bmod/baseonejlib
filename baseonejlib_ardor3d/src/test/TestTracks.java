package test;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Line;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;
import com.ardor3d.scenegraph.hint.LightCombineMode;
import com.ardor3d.scenegraph.shape.Sphere;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.ArdorUtil;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.math.BSpline3;

public class TestTracks extends GameBase {

	public static void main(final String[] args) {
		new TestTracks().start();
	}

	private Sphere sphere;
	private Road road;
	private CameraController camCtrl;

	@Override
	protected void init() {

		road = new Road();
		root.attachChild(road);

		sphere = new Sphere("Ball", 4, 6, 1);
		sphere.getSceneHints().setLightCombineMode(LightCombineMode.Off);
		final WireframeState ws = new WireframeState();
		sphere.setRenderState(ws);
		sphere.addController(new RoadController(road));
		root.attachChild(sphere);

		camCtrl = new CameraController(sphere, camera);
		camCtrl.setFOV(90);
	}

	@Override
	protected void update(final ReadOnlyTimer timer) {
	}

	@Override
	protected void lateUpdate(final ReadOnlyTimer timer) {
		camCtrl.update(timer.getTimePerFrame());
	}
}

class Road extends Node {

	private final BSpline3 curve;

	public Road() {
		final ReadOnlyVector3[] pts = Road.createPoints(100, 60);
		curve = new BSpline3(pts);
		attachChild(createCurve(1200));
	}

	private Spatial createCurve(final int samples) {
		final Vector3[] pts = new Vector3[samples + 1];

		for (int i = 0; i <= samples; i++) {
			final double t = (double) i / (double) samples;
			pts[i] = curve.getPoint(t, new Vector3());
		}

		final Line line = new Line("MyLine", pts, ArdorUtil.createArray(
				Vector3.UNIT_Y, pts.length), ArdorUtil.createArray(
				ColorRGBA.YELLOW, pts.length), ArdorUtil.createArray(
				Vector2.ZERO, pts.length));
		return line;
	}

	private int getSegments() {
		return curve.getCVCount() - 3;
	}

	private static ReadOnlyVector3[] createPoints(final int num,
			final double spacing) {
		final double deviate = 10;

		final Vector3[] pts = new Vector3[num];
		for (int i = 0; i < pts.length; i++) {
			final double rx = (MathUtils.rand.nextDouble() * 2 - 1) * deviate;
			final double ry = (MathUtils.rand.nextDouble() * 2 - 1) * deviate;
			pts[i] = new Vector3(rx, ry, i * spacing);
		}
		return pts;
	}

	public void getTransform(final double dist, final Vector3 pos,
			final Matrix3 rot) {
		final Vector3 vel = Vector3.fetchTempInstance();

		final double t = dist / getSegments();

		curve.getPoint(t, pos);
		curve.getVelocity(t, vel);
		rot.lookAt(vel, Vector3.UNIT_Y);

		Vector3.releaseTempInstance(vel);

	}
}

class RoadController implements SpatialController<Spatial> {

	private final double track = 0;
	private double distance = 0;
	private final double speed = 1;
	private final Road road;
	private final Vector3 pos = new Vector3();
	private final Matrix3 rot = new Matrix3();

	public RoadController(final Road road) {
		this.road = road;
	}

	@Override
	public void update(final double time, final Spatial caller) {
		distance += speed * time;

		road.getTransform(distance, pos, rot);
		caller.setTranslation(pos);
		caller.setRotation(rot);
	}
}

class CameraController {
	private final Spatial target;
	private final Camera cam;
	private final Vector3 camPos = new Vector3();

	public CameraController(final Spatial target, final Camera cam) {
		this.target = target;
		this.cam = cam;
	}

	public void setFOV(final double fov) {
		final double aspect = (double) cam.getWidth()
				/ (double) cam.getHeight();
		cam.setFrustumPerspective(fov, aspect, cam.getFrustumNear(),
				cam.getFrustumFar());
	}

	public void update(final double t) {
		camPos.set(target.getWorldTranslation());
		camPos.addLocal(0, 6, -10);
		cam.setLocation(camPos);
		cam.lookAt(target.getWorldTranslation(), Vector3.UNIT_Y);
	}
}