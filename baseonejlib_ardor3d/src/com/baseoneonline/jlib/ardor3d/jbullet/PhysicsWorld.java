package com.baseoneonline.jlib.ardor3d.jbullet;

import java.util.logging.Logger;

import javax.vecmath.Vector3f;

import com.ardor3d.scenegraph.Mesh;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.IDebugDraw;

public class PhysicsWorld
{
	private final DefaultCollisionConfiguration collisionConfiguration;
	private final CollisionDispatcher dispatcher;
	private final DiscreteDynamicsWorld world;

	public PhysicsWorld()
	{
		final float worldSize = 10000;
		collisionConfiguration = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		final Vector3f worldAabbMin = new Vector3f(-worldSize, -worldSize,
				-worldSize);
		final Vector3f worldAabbMax = new Vector3f(worldSize, worldSize,
				worldSize);
		final AxisSweep3 overlappingPairCache = new AxisSweep3(worldAabbMin,
				worldAabbMax);
		final SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

		world = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache,
				solver, collisionConfiguration);
		world.setGravity(new Vector3f(0, -10, 0));
		world.getDispatchInfo().allowedCcdPenetration = 0f;

	}

	public void add(final CollisionObject collisionObject)
	{
		world.addCollisionObject(collisionObject);
	}

	public void update(final double t)
	{
		world.stepSimulation((float) t, 8);
	}

	public RigidBody add(Mesh floor)
	{
		PhysicsMotionState motionState = new PhysicsMotionState(floor);
		IndexedMesh iMesh = Converter.convert(floor);
		TriangleIndexVertexArray triangles = new TriangleIndexVertexArray();
		triangles.addIndexedMesh(iMesh);
		BvhTriangleMeshShape collisionShape = new BvhTriangleMeshShape(
				triangles, true);
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(1,
				motionState, collisionShape);
		RigidBody body = new RigidBody(info);

		world.addRigidBody(body);
		return body;
	}
}

class ArdorDebugDrawer extends IDebugDraw
{

	private static Logger LOG = Logger.getLogger(ArdorDebugDrawer.class
			.getName());

	@Override
	public void draw3dText(Vector3f location, String textString)
	{

	}

	@Override
	public void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB,
			float distance, int lifeTime, Vector3f color)
	{

	}

	@Override
	public void drawLine(Vector3f from, Vector3f to, Vector3f color)
	{

	}

	@Override
	public int getDebugMode()
	{
		return 0;
	}

	@Override
	public void reportErrorWarning(String warningString)
	{
		LOG.warning(warningString);
	}

	@Override
	public void setDebugMode(int debugMode)
	{

	}

}
