package test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import processing.core.PApplet;

import com.baseoneonline.java.math.SimplifyDouglasPeucker;
import com.baseoneonline.java.math.Vec2f;

public class TestSimplifyLines extends PApplet
{

	private final ArrayList<Vec2f> points = new ArrayList<>();
	private Vec2f[] pts = null;

	private boolean mouseDown = false;

	private static int COLOR_DRAWING = 0xFFFFFF88;
	private static int COLOR_DRAWN = 0xFFFF8800;
	private static int COLOR_TEXT = 0xFFBBDDFF;

	private static int drawColor = COLOR_DRAWING;

	private float simplifyTolerance = 1;

	@Override
	public void setup()
	{
		size(500, 500);
	}

	@Override
	public void draw()
	{
		background(0x444444);
		stroke(drawColor);

		if (mouseDown)
			drawPolyLine();
		else
			drawOptimized();

		fill(COLOR_TEXT);
		text(String.format("Tolerance: %s", simplifyTolerance), 10, 20);
		text(String.format("Points drawn: %s", points.size()), 10, 40);
		if (null != pts)
			text(String.format("Optimized: %s", pts.length), 10, 60);

	}

	private void drawOptimized()
	{
		if (null == pts)
			pts = getOptimized();

		if (pts.length <= 1)
			return;

		for (int i = 1; i < pts.length; i++)
		{
			Vec2f a = pts[i - 1];
			Vec2f b = pts[i];
			line(a.x, a.y, b.x, b.y);
		}
	}

	private void drawPolyLine()
	{
		if (points.size() <= 1)
			return;

		for (int i = 1; i < points.size(); i++)
		{
			Vec2f a = points.get(i - 1);
			Vec2f b = points.get(i);
			line(a.x, a.y, b.x, b.y);
		}

	}

	private Vec2f[] getOptimized()
	{
		Vec2f[] pts = points.toArray(new Vec2f[points.size()]);
		if (pts.length < 2)
		{
			return pts;
		} else
		{
			return SimplifyDouglasPeucker
					.simplifyLine2D(simplifyTolerance, pts);
		}
	}

	private void changeTolerance(float n)
	{
		simplifyTolerance += n;
		if (simplifyTolerance < 0)
			simplifyTolerance = 0;
		pts = null;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{

		char c = e.getKeyChar();

		float mult = 1;

		if (e.isShiftDown())
			mult = 10;
		if (e.isControlDown())
			mult = .1f;

		switch (c)
		{
		case '+':
			changeTolerance(mult);
			break;
		case '-':
			changeTolerance(-mult);
			break;

		default:
			break;
		}
	}

	@Override
	public void mousePressed()
	{
		points.clear();
		drawColor = COLOR_DRAWING;
		mouseDown = true;
	}

	@Override
	public void mouseReleased()
	{
		drawColor = COLOR_DRAWN;
		mouseDown = false;
		pts = null;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		points.add(new Vec2f(e.getX(), e.getY()));
	}
}
