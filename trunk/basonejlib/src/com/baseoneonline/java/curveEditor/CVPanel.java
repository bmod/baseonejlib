package com.baseoneonline.java.curveEditor;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;

import com.baseoneonline.java.curveEditor.core.Curve;
import com.baseoneonline.java.curveEditor.core.Point;
import com.baseoneonline.java.jlib.utils.Config;

class CVPanel extends Canvas {

	Config conf = Config.getConfig("CVPanel.cfg");

	private static final long serialVersionUID = 8259577516462336875L;

	private float margin;
	private float pointPickSize;
	private float pointHandleSize;
	private float pointHandleSelectedSize;
	private float curveStrokeWidth;
	private Color pointHandleColor;
	private Color pointHandleSelectedColor;
	private Color curveStrokeColor;

	private final Curve curve;
	private BufferedImage buffer;
	private Point selectedPoint = null;

	private final ActionMap actionMap = new ActionMap();

	private final Rectangle viewPort = new Rectangle(100, 100);

	public CVPanel() {

		setProperties();

		curve = new Curve();

		resize();
		addComponentListener(componentListener);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseMotionListener);
		addKeyListener(keyListener);
	}

	private void setProperties() {



		actionMap.put(KeyEvent.VK_DELETE, deleteSelectedPointsAction);
		actionMap.put(KeyEvent.VK_BACK_SPACE, deleteSelectedPointsAction);


		margin = conf.get("margin", 20f);
		pointPickSize = conf.get("pointPickSize", 8f);
		pointHandleSize = conf.get("pointHandleSize", 5f);
		pointHandleSelectedSize = conf.get("pointHandleSelectedSize", 5f);
		pointHandleColor = new Color(conf.get("pointHandleColor", 0x000000));
		pointHandleSelectedColor = new Color(conf.get("pointHandeSelectedColor", 0xFF0000));
		curveStrokeColor = new Color(conf.get("curveStrokeColor", 0x000000));
		curveStrokeWidth = conf.get("curveStrokeWidth", .7f);
	}


	@Override
	public void update(final Graphics g1) {
		final Graphics2D g = (Graphics2D) buffer.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (curve.size() > 0) {
			Point p1 = curve.getPoint(0);

			for (int i = 0; i < curve.size(); i++) {
				final Point p = curve.getPoint(i);

				if (p == selectedPoint) {
					drawSelectedPoint(g, p.x, p.y);
				} else {
					drawPointHandle(g, p.x, p.y);
				}

				if (i > 0) {
					g.setColor(Color.black);
					g.setStroke(new BasicStroke(.5f));
					final Shape s = new Line2D.Float(p.toPoint2D(), p1
							.toPoint2D());
					g.draw(s);
				}
				p1 = p;
			}
		}
		g1.drawImage(buffer, 0, 0, null);
	}

	private void reorderPoints(final Curve c) {

		Collections.sort(curve.getPoints(), new Comparator<Point>() {
			public int compare(final Point o1, final Point o2) {
				if (o1.x < o2.x) {
					return -1;
				}
				if (o1.x > o2.x) {
					return 1;
				}
				return 0;
			}
		});
	}

	private void drawPointHandle(final Graphics2D g, final float x,
			final float y) {
		g.setColor(pointHandleColor);
		g.setStroke(new BasicStroke(1));
		final Shape dot = new Rectangle2D.Float(x - (pointHandleSize / 2), y
				- (pointHandleSize / 2), pointHandleSize, pointHandleSize);
		g.draw(dot);
	}

	private void drawSelectedPoint(final Graphics2D g, final float x,
			final float y) {
		g.setColor(pointHandleSelectedColor);
		g.setStroke(new BasicStroke(1));
		final Shape dot = new Rectangle2D.Float(x - (pointHandleSelectedSize / 2), y
				- (pointHandleSelectedSize / 2), pointHandleSelectedSize, pointHandleSelectedSize);
		g.draw(dot);
	}

	private void addPoint(final java.awt.Point p) {
		curve.addPoint(new Point(p.x, p.y));
	}

	private void trySelectPoint(final java.awt.Point p) {
		final float ps = pointPickSize / 2;
		for (int i = 0; i < curve.size(); i++) {
			final Point p1 = curve.getPoint(i);
			if ((p.x > p1.x - ps) && (p.x < p1.x + ps) && (p.y > p1.y - ps)
					&& (p.y < p1.y + ps)) {
				selectedPoint = p1;
				return;
			}
		}
		selectedPoint = null;

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
		return null != selectedPoint;
	}

	private final Action deleteSelectedPointsAction = new AbstractAction() {
		public void actionPerformed(final ActionEvent e) {
			if (hasSelection()) {
				curve.removePoint(selectedPoint);
				selectedPoint = null;
				reorderPoints(curve);
				repaint();
			}
		}
	};

	private final Action addPointAction = new AbstractAction() {
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub

		}
	};

	private final MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mousePressed(final MouseEvent e) {
			trySelectPoint(e.getPoint());
			System.out.println(selectedPoint);
			if (hasSelection()) {

			} else {
				addPoint(e.getPoint());
				reorderPoints(curve);
			}
			repaint();
		}

	};

	private final MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(final MouseEvent e) {
			if (hasSelection()) {
				selectedPoint.set(e.getX(),e.getY());
				reorderPoints(curve);
				repaint();
			}
		}
	};

	private final ComponentListener componentListener = new ComponentAdapter() {
		@Override
		public void componentResized(final ComponentEvent e) {
			resize();
			repaint();
		}
	};

	private final KeyListener keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(final KeyEvent e) {
			final int key = e.getKeyCode();

		}
	};

}
