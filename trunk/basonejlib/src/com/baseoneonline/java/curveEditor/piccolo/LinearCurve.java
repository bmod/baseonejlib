package com.baseoneonline.java.curveEditor.piccolo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import com.baseoneonline.java.curveEditor.core.Curve;
import com.baseoneonline.java.curveEditor.core.Point;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

public class LinearCurve extends PNode {

	private static final long serialVersionUID = -2274712655379487932L;

	private final Curve curve;

	public LinearCurve(final Curve curve) {
		super();
		this.curve = curve;
		updateFromCurve();
	}

	private void updateFromCurve() {
		removeAllChildren();
		for (int i=0; i<curve.size(); i++) {
			final Point p = curve.getPoint(i);
			final CurvePoint pt = new CurvePoint();
			pt.translate(p.x, p.y);
			addChild(pt);
		}
	}


	@Override
	protected void paint(final PPaintContext paintContext) {
		final Graphics2D g = paintContext.getGraphics();
		Point p1 = null;
		Point p2 = null;

		for (int i=0; i<curve.size(); i++) {
			if (i > 1) {
				p2 = curve.getPoint(i);
				final Line2D l = new Line2D.Double(p1.toPoint2D(),p2.toPoint2D());
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(1));
				g.draw(l);
			}
			p1 = p2;
		}
	}



}
