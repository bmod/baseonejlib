package com.baseoneonline.java.test;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;

public class TestGTGE extends Game {

	public static void main(String[] args) {
		GameLoader game = new GameLoader();
		game.setup(new TestGTGE(), new Dimension(640, 480), false);
		game.start();
	}

	private Bubble bubble;

	@Override
	public void initResources() {
		setFPS(60);
		bubble = new Bubble();
	}

	@Override
	public void render(Graphics2D g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		bubble.draw(g);
	}

	@Override
	public void update(long elapsedTime) {

	}

}

interface Drawable {
	public void draw(Graphics2D g);
}

class NGon implements Drawable {

	GeneralPath path;

	public NGon(int sides, float radius) {
		float a = 0;
		float x = (float) (Math.cos(0) * radius);
		float y = (float) (Math.sin(0) * radius);
		path = new GeneralPath();
		path.moveTo(x, y);
		for (int i = 1; i < sides; i++) {
			a = (float) i / (float) sides * (float) Math.PI * 2;
			x = (float) (Math.cos(a) * radius);
			y = (float) (Math.sin(a) * radius);
			path.lineTo(x, y);
		}
		path.closePath();
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.draw(path);
	}
}

class Bubble {

	Point2D[] points;

	public Bubble() {
		int segs = 16;
		float radius = 100;

		points = new Point2D[segs];
		for (int i = 0; i < segs; i++) {
			float a = (float) i / (float) segs * (float) Math.PI * 2;
			points[i] = new Point2D.Float((float) Math.cos(a) * radius,
					(float) Math.sin(a) * radius);

		}

	}

	public void draw(Graphics2D g) {
		float dev = 5;
		for (int i = 0; i < points.length - 1; i++) {
			Point2D p1 = points[i];
			Point2D p2 = points[i + 1];
			g.drawLine((int) (p1.getX() + Math.random() * dev), (int) (p1
					.getY() + Math.random() * dev), (int) (p2.getX() + Math
					.random()
					* dev), (int) (p2.getY() + Math.random() * dev));
		}
	}

}
