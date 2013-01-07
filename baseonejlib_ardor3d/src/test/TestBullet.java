package test;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.framework.Canvas;
import com.ardor3d.input.Key;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.scenegraph.shape.Sphere;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.controllers.EditorCameraController;
import com.baseoneonline.jlib.ardor3d.jbullet.BulletConvert;
import com.baseoneonline.jlib.ardor3d.jbullet.PhysDebugDraw;
import com.baseoneonline.jlib.ardor3d.jbullet.PhysicsWorld;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.SphereShape;

public class TestBullet extends TestBase {
	public static void main(String[] args) {
		new TestBullet().start();
	}

	private PhysicsWorld world;
	private EditorCameraController camCtrl;

	@Override
	protected void init() {
		Box b = new Box("Box1", Vector3.ZERO, 10, 1, 10);
		root.attachChild(b);
		world = PhysicsWorld.get();
		world.setGravity(new Vector3(0, -10, 0));

		world.addBox(b, 0);

		camCtrl = new EditorCameraController(logicalLayer, camera);
		camCtrl.setDistance(20);

		SphereShape triggerShape = new SphereShape(2);
		GhostObject ob = new GhostObject();
		ob.setCollisionShape(triggerShape);
		com.bulletphysics.linearmath.Transform xf = new com.bulletphysics.linearmath.Transform();
		xf.setIdentity();
		xf.origin.set(3, 3, 0);
		ob.setWorldTransform(xf);
		world.add(ob);

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.SPACE), new TriggerAction() {

			@Override
			@MainThread
			public void perform(Canvas source, TwoInputStates inputStates,
					double tpf) {
				addModel();
			}
		}));
	}

	private void addModel() {
		int rand = MathUtils.rand.nextInt(2);
		double range = 2;
		double x = ((MathUtils.rand.nextDouble() * 2) - 1) * range;
		double y = ((MathUtils.rand.nextDouble() * 2) - 1) * range;
		Spatial s = null;
		switch (rand) {
		case 0:
			s = new Box("Box2", Vector3.ZERO, 1, 1, 1);
			s.setRotation(new Matrix3().fromAngles(10, 10, 10));
			s.setTranslation(x, 20, y);
			world.addBox((Box) s, 1);
			break;
		case 1:
			s = new Sphere("Sphere", 8, 8, 1);
			s.setRotation(new Matrix3().fromAngles(10, 10, 10));
			s.setTranslation(x, 20, y);
			world.addSphere((Sphere) s, 1);
			break;
		default:
			break;
		}

		root.attachChild(s);
	}

	@Override
	protected void update(ReadOnlyTimer timer) {
		world.update(timer.getTimePerFrame());
	}

	@Override
	protected void postUpdate(ReadOnlyTimer timer) {
		camCtrl.update();
	}

	@Override
	protected void renderExample(Renderer renderer) {
		super.renderExample(renderer);
		PhysDebugDraw.render(world, renderer);
	}
}

class DynamicsController implements SpatialController<Spatial> {

	private final CollisionObject body;
	private final Transform transform = new Transform();
	private final com.bulletphysics.linearmath.Transform bTransform = new com.bulletphysics.linearmath.Transform();

	public DynamicsController(CollisionObject body) {
		this.body = body;
	}

	public CollisionObject getBody() {
		return body;
	}

	@Override
	public void update(double time, Spatial caller) {
		body.getWorldTransform(bTransform);
		BulletConvert.convert(bTransform, transform);
		caller.setTransform(transform);
	}
}