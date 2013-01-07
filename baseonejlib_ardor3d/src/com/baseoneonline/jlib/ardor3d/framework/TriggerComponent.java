package com.baseoneonline.jlib.ardor3d.framework;

import java.io.IOException;

import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;
import com.baseoneonline.jlib.ardor3d.jbullet.BulletConvert;
import com.baseoneonline.jlib.ardor3d.jbullet.PhysicsWorld;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;

public class TriggerComponent extends Component {

	private final GhostObject ghostObject = new GhostObject();
	private final Transform ghostTransform = new Transform();

	public TriggerComponent() {
		setShape(new SphereShape(1));
	}

	public void setToSphereShape(double radius) {
		setShape(new SphereShape((float) radius));
	}

	public void setShape(CollisionShape shape) {
		ghostObject.setCollisionShape(shape);
	}

	@Override
	public void update(double t) {
		ghostObject.setWorldTransform(BulletConvert.convert(getOwner()
				.getNode().getWorldTransform(), ghostTransform));
	}

	@Override
	public void onAdded() {
		PhysicsWorld.get().add(ghostObject);
	}

	@Override
	public void onRemoved() {
		PhysicsWorld.get().remove(ghostObject);
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
	}

}
