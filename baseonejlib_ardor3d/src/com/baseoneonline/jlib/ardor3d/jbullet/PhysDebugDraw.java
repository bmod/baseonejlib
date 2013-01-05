package com.baseoneonline.jlib.ardor3d.jbullet;

import java.util.logging.Logger;

import javax.vecmath.Vector3f;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.scenegraph.Mesh;
import com.baseoneonline.jlib.ardor3d.spatials.SimpleAxis;
import com.baseoneonline.jlib.ardor3d.spatials.WireBox;
import com.baseoneonline.jlib.ardor3d.spatials.WireSphere;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;

public class PhysDebugDraw {

	private static final Transform bTrans = new Transform();
	private static final Vector3f bVec = new Vector3f();
	private static final Vector3 vec = new Vector3();
	private static final com.ardor3d.math.Transform xfm = new com.ardor3d.math.Transform();
	private static final SimpleAxis axis = new SimpleAxis();
	private static final WireBox box = new WireBox();
	private static WireSphere sphere = new WireSphere();

	private static final ReadOnlyColorRGBA COLOR_ACTIVE = new ColorRGBA(1, 1,
			0, 1);
	private static final ReadOnlyColorRGBA COLOR_DEACTIVATING = new ColorRGBA(
			1, .5f, 0, 1);
	private static final ReadOnlyColorRGBA COLOR_SLEEP = new ColorRGBA(0, 0, 1,
			1);
	private static final ReadOnlyColorRGBA COLOR_DISABLE_DEACTIVATE = ColorRGBA.RED;
	private static final ReadOnlyColorRGBA COLOR_DISABLE_SIM = ColorRGBA.MAGENTA;

	public static void render(PhysicsWorld world, Renderer renderer) {
		for (CollisionObject ob : world.getCollisionObjects()) {
			ob.getWorldTransform(bTrans);

			ReadOnlyColorRGBA color;
			switch (ob.getActivationState()) {
			case CollisionObject.ACTIVE_TAG:
				color = COLOR_ACTIVE;
				break;
			case CollisionObject.ISLAND_SLEEPING:
				color = COLOR_SLEEP;
				break;
			case CollisionObject.WANTS_DEACTIVATION:
				color = COLOR_DEACTIVATING;
				break;
			case CollisionObject.DISABLE_DEACTIVATION:
				color = COLOR_DISABLE_DEACTIVATE;
				break;
			case CollisionObject.DISABLE_SIMULATION:
				color = COLOR_DISABLE_SIM;
				break;

			default:
				color = ColorRGBA.WHITE;
				Logger.getLogger(PhysDebugDraw.class.getName()).warning(
						"No color defined for CollisionObject activation state: "
								+ ob.getActivationState());
				break;
			}

			renderObject(bTrans, ob.getCollisionShape(), color, renderer);
		}
	}

	private static void renderObject(Transform worldTransform,
			CollisionShape shape, ReadOnlyColorRGBA color, Renderer r) {
		// Draw axis
		BulletConvert.convert(worldTransform, xfm);
		axis.setTransform(xfm);
		axis.updateGeometricState(0, false);
		axis.render(r);

		Mesh mesh = null;
		switch (shape.getShapeType()) {
		case BOX_SHAPE_PROXYTYPE:
			((BoxShape) shape).getHalfExtentsWithMargin(bVec);
			box.setExtents(BulletConvert.convert(bVec, vec));
			mesh = box;
			break;
		case SPHERE_SHAPE_PROXYTYPE:
			sphere.setRadius(((SphereShape) shape).getRadius());
			mesh = sphere;
			break;
		default:
			break;
		}

		if (mesh != null) {
			mesh.setTransform(xfm);
			mesh.setDefaultColor(color);
			mesh.updateGeometricState(0, false);
			mesh.render(r);
		}
	}
}
