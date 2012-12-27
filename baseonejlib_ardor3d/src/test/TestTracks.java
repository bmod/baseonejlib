package test;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.bounding.BoundingBox;
import com.ardor3d.bounding.BoundingSphere;
import com.ardor3d.bounding.CollisionTree;
import com.ardor3d.bounding.CollisionTreeManager;
import com.ardor3d.framework.Canvas;
import com.ardor3d.input.Key;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.intersection.CollisionData;
import com.ardor3d.intersection.CollisionResults;
import com.ardor3d.intersection.PickingUtil;
import com.ardor3d.intersection.PrimitiveCollisionResults;
import com.ardor3d.intersection.PrimitiveKey;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.state.FogState;
import com.ardor3d.renderer.state.FogState.DensityFunction;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Line;
import com.ardor3d.scenegraph.MeshData;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;
import com.ardor3d.scenegraph.hint.CullHint;
import com.ardor3d.scenegraph.hint.LightCombineMode;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.scenegraph.shape.Sphere;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.ArdorUtil;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.math.BSpline3;

public class TestTracks extends GameBase {

	public static void main(final String[] args) {
		// new TestTracks().start(1280, 720, true);
		new TestTracks().start();
	}

	private Sphere player;
	private Road road;
	private CameraController camCtrl;
	private VehicleController roadCtrl;
	private final CollisionResults results = new PrimitiveCollisionResults();
	private Node obstacles;

	@Override
	protected void init() {
		CollisionTreeManager.getInstance().setTreeType(CollisionTree.Type.AABB);
		CollisionTreeManager.getInstance().setDoSort(true);

		road = new Road(TestTracks.createPoints(100, 60));
		root.attachChild(road);

		obstacles = TestTracks.createObstacles(road);
		root.attachChild(obstacles);

		player = new Sphere("Ball", 4, 6, 1);
		player.setScale(1, .5, 1);
		player.setModelBound(new BoundingSphere());
		player.updateModelBound();
		player.getSceneHints().setLightCombineMode(LightCombineMode.Off);
		final WireframeState ws = new WireframeState();
		player.setRenderState(ws);

		roadCtrl = new VehicleController(road);
		player.addController(roadCtrl);
		root.attachChild(player);

		camCtrl = new CameraController(roadCtrl, camera);

		camCtrl.setFOV(90);

		final FogState fs = new FogState();
		fs.setColor(ColorRGBA.BLACK);
		fs.setEnd(200);
		fs.setDensityFunction(DensityFunction.Linear);
		fs.setStart(0);
		root.setRenderState(fs);

		camera.setFrustumFar(fs.getEnd());

		root.getSceneHints().setCullHint(CullHint.Dynamic);

		initInput();
	}

	@Override
	protected void update(final ReadOnlyTimer timer) {
		results.clear();
		PickingUtil.findCollisions(player, obstacles, results);
		for (int i = 0; i < results.getNumber(); i++) {
			final CollisionData data = results.getCollisionData(i);
			for (final PrimitiveKey k : data.getSourcePrimitives()) {
				final MeshData md = data.getSourceMesh().getMeshData();
				final Vector3[] verts = md.getPrimitiveVertices(
						k.getPrimitiveIndex(), k.getSection(), null);

				for (final Vector3 v : verts) {
					player.getWorldTransform().applyForward(v, v);
					createExplosion(v);
				}
			}
		}
	}

	private void createExplosion(final ReadOnlyVector3 location) {
		final Sphere s = new Sphere("Sphere", 4, 4, 1);
		s.getSceneHints().setLightCombineMode(LightCombineMode.Off);
		s.setDefaultColor(ColorRGBA.ORANGE);
		s.setTranslation(location);
		s.addController(new SpatialController<Spatial>() {

			double scale = ArdorUtil.randRange(1, 1);

			@Override
			public void update(final double time, final Spatial caller) {
				scale -= .1;
				caller.setScale(scale);

				if (scale <= 0) {
					root.detachChild(s);
				}
			}
		});
		root.attachChild(s);
	}

	@Override
	protected void lateUpdate(final ReadOnlyTimer timer) {
		camCtrl.update(timer.getTimePerFrame());
	}

	private static Node createObstacles(final Road road) {
		final Node parent = new Node("Obstacles");
		double distance = 0;
		final double spacing = .3;
		final Transform tmp = new Transform();
		final Vector3 input = new Vector3();
		while (distance < road.getSegmentCount()) {
			distance += spacing;

			final int track = MathUtils.rand.nextInt(road.getTrackCount());
			input.set(track, 0, distance);
			road.getTransform(input, tmp);

			final Box obstacle = new Box("Obstacle", Vector3.ZERO, 1, 1, 1);
			obstacle.setTransform(tmp);

			obstacle.setDefaultColor(ColorRGBA.RED);
			obstacle.setModelBound(new BoundingBox());
			obstacle.updateModelBound();
			parent.attachChild(obstacle);

		}
		parent.getSceneHints().setLightCombineMode(LightCombineMode.Off);
		final WireframeState ws = new WireframeState();
		parent.setRenderState(ws);

		return parent;
	}

