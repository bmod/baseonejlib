package com.baseoneonline.java.test;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;

public class TestJBox2D extends SimpleGame {

	public static void main(final String[] args) {
		final TestJBox2D app = new TestJBox2D();
		app.start();
	}

	ArrayList<PhysObj> objects = new ArrayList<PhysObj>();
	World world;

	@Override
	protected void simpleInitGame() {
		final AABB aabb = new AABB(new Vec2(-1000, -1000), new Vec2(1000, 1000));
		world = new World(aabb, new Vec2(0, .1f), true);

		createBox(-10, 0, 1, 20);
		createBox(10, 0, 1, 20);
		createBox(0, -10, 20, 1);
		createBox(0, 10, 20, 1);

		createBall(0, 0, 1);

		cam.setUp(new Vector3f(0, -1, 0));

	}

	private void createBox(final float x, final float y, final float w,
			final float h) {
		final PhysObj obj = new PhysObj();
		obj.bodyDef = new BodyDef();

		obj.body = world.createBody(obj.bodyDef);
		obj.body.setXForm(new Vec2(x, y), 0);

		final PolygonDef shape = new PolygonDef();
		shape.setAsBox(w / 2, h / 2);
		shape.density = 1;
		obj.body.createShape(shape);
		obj.body.setMassFromShapes();
		obj.spatial = new Box("box", new Vector3f(), w, h, 1);
		rootNode.attachChild(obj.spatial);
		objects.add(obj);
	}

	private void createBall(final float x, final float y, final float radius) {
		final PhysObj obj = new PhysObj();
		obj.bodyDef = new BodyDef();

		obj.body = world.createBody(obj.bodyDef);
		obj.body.setXForm(new Vec2(x, y), 0);
		final CircleDef shape = new CircleDef();

		shape.density = 1;
		shape.radius = radius;
		obj.body.createShape(shape);
		obj.body.setMassFromShapes();
		obj.spatial = new Sphere("dynSphere", 8, 8, radius);
		rootNode.attachChild(obj.spatial);
		objects.add(obj);

	}

	@Override
	protected void simpleUpdate() {
		world.step(.1f, 1);
		for (final PhysObj o : objects) {
			final Vec2 v = o.body.getPosition();
			o.spatial.setLocalTranslation(v.x, v.y, 0);
			System.out.println(v);
		}
	}
}

class PhysObj {

	public BodyDef bodyDef;
	public Body body;
	public Spatial spatial;
}
