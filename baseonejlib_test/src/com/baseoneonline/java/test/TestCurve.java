package com.baseoneonline.java.test;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.baseoneonline.java.math.Curve;
import com.baseoneonline.java.math.NurbsCurve;
import com.baseoneonline.java.math.Vec2f;

public class TestCurve extends Applet {

	// public static void main(final String[] args) {
	// final TestCurve frame = new TestCurve();
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// frame.setSize(450, 350);
	// frame.setVisible(true);
	// }

	CurvePanel cvPanel = new CurvePanel();
	private Vec2f[] pts;

	@Override
	public void init() {

		setLayout(new BorderLayout());

		add(cvPanel);

		JPanel sidePanel = new JPanel();
		{
			BoxLayout box = new BoxLayout(sidePanel, BoxLayout.Y_AXIS);
			sidePanel.setLayout(box);

			final JSlider lodSlider = new JSlider(JSlider.HORIZONTAL);
			lodSlider.setValue(cvPanel.getLevelOfDetail());
			lodSlider.setMaximum(500);
			lodSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent ev) {
					cvPanel.setLevelOfDetail(lodSlider.getValue());
					cvPanel.repaint();
				}
			});
			sidePanel.add(lodSlider);

		}
		add(sidePanel, BorderLayout.EAST);

		pts = createCurve();
		NurbsCurve curve = new NurbsCurve(pts);
		cvPanel.setCurve(curve);

	}

	private Vec2f[] createCurve() {
		int len = 7;
		Vec2f[] pts = new Vec2f[len];
		for (int i = 0; i < len; i++) {
			pts[i] = new Vec2f(rand(200), rand(200));
		}
		return pts;
	}

	private float rand(float n) {
		return (float) Math.random() * n;
	}

}

class CurvePanel extends JPanel {
	private final ArrayList<Handle> handles = new ArrayList<Handle>();
	float handleSize = 3;
	private Handle selectedHandle = null;
	int selectedPoint = -1;
	private Curve curve;
	private int levelOfDetail = 64;
	private Path2D path;

	public CurvePanel() {
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		repaint();
	}

	public int getLevelOfDetail() {
		return levelOfDetail;
	}

	public void setLevelOfDetail(int lod) {
		if (lod < 2)
			lod = 2;
		this.levelOfDetail = lod;
		path = new Path2D.Float(Path2D.WIND_EVEN_ODD, lod);
	}

	public void setCurve(Curve curve) {
		this.curve = curve;
		setLevelOfDetail(getLevelOfDetail());
		refreshHandles();
		repaint();
	}

	public Curve getCurve() {
		return curve;
	}

	private void refreshHandles() {
		handles.clear();
		selectedHandle = null;
		for (int i = 0; i < curve.getNumCVs(); i++) {
			Vec2f cv = curve.getCV(i);
			Handle h = new Handle(cv);
			handles.add(h);
		}
	}

	@Override
	public void paint(final Graphics g1) {
		final Graphics2D g = (Graphics2D) g1;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Draw handles
		g.setColor(Color.BLACK);
		for (final Handle h : handles) {
			h.draw(g);
		}

		g.setColor(Color.BLUE);

		if (null != curve) {
			// Draw Curve
			path.reset();
			final Vec2f point = curve.getPoint(0, null);
			path.moveTo(point.x, point.y);
			float t;
			for (int i = 1; i <= levelOfDetail; i++) {
				t = i / (float) levelOfDetail;
				curve.getPoint(t, point);

				path.lineTo(point.x, point.y);
			}
			g.draw(path);
		} else {
			g.drawString("No curve", 20, 20);
		}
	}

	private Handle getHandleAt(final Point2D p) {
		for (Handle h : handles) {
			if (h.contains(p))
				return h;
		}
		return null;
	}

	private final MouseAdapter mouseAdapter = new MouseAdapter() {

		Point mpos = new Point();
		Point oldPos = new Point();
		Point delta = new Point();

		@Override
		public void mousePressed(final MouseEvent e) {
			mpos = e.getPoint();
			selectedHandle = getHandleAt(mpos);
			oldPos = mpos;
		};

		@Override
		public void mouseDragged(final MouseEvent e) {
			if (null != selectedHandle) {
				mpos = e.getPoint();
				delta.x = mpos.x - oldPos.x;
				delta.y = mpos.y - oldPos.y;
				selectedHandle.translate(delta.x, delta.y);
				repaint();
				oldPos = mpos;
			}
		}
	};

}

class Handle {

	private final Vec2f cv;
	Rectangle2D rect = new Rectangle2D.Float();

	private static float size = 5;
	private static float pickSize = 15;

	public Handle(Vec2f cv) {
		this.cv = cv;
		updateRectangle();
	}

	public boolean contains(Point2D p) {
		return p.getX() > cv.x - pickSize / 2 && p.getX() < cv.x + pickSize / 2
				&& p.getY() > cv.y - pickSize / 2
				&& p.getY() < cv.y + pickSize / 2;
	}

	public void translate(float x, float y) {
		cv.x += x;
		cv.y += y;
		updateRectangle();
	}

	private void updateRectangle() {
		rect.setRect(cv.x - size / 2, cv.y - size / 2, size, size);
	}

	public void draw(Graphics2D g) {
		g.draw(rect);
	}

}