	private static ReadOnlyVector3[] createPoints(final int num,
			final double spacing) {
		final double maxAngle = 40;
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

	private void initInput() {
		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.A), new TriggerAction() {

			@Override
			@MainThread
			public void perform(final Canvas source,
					final TwoInputStates inputStates, final double tpf) {
				roadCtrl.shiftTrack(-1);
			}
		}));
		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.D), new TriggerAction() {

			@Override
			@MainThread
			public void perform(final Canvas source,
					final TwoInputStates inputStates, final double tpf) {
				roadCtrl.shiftTrack(1);
			}
		}));
	}
}

class Road extends Node {

	private final BSpline3 curve;
	private final int trackCount = 5;
	private final float trackSpacing = 3;

	public Road(final ReadOnlyVector3[] pts) {
		curve = new BSpline3(pts);
		for (final Spatial cv : createCurves(1200)) {
			attachChild(cv);
		}

	}

	public Vector3 getPoint(final Vector3 input, final Vector3 store) {
		final Transform tmp = Transform.fetchTempInstance();
		getTransform(input, tmp);
		store.set(tmp.getTranslation());
		Transform.releaseTempInstance(tmp);
		return store;
	}

	public Transform getTransform(final ReadOnlyVector3 input,
			final Transform store) {
		final Vector3 tmp = Vector3.fetchTempInstance();
		tmp.set(input.getX() * trackSpacing, input.getY(), input.getZ()
				/ getSegmentCount());
		curve.getTransform(tmp, Vector3.UNIT_Y, store);

		Vector3.releaseTempInstance(tmp);
		return store;
	}

	private Spatial[] createCurves(final int samples) {

		final Spatial[] lines = new Spatial[trackCount];
		final Vector3 input = new Vector3();
		for (int tk = 0; tk < trackCount; tk++) {
			final Vector3[] pts = new Vector3[samples + 1];
			for (int i = 0; i <= samples; i++) {
				final double t = (double) i / (double) samples
						* getSegmentCount();
				input.set(tk, 0, t);
				pts[i] = getPoint(input, new Vector3());
			}

			lines[tk] = new Line("MyLine", pts, ArdorUtil.createArray(
					Vector3.UNIT_Y, pts.length), ArdorUtil.createArray(
					ColorRGBA.YELLOW, pts.length), ArdorUtil.createArray(
					Vector2.ZERO, pts.length));
		}
		return lines;
	}

	public int getSegmentCount() {
		return curve.getCVCount() - 3;
	}

	public int getTrackCount() {
		return trackCount;
	}

}

class VehicleController implements SpatialController<Spatial> {

	private double nextTrack = 0;
	private final double speed = 1;
	private final Road road;
	private final Transform xform = new Transform();
	private final Vector3 input = new Vector3();
	private Spatial caller;
	private final double maxBank = -170 * MathUtils.DEG_TO_RAD;
	private double bank = 0;
	private final double trackShiftSmoothing = .1;
	private final double bankSmoothing = .2;

	public VehicleController(final Road road) {
		this.road = road;
	}

	public void shiftTrack(final int i) {
		final double n = nextTrack + i;
		if (n >= 0 && n < road.getTrackCount()) {
			nextTrack = n;
			bank = maxBank * i;
		}
	}

	public Vector3 getInput() {
		return input;
	}

	public Road getRoad() {
		return road;
	}

	@Override
	public void update(final double time, final Spatial caller) {
		this.caller = caller;

		input.setX(ArdorUtil.lerp2(trackShiftSmoothing, input.getX(), nextTrack));
		input.addLocal(0, 0, speed * 1d / 60d);

		bank = MathUtils.lerp(bankSmoothing, bank, 0);

		road.getTransform(input, xform);

		caller.setTranslation(xform.getTranslation());

		final Matrix3 tmp = Matrix3.fetchTempInstance().set(xform.getMatrix());
		final Matrix3 bankRot = Matrix3.fetchTempInstance();
		bankRot.fromAngles(0, 0, bank);
		tmp.multiplyLocal(bankRot);
		caller.setRotation(tmp);

		Matrix3.releaseTempInstance(tmp);
		Matrix3.releaseTempInstance(bankRot);
	}

	public Spatial getOwner() {
		return caller;
	}
}

class CameraController {

	private final double rotationSmoothing = .1;
	private final double trackSmoothing = .02;

	private final VehicleController roadCtrl;
	private final Vector3 input = new Vector3();
	private final Vector3 aimOffset = new Vector3(0, -20, 1);
	private final Vector3 camOffset = new Vector3(0, 5, -.15);
	private final Camera cam;

	private final Vector3 camPos = new Vector3();
	private final Vector3 aimPos = new Vector3();
	private final Vector3 up = new Vector3(Vector3.UNIT_Y);

	private final Quaternion elevRot = new Quaternion();

	private final Vector3 tmpPos = new Vector3();

	public CameraController(final VehicleController ctrl, final Camera cam) {
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
		final ReadOnlyVector3 nextInput = roadCtrl.getInput();

		final double x = ArdorUtil.lerp2(trackSmoothing, input.getX(),
				nextInput.getX());

		input.set(nextInput);
		input.setX(x);

		tmpPos.set(input);
		tmpPos.addLocal(camOffset);
		roadCtrl.getRoad().getPoint(tmpPos, camPos);
		cam.setLocation(camPos);

		tmpPos.set(input);
		tmpPos.addLocal(aimOffset);
		roadCtrl.getRoad().getPoint(tmpPos, aimPos);

		cam.lookAt(aimPos, up);
	}
}