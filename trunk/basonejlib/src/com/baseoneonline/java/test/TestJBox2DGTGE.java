package com.baseoneonline.java.test;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.DebugDraw;
import org.jbox2d.dynamics.World;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;

public class TestJBox2DGTGE extends Game {

	public static void main(String[] args) {
		GameLoader ldr = new GameLoader();
		TestJBox2DGTGE app = new TestJBox2DGTGE();
		ldr.setup(app, new Dimension(500, 400), false);

		ldr.start();
	}

	World world;
	G2DDraw draw;

	@Override
	public void initResources() {
		setFPS(60);
		
		draw = new G2DDraw();

		AABB aabb = new AABB(new Vec2(-10000, -10000), new Vec2(10000, 10000));
		Vec2 gravity = new Vec2(0, .1f);
		world = new World(aabb, gravity, true);

		BodyDef def = new BodyDef();
		PolygonDef pdef = new PolygonDef();
		pdef.setAsBox(50, 40);
		Body body = world.createBody(def);
		body.createShape(pdef);

		world.setDebugDraw(draw);
	}

	@Override
	public void render(Graphics2D g) {
		g.clearRect(0, 0, getWidth(), getHeight());

		draw.setGraphics(g);
		System.out.println(draw.g);
		world.drawDebugData();
	}

	@Override
	public void update(long l) {
		world.step(l, 1);
	}

}

class G2DDraw extends DebugDraw {

	Graphics2D g;
	float pointSize = 1;

	@Override
	public void drawCircle(Vec2 center, float radius, Color3f color) {
		Ellipse2D ellipse =
			new Ellipse2D.Float(center.x - radius, center.y - radius, center.x
				+ radius, center.y + radius);
		draw(ellipse);
	}

	public void setGraphics(Graphics2D g2) {
		if (g2 == null) throw new NullPointerException("NUKLLL");
		this.g = g2;
	}

	@Override
	public void drawPoint(Vec2 position, float f, Color3f color3f) {
		Rectangle2D rect =
			new Rectangle2D.Float(position.x - pointSize, position.y
				- pointSize, position.x + pointSize, position.y + pointSize);
		draw(rect);
	}

	@Override
	public void drawPolygon(Vec2[] vertices, int vertexCount, Color3f color) {
		if (vertexCount < 2)
			return;
		GeneralPath path = new GeneralPath();
		path.moveTo(vertices[0].x, vertices[0].y);
		for (int i = 1; i < vertexCount; i++) {
			path.lineTo(vertices[i].x, vertices[i].y);
		}
		draw(path);
	}

	@Override
	public void drawSegment(Vec2 p1, Vec2 p2, Color3f color) {
		Line2D line = new Line2D.Float(p1.x, p1.y, p2.x, p2.y);
		draw(line);
	}

	@Override
	public void drawSolidCircle(Vec2 center, float radius, Vec2 axis,
			Color3f color) {
		drawCircle(center, radius, color);
	}

	@Override
	public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color) {
		drawPolygon(vertices, vertexCount, color);
	}

	@Override
	public void drawString(float x, float y, String s, Color3f color) {
		if (null != g) g.drawString(s, x, y);
	}

	@Override
	public void drawXForm(XForm xf) {
		
	}
	
	private void draw(Shape s) {
		if (null != g) {
			g.draw(s);
		}
	}

}
