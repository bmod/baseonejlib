package com.baseoneonline.jlib.ardor3d.tools;

import java.util.logging.Logger;

import javax.vecmath.Vector3f;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.ui.text.BMText;
import com.baseoneonline.jlib.ardor3d.Line;
import com.baseoneonline.jlib.ardor3d.jbullet.BulletConvert;
import com.bulletphysics.linearmath.DebugDrawModes;
import com.bulletphysics.linearmath.IDebugDraw;

public class DebugDraw extends IDebugDraw {

	private int debugMode = DebugDrawModes.DRAW_CONTACT_POINTS
			| DebugDrawModes.DRAW_WIREFRAME;

	//
	// public static ColorRGBA collisionColor = new ColorRGBA(ColorRGBA.GREEN);
	//
	// private static WireBox box = new WireBox();
	//
	// public static void drawCollision(DynamicsWorld world, Renderer r) {
	// for (CollisionObject ob : world.getCollisionObjectArray()) {
	// drawShape(ob.getCollisionShape(), r);
	// }
	//
	// world.getDebugDrawer()
	// }
	//
	// public static void drawShape(CollisionShape shape, Renderer r) {
	// if (shape instanceof BoxShape) {
	// drawShape((BoxShape) shape);
	// }
	// throw new RuntimeException("Shape type not drawable: " + shape);
	// }
	//
	// private static void drawShape(BoxShape boxShape) {
	// box.setColor(collisionColor);
	// box.setData(boxShape.get, extents)
	// }

	private final Line line;
	private final Line point;
	private final ColorRGBA color = new ColorRGBA();
	private final Renderer renderer;
	private BMText text;

	private final Vector3[] verts;
	private final Vector3[] pointVertex;

	public DebugDraw(Renderer r) {
		renderer = r;

		verts = new Vector3[] { new Vector3(), new Vector3() };
		pointVertex = new Vector3[] { new Vector3() };

		line = new Line();
		point = new Line();
		point.setIndexMode(IndexMode.Points);
		point.setLineWidth(3);
	}

	@Override
	public void draw3dText(Vector3f location, String textString) {
	}

	@Override
	public void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB,
			float distance, int lifeTime, Vector3f color) {
		BulletConvert.convert(PointOnB, pointVertex[0]);
		BulletConvert.convert(color, this.color);
		point.setVertices(pointVertex);
		point.draw(renderer);
	}

	@Override
	public void drawLine(Vector3f from, Vector3f to, Vector3f color) {
		BulletConvert.convert(from, verts[0]);
		BulletConvert.convert(to, verts[1]);
		BulletConvert.convert(color, this.color);
		line.setVertices(verts);
		line.draw(renderer);
	}

	@Override
	public void reportErrorWarning(String warningString) {
		Logger.getLogger(getClass().getName()).warning(warningString);
	}

	@Override
	public void setDebugMode(int debugMode) {
		this.debugMode = debugMode;
	}

	@Override
	public int getDebugMode() {
		return debugMode;
	};

}
