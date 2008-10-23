package com.baseoneonline.java.curveEditor;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Vector;

import com.baseoneonline.java.curveEditor.core.Bounds;
import com.baseoneonline.java.curveEditor.core.Curve;
import com.baseoneonline.java.curveEditor.core.Point;

class CVPanel extends Canvas {

	private static final long serialVersionUID = 8259577516462336875L;

	private final List<Curve> curves = new Vector<Curve>();

	private BufferedImage buffer;

	public CVPanel() {
		resizeBuffer();
		addComponentListener(componentAdapter);
		addMouseListener(mouseAdapter);
	}

	public void addCurve(final Curve c) {
		curves.add(c);
	}

	@Override
	public void update(final Graphics g1) {
		final Graphics2D g = (Graphics2D) buffer.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		final Bounds bounds = findBounds();
		final float width = bounds.getWidth();
		final float height = bounds.getHeight();

		for (final Curve c : curves) {
			for (int i = 0; i < c.size(); i++) {
				final Point p = c.getPoint(i);
				final float x = (p.x - bounds.left) * width;
				final float y = (p.y - bounds.top) * height;

				final float size = 2;
				final float s = size / 2;

				final Shape shape = new Ellipse2D.Float(x - s, y - s, x + s, y + s);
				g.setColor(Color.red);
				g.setStroke(new BasicStroke(1));
				g.draw(shape);
			}
		}

		g.setStroke(new BasicStroke(2));
		g.setColor(Color.black);
		g.drawLine(5, 10, 200, 300);



		g1.drawImage(buffer, 0, 0, null);
	}

	@Override
	public void paint(final Graphics g) {}

	private void resizeBuffer() {
		int w = getWidth();
		int h = getHeight();
		if (w <= 0) {
			w = 1;
		}
		if (h <= 0) {
			h = 1;
		}
		buffer = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
	}

	private Bounds findBounds() {
		final Bounds b = new Bounds();
		for (final Curve c : curves) {
			b.merge(c.getBounds());
		}
		return b;
	}

	MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(final MouseEvent e) {}
	};

	ComponentAdapter componentAdapter = new ComponentAdapter() {
		@Override
		public void componentResized(final ComponentEvent e) {
			resizeBuffer();
			repaint();
		}
	};
}
