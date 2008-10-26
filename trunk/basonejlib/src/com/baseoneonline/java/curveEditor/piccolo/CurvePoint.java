package com.baseoneonline.java.curveEditor.piccolo;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

public class CurvePoint extends PNode {

	private final double size = 2;
	private final Rectangle2D rect = new Rectangle2D.Double(-size/2, -size/2, size, size);

	public CurvePoint() {
		super();
	}

	@Override
	public boolean setBounds(final double x, final double y, final double width, final double height) {
		if (super.setBounds(x, y, width, height)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean intersects(final Rectangle2D localBounds) {
		return rect.intersects(localBounds);
	}

	@Override
	protected void paint(final PPaintContext paintContext) {
		final Graphics2D g = paintContext.getGraphics();
		g.setPaint(getPaint());
		g.fill(rect);
	}


}
