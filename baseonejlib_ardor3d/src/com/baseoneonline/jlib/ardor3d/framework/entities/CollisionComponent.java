package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;

import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;
import com.baseoneonline.jlib.ardor3d.framework.PhysicsManager;
import com.baseoneonline.jlib.ardor3d.jbullet.BulletConvert;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;

public class CollisionComponent extends Component {

	private final CollisionShape shape = new SphereShape(1);
	private final GhostObject ghostObject = new GhostObject();
	private final Transform ghostTransform = new Transform();

	private int collisionMask = 1;

	private int collisionGroup = 1;

	public CollisionComponent() {

	}

	public CollisionComponent(final int group, final int filter) {
		collisionGroup = group;
		collisionMask = filter;
	}

	@Override
	public void write(final OutputCapsule capsule) throws IOException {
		capsule.write(collisionGroup, "group", 1);
		capsule.write(collisionMask, "mask", 1);
	}

	@Override
	public void read(final InputCapsule capsule) throws IOException {
		collisionGroup = capsule.readInt("group", 1);
		collisionMask = capsule.readInt("mask", 1);
	}

	@Override
	public void update(final double t) {
		ghostObject.setWorldTransform(BulletConvert.convert(getEntity()
				.getNode().getWorldTransform(), ghostTransform));

	}

	@Override
	public void resume() {
		ghostObject.setCollisionShape(shape);
		PhysicsManager.get().add(this);
	}

	@Override
	public void suspend() {
		PhysicsManager.get().remove(this);
	}

	public int getCollisionGroup() {
		return collisionGroup;
	}

	public int getCollisionMask() {
		return collisionMask;
	}

	public CollisionObject getCollisionObject() {
		return ghostObject;
	}

}
