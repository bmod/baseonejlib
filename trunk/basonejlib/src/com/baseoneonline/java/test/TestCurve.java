package com.baseoneonline.java.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestCurve extends JPanel {

	public static void main(final String[] args) {
		final JFrame frame = new JFrame("TestCurve");
		frame.setLayout(new BorderLayout());
		frame.add(new TestCurve());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(450, 350);
		frame.setVisible(true);
	}

	private final ArrayList<Rectangle2D> handles = new ArrayList<Rectangle2D>();
	float handleSize = 3;
	int selectedPoint = -1;
	Curve curve;

	public TestCurve() {
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setBackground(Color.white);

		final int num = 7;

		final float w = 400;
		final float h = 300;
		float x = 0;
		float y = 0;

		final Vec2f[] pts = new Vec2f[num];
		for (int i = 0; i < num; i++) {
			x = (float) (Math.random() * w);
			y = (float) (Math.random() * h);
			final Rectangle2D rect = new Rectangle2D.Float(x, y,
					handleSize * 2, handleSize * 2);
			handles.add(rect);
			pts[i] = new Vec2f((float) rect.getCenterX(), (float) rect
					.getCenterY());
		}
		curve = new Curve(pts);

		repaint();
	}

	@Override
	public void paint(final Graphics g1) {
		final Graphics2D g = (Graphics2D) g1;
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Draw handles
		g.setColor(Color.blue);
		for (final Rectangle2D r : handles) {
			g.draw(r);
		}

		// Draw Curve
		final int precision = 50;
		final Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD, precision);
		final Vec2f v = curve.getPoint(0, null);
		path.moveTo(v.x, v.y);
		for (int i = 1; i <= precision; i++) {
			curve.getPoint((float) i / (float) precision, v);
			path.lineTo(v.x, v.y);
		}
		g.draw(path);
	}

	private int getHandleAt(final Point2D p) {
		for (int i = 0; i < handles.size(); i++) {
			if (handles.get(i).contains(p)) return i;
		}
		return -1;
	}

	private void moveHandle(final int h, final Point2D e) {
		handles.get(selectedPoint).setFrameFromCenter(e.getX(), e.getY(),
				e.getX() + handleSize, e.getY() + handleSize);
	}

	private final MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(final MouseEvent e) {
			selectedPoint = getHandleAt(e.getPoint());
		};

		@Override
		public void mouseDragged(final MouseEvent e) {
			if (-1 != selectedPoint) {
				moveHandle(selectedPoint, e.getPoint());
				curve.getCV(selectedPoint).x = e.getPoint().x;
				curve.getCV(selectedPoint).y = e.getPoint().y;
				repaint();
			}
		}
	};

}

class Vec2f {

	public float x;
	public float y;

	public Vec2f(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2f() {
		x = 0;
		y = 0;
	}

	public Vec2f mult(final float n) {
		return new Vec2f(x * n, y * n);
	}

	public void zero() {
		x = 0;
		y = 0;
	}

	public void addLocal(final Vec2f v) {
		x += v.x;
		y += v.y;
	}

	public void set(final Vec2f v) {
		x = v.x;
		y = v.y;
	}
}

class Curve {

	private final Vec2f[] pts;
	private final int degree;
	private final int order;
	private final int num_knots;
	private float[] knots;

	public Curve(final Vec2f[] pts) {
		this.pts = pts;
		degree = 3;
		order = degree + 1;
		num_knots = pts.length + order;
		createKnots();
	}

	private void createKnots() {
		knots = new float[num_knots];
		float v = 0;
		for (int i = 0; i < num_knots; i++) {
			knots[i] = v;
			if (i >= degree && i < num_knots - degree - 1) {
				v += 1;
			}

		}
	}

	public Vec2f getCV(final int i) {
		return pts[i];
	}

	public Vec2f getPoint(final float mu, Vec2f store) {
		if (null == store) store = new Vec2f();
		if (mu >= 1) {
			store.set(pts[pts.length - 1]);
			return store;
		}
		if (mu <= 0) {
			store.set(pts[0]);
			return store;
		}
		store.zero();
		for (int i = 0; i < pts.length; i++) {
			final float v = CoxDeBoor(mu * pts.length, i, order);
			if (v > 0.00001f) {
				store.addLocal(pts[i].mult(v));
			}
		}
		return store;
	}

	private float CoxDeBoor(final float u, final int i, final int k) {
		if (k == 1) {
			if (knots[i] <= u && u <= knots[i + 1]) { return 1.0f; }
			return 0.0f;
		}
		final float Den1 = knots[i + k - 1] - knots[i];
		final float Den2 = knots[i + k] - knots[i + 1];
		float Eq1 = 0, Eq2 = 0;
		if (Den1 > 0) {
			Eq1 = ((u - knots[i]) / Den1) * CoxDeBoor(u, i, k - 1);
		}
		if (Den2 > 0) {
			Eq2 = (knots[i + k] - u) / Den2 * CoxDeBoor(u, i + 1, k - 1);
		}
		return Eq1 + Eq2;
	}

}
