package com.baseoneonline.jlib.ardor3d.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector3f;

import com.baseoneonline.jlib.ardor3d.framework.entities.CollisionComponent;
import com.baseoneonline.jlib.ardor3d.framework.entities.Entity;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
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

	public static int[] COLLISION_GROUPS = { 0x00000000, 0x00000001,
			0x00000002, 0x00000004, 0x00000008, 0x00000010, 0x00000020,
			0x00000040, 0x00000080, 0x00000100, 0x00000200, 0x00000400,
			0x00000800, 0x00001000, 0x00002000, 0x00004000 };

	private final DiscreteDynamicsWorld world;
	private final Map<CollisionObject, Entity> objects = new HashMap<CollisionObject, Entity>();

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
		world.performDiscreteCollisionDetection();
		gatherCollisions();
	}

	private void gatherCollisions() {
		int numManifolds = world.getDispatcher().getNumManifolds();
		for (int i = numManifolds - 1; i >= 0; i--) {
			PersistentManifold contactManifold = world.getDispatcher()
					.getManifoldByIndexInternal(i);

			// Only support one contact for now
			int numContacts = contactManifold.getNumContacts();
			if (numContacts > 0) {
				CollisionObject obA = (CollisionObject) contactManifold
						.getBody0();
				CollisionObject obB = (CollisionObject) contactManifold
						.getBody1();
				Entity entA = objects.get(obA);
				Entity entB = objects.get(obB);

				entA.onCollide(entB);
				entB.onCollide(entA);
			}
		}
	}

	public List<CollisionObject> getCollisionObjects() {
		return world.getCollisionObjectArray();
	}

	public void add(final CollisionComponent comp) {
		CollisionObject ob = comp.getCollisionObject();
		objects.put(ob, comp.getEntity());
		world.addCollisionObject(ob,
				(short) COLLISION_GROUPS[comp.getCollisionGroup()],
				(short) COLLISION_GROUPS[comp.getCollisionMask()]);

	}

	public void remove(final CollisionComponent comp) {
		world.removeCollisionObject(comp.getCollisionObject());
	}

}
