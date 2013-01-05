package test;

import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.controller.SpatialController;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.controllers.EditorCameraController;
import com.baseoneonline.jlib.ardor3d.jbullet.BulletConvert;
import com.baseoneonline.jlib.ardor3d.jbullet.PhysicsWorld;
import com.bulletphysics.collision.dispatch.CollisionObject;

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
		Box b2 = new Box("Box2", Vector3.ZERO, 1, 1, 1);
		b2.setTranslation(0, 20, 0);
		b2.setRotation(new Matrix3().fromAngles(10, 10, 10));
		root.attachChild(b2);
		world = new PhysicsWorld(canvas.getCanvasRenderer().getRenderer());
		world.setGravity(new Vector3(0, -10, 0));

		world.addBox(b, 0);
		world.addBox(b2, 1);

		camCtrl = new EditorCameraController(logicalLayer, camera);
		camCtrl.setDistance(20);
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
		world.render();
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