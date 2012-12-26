package test;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyTransform;
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

		final RoadController roadCtrl = new RoadController(road);
		sphere.addController(roadCtrl);
		root.attachChild(sphere);

		camCtrl = new CameraController(roadCtrl, camera);
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

	public Transform getTransform(final ReadOnlyVector3 input,
			final Transform store) {
		final Vector3 pos = Vector3.fetchTempInstance();
		final Matrix3 rot = Matrix3.fetchTempInstance();
		final Vector3 tmp = Vector3.fetchTempInstance();
		final Vector3 vel = Vector3.fetchTempInstance();

		final double t = input.getZ() / getSegments();

		curve.getPoint(t, pos);
		curve.getVelocity(t, vel);
		rot.lookAt(vel, Vector3.UNIT_Y);

		rot.getColumn(0, tmp);
		tmp.multiplyLocal(input.getX());
		pos.addLocal(tmp);

		rot.getColumn(1, tmp);
		tmp.multiplyLocal(input.getY());
		pos.addLocal(tmp);

		store.setTranslation(pos);
		store.setRotation(rot);

		Vector3.releaseTempInstance(pos);
		Matrix3.releaseTempInstance(rot);
		Vector3.releaseTempInstance(tmp);
		Vector3.releaseTempInstance(vel);

		return store;
	}

	public Vector3 getPoint(final Vector3 input, final Vector3 store) {
		final Transform tmp = Transform.fetchTempInstance();
		getTransform(input, tmp);
		store.set(tmp.getTranslation());
		Transform.releaseTempInstance(tmp);
		return store;
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
		final double maxAngle = 90;
		final double angleChance = .8;

		final Vector3[] pts = new Vector3[num];
		final Vector3 pos = new Vector3();
		final Quaternion q = new Quaternion();
		final Quaternion tmp = new Quaternion();
		final Vector3 tmpPos = new Vector3();
		double heading;

		for (int i = 0; i < pts.length; i++) {
			if (MathUtils.rand.nextDouble() <= angleChance)
				heading = ArdorUtil.randRange(0, maxAngle)
						* MathUtils.DEG_TO_RAD;
			else
				heading = 0;

			tmp.fromEulerAngles(heading, 0, 0);
			q.multiplyLocal(tmp);
			q.apply(Vector3.UNIT_Z, tmpPos);
			tmpPos.multiplyLocal(spacing);
			pos.addLocal(tmpPos);
			pos.setY(ArdorUtil.randRange(0, 10));
			pts[i] = new Vector3(pos);
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
	private Spatial caller;

	public RoadController(final Road road) {
		this.road = road;
	}

	public double getDistance() {
		return distance;
	}

	public Road getRoad() {
		return road;
	}

	@Override
	public void update(final double time, final Spatial caller) {
		this.caller = caller;
		distance += speed * 1d / 60d;

		road.getTransform(distance, pos, rot);
		caller.setTranslation(pos);
		caller.setRotation(rot);
	}

	public Spatial getOwner() {
		return caller;
	}
}

class CameraController {

	final double rotationSmoothing = .1;
	private final double distance = 10;

	private final RoadController roadCtrl;
	private final Vector3 aimOffset = new Vector3(0, 2, 1);
	private final Camera cam;

	private final Vector3 camPos = new Vector3();
	private final Vector3 aimPos = new Vector3();
	private final Vector3 up = new Vector3(Vector3.UNIT_Y);

	private final Quaternion currRot = new Quaternion();
	private final Quaternion nextRot = new Quaternion();
	private final Quaternion elevRot = new Quaternion();

	private final Quaternion tmpRot = new Quaternion();
	private final Vector3 tmpPos = new Vector3();

	public CameraController(final RoadController ctrl, final Camera cam) {
		roadCtrl = ctrl;
		this.cam = cam;
		setElevation(30 * MathUtils.DEG_TO_RAD);
	}

	/**
	 * @param elevation
	 *            Radians
	 */
	public void setElevation(final double elevation) {
		elevRot.fromEulerAngles(0, 0, elevation);
	}

	public void setFOV(final double fov) {
		final double aspect = (double) cam.getWidth()
				/ (double) cam.getHeight();
		cam.setFrustumPerspective(fov, aspect, cam.getFrustumNear(),
				cam.getFrustumFar());
	}

	public void update(final double t) {
		final ReadOnlyTransform targetXform = roadCtrl.getOwner()
				.getWorldTransform();

		nextRot.fromRotationMatrix(targetXform.getMatrix());
		nextRot.multiplyLocal(elevRot);

		Quaternion.slerp(currRot, nextRot, rotationSmoothing, tmpRot);
		Quaternion.slerp(tmpRot, nextRot, rotationSmoothing, currRot);

		camPos.set(0, 0, -distance);
		currRot.apply(camPos, camPos);
		camPos.addLocal(targetXform.getTranslation());

		cam.setLocation(camPos);

		tmpPos.set(aimOffset);
		tmpPos.addLocal(0, 0, roadCtrl.getDistance());
		roadCtrl.getRoad().getPoint(tmpPos, aimPos);

		cam.lookAt(aimPos, up);
	}
}