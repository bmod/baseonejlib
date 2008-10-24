package com.baseoneonline.java.curveEditor;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.baseoneonline.java.curveEditor.core.Curve;
import com.baseoneonline.java.curveEditor.core.Point;

class CVPanel extends Canvas {

	private static final long serialVersionUID = 8259577516462336875L;

	private final Curve curve;

	private BufferedImage buffer;

	private final float margin = 20;

	private final float dotPickSize = 10;

	private int selectedDot = -1;

	private final Rectangle viewPort = new Rectangle(100, 100);

	public CVPanel() {

		final Point[] pts = { createPoint(), createPoint(), createPoint(),
				createPoint(), createPoint(), createPoint(), createPoint() };

		curve = new Curve();

		resize();
		addComponentListener(componentListener);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotionListener);
		addKeyListener(keyListener);
	}

	private Point createPoint() {
		final float scale = 100;
		return new Point((float) Math.random() * scale, (float) Math.random()
				* scale);
	}

	@Override
	public void update(final Graphics g1) {
		final Graphics2D g = (Graphics2D) buffer.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.red);
		g.setStroke(new BasicStroke(1));

		final float dotSize = 5;

		final List<Point> pts = curve.getPoints();
		if (pts.size() > 0) {
			Point p1 = pts.get(0);

			for (int i = 0; i < pts.size(); i++) {
				final Point p = pts.get(i);

				if (i == selectedDot) {
					drawSelectedPoint(g, p.x, p.y);
				} else {
					drawPoint(g, p.x, p.y);
				}

				if (i > 0) {
					g.setColor(Color.black);
					final Shape s = new Line2D.Float(p.toPoint2D(), p1
							.toPoint2D());
					g.draw(s);
				}
				p1 = p;
			}
		}
		g1.drawImage(buffer, 0, 0, null);
	}

	private void drawPoint(final Graphics2D g, final float x, final float y) {
		g.setColor(Color.LIGHT_GRAY);
		g.setStroke(new BasicStroke(1.5f));
		final float dotSize = 4;
		final Shape dot = new Rectangle2D.Float(x - (dotSize / 2), y
				- (dotSize / 2), dotSize, dotSize);
		g.draw(dot);
	}

	private void drawSelectedPoint(final Graphics2D g, final float x,
			final float y) {
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(2));
		final float dotSize = 6;
		final Shape dot = new Rectangle2D.Float(x - (dotSize / 2), y
				- (dotSize / 2), dotSize, dotSize);
		g.draw(dot);
	}

	private void addPoint(final java.awt.Point p) {
		curve.addPoint(new Point(p.x, p.y));
	}

	private void trySelectPoint(final java.awt.Point p) {
		final float ps = dotPickSize / 2;
		for (int i = 0; i < curve.getPoints().size(); i++) {
			final Point p1 = curve.getPoints().get(i);
			if (p.x > p1.x - ps && p.x < p1.x + ps && p.y > p1.y - ps
					&& p.y < p1.y + ps) {
				selectedDot = i;
				return;
			}
		}
		selectedDot = -1;

	}

	@Override
	public void paint(final Graphics g) {}

	private void resize() {
		int w = getWidth();
		int h = getHeight();
		if (w <= 0) {
			w = 1;
		}
		if (h <= 0) {
			h = 1;
		}
		buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		viewPort.setSize(getWidth(), getHeight());

	}

	public boolean hasSelection() {
		return selectedDot != -1;
	}

	private final MouseListener mouseListener = new MouseListener() {

		public void mouseClicked(final MouseEvent e) {}

		public void mouseEntered(final MouseEvent arg0) {}

		public void mouseExited(final MouseEvent arg0) {
		// System.out.println("Mouse Out");
		}

		public void mousePressed(final MouseEvent e) {
			trySelectPoint(e.getPoint());
			System.out.println(selectedDot);
			if (hasSelection()) {

			} else {
				addPoint(e.getPoint());
			}
			repaint();
		}

		public void mouseReleased(final MouseEvent arg0) {}
	};

	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {

		public void mouseDragged(final MouseEvent e) {
			if (hasSelection()) {
				final Point p = curve.getPoints().get(selectedDot);
				p.x = e.getX();
				p.y = e.getY();
				repaint();
			}
		}

		public void mouseMoved(final MouseEvent e) {}

	};

	private final ComponentListener componentListener = new ComponentListener() {

		public void componentResized(final ComponentEvent e) {
			resize();
			repaint();
		}

		public void componentHidden(final ComponentEvent e) {}

		public void componentMoved(final ComponentEvent e) {}

		public void componentShown(final ComponentEvent e) {}
	};

	private final KeyListener keyListener = new KeyListener() {

		public void keyPressed(final KeyEvent e) {}

		public void keyReleased(final KeyEvent e) {}

		public void keyTyped(final KeyEvent e) {
			if (KeyEvent.VK_BACK_SPACE == e.getKeyCode()) {
				System.out.println("DEL");
				curve.removePoint(selectedDot);
				repaint();
			}
		}

	};

}
