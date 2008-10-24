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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Vector;

import com.baseoneonline.java.curveEditor.core.Curve;
import com.baseoneonline.java.curveEditor.core.Point;

class CVPanel extends Canvas {

	private static final long serialVersionUID = 8259577516462336875L;

	private final List<Curve> curves = new Vector<Curve>();

	private BufferedImage buffer;

	private final float margin = 20;

	private final Rectangle viewPort = new Rectangle(100, 100);

	Curve curve;

	public CVPanel() {

		final Point[] pts = { createPoint(), createPoint(), createPoint(),
				createPoint(), createPoint(), createPoint(), createPoint() };

		curve = new Curve(pts);
		curves.add(curve);

		resize();
		addComponentListener(componentListener);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotionListener);
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

		for (final Curve c : curves) {
			drawCurveLinear(g, c);
		}

		g1.drawImage(buffer, 0, 0, null);
	}

	private void drawCurveLinear(final Graphics2D g, final Curve c) {
		final Point[] pts = c.getPoints();
		Point p1 = pts[0];
		for (int i = 1; i < pts.length; i++) {
			final Point p = pts[i];

			final Shape s = new Line2D.Float(p.toPoint2D(), p1.toPoint2D());
			g.draw(s);

			p1 = p;
		}
	}

	private Point transformPoint(final Point p) {
		final Point p1 = new Point();

		return p1;
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

	private final MouseListener mouseListener = new MouseListener() {

		public void mouseClicked(final MouseEvent e) {}

		public void mouseEntered(final MouseEvent arg0) {}

		public void mouseExited(final MouseEvent arg0) {
			System.out.println("Mouse Out");
		}

		public void mousePressed(final MouseEvent arg0) {
			System.out.println("Click");
		}

		public void mouseReleased(final MouseEvent arg0) {}
	};

	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {

		public void mouseDragged(final MouseEvent e) {
			System.out.println("Drag "+e.getButton());
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
}
