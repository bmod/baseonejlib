package com.baseoneonline.jlib.ardor3d.jbullet;

import java.util.List;

import javax.vecmath.Vector3f;

import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.scenegraph.shape.Sphere;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class PhysicsWorld {

	private static PhysicsWorld instance;

	public static PhysicsWorld get() {
		if (instance == null)
			instance = new PhysicsWorld();
		return instance;
	}

	private final DynamicsWorld world;

	/**
	 * 
	 */
	private PhysicsWorld() {
		float size = 1000;
		Vector3f min = new Vector3f(size, size, size);
		Vector3f max = new Vector3f(size, size, size);

		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		Dispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
		BroadphaseInterface pairCache = new AxisSweep3(min, max);
		ConstraintSolver constraintSolver = new SequentialImpulseConstraintSolver();
		world = new DiscreteDynamicsWorld(dispatcher, pairCache,
				constraintSolver, collisionConfiguration);

	}

	public void update(double t) {
		world.stepSimulation((float) t);
	}

	public void setGravity(Vector3 v) {
		world.setGravity(BulletConvert.convert(v, new Vector3f()));
	}

	public RigidBody addBox(Box b, double mass) {
		BoxShape boxShape = BulletConvert.createBoxShape(b);
		return addShape(b, mass, boxShape);
	}

	public RigidBody addSphere(Sphere s, double mass) {
		SphereShape sphereShape = BulletConvert.createSphereShape(s);
		return addShape(s, mass, sphereShape);
	}

	public RigidBody addShape(Spatial spatial, double mass, CollisionShape shape) {

		MotionState motionState = new BulletMotionState(spatial);

		boolean isDynamic = (mass != 0);
		Vector3f localInertia = new Vector3f(0, 0, 0);
		if (isDynamic) {
			shape.calculateLocalInertia((float) mass, localInertia);
		}

		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(
				(float) mass, motionState, shape, localInertia);
		RigidBody body = new RigidBody(info);

		body.setWorldTransform(body.getMotionState().getWorldTransform(
				new Transform()));
		world.addRigidBody(body);
		return body;
	}

	public List<CollisionObject> getCollisionObjects() {
		return world.getCollisionObjectArray();
	}

	public GhostObject addTrigger(SphereShape trigger) {
		GhostObject ob = new GhostObject();
		ob.setCollisionShape(trigger);
		world.addCollisionObject(ob);
		return ob;
	}



	public void add(GhostObject ob) {
		world.addCollisionObject(ob);
	}

	public void remove(GhostObject ghostObject) {
		world.removeCollisionObject(ghostObject);
	}

}
