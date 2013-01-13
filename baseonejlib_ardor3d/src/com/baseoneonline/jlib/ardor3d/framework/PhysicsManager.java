package com.baseoneonline.jlib.ardor3d.framework;

import java.util.List;

import javax.vecmath.Vector3f;

import com.baseoneonline.jlib.ardor3d.framework.entities.CollisionComponent;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhysicsManager {

	private static PhysicsManager instance;

	public static PhysicsManager get() {
		if (instance == null)
			instance = new PhysicsManager();
		return instance;
	}

	private static final int[] COLLISION_GROUPS = { 0x00000000, 0x00000001,
			0x00000002, 0x00000004, 0x00000008, 0x00000010, 0x00000020,
			0x00000040, 0x00000080, 0x00000100, 0x00000200, 0x00000400,
			0x00000800, 0x00001000, 0x00002000, 0x00004000 };

	private final DiscreteDynamicsWorld world;

	/**
	 * 
	 */
	private PhysicsManager() {
		final float size = 1000;
		final Vector3f min = new Vector3f(size, size, size);
		final Vector3f max = new Vector3f(size, size, size);

		final CollisionConfiguration colCfg = new DefaultCollisionConfiguration();

		final Dispatcher dispatcher = new CollisionDispatcher(colCfg);
		final BroadphaseInterface pairCache = new AxisSweep3(min, max);
		final ConstraintSolver constraintSolver = new SequentialImpulseConstraintSolver();
		world = new DiscreteDynamicsWorld(dispatcher, pairCache,
				constraintSolver, colCfg);

	}

	public void update(final double t) {
		world.stepSimulation((float) t);
		// for (final Component trigger : triggers.keySet()) {
		// final GhostObject obTrigger = triggers.get(trigger);
		// for (final Component sensor : sensors.keySet()) {
		// final GhostObject obSensor = sensors.get(sensor);
		// if (obTrigger.checkCollideWith(obSensor)) {
		// System.out.println("Collision!");
		// }
		// }
		// }
		// world.performDiscreteCollisionDetection();
	}

	// public RigidBody addBox(final Box b, final double mass) {
	// final BoxShape boxShape = BulletConvert.createBoxShape(b);
	// return addShape(b, mass, boxShape);
	// }
	//
	// public RigidBody addSphere(final Sphere s, final double mass) {
	// final SphereShape sphereShape = BulletConvert.createSphereShape(s);
	// return addShape(s, mass, sphereShape);
	// }

	// public RigidBody addShape(final Spatial spatial, final double mass,
	// final CollisionShape shape) {
	//
	// final MotionState motionState = new BulletMotionState(spatial);
	//
	// final boolean isDynamic = mass != 0;
	// final Vector3f localInertia = new Vector3f(0, 0, 0);
	// if (isDynamic) {
	// shape.calculateLocalInertia((float) mass, localInertia);
	// }
	//
	// final RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(
	// (float) mass, motionState, shape, localInertia);
	// final RigidBody body = new RigidBody(info);
	//
	// body.setWorldTransform(body.getMotionState().getWorldTransform(
	// new Transform()));
	// world.addRigidBody(body);
	// return body;
	// }

	public List<CollisionObject> getCollisionObjects() {
		return world.getCollisionObjectArray();
	}

	public void add(final CollisionComponent comp) {
		final int group = COLLISION_GROUPS[comp.getCollisionGroup()];

		int mask = 0;
		final int[] compMask = comp.getCollisionMask();
		for (int i = 0; i < compMask.length; i++)
			mask = mask | COLLISION_GROUPS[compMask[i]];

		world.addCollisionObject(comp.getCollisionObject(), (short) group,
				(short) mask);
	}

	public void remove(final CollisionComponent comp) {
		world.removeCollisionObject(comp.getCollisionObject());
	}

}
