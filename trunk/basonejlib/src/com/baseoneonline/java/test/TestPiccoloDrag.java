package com.baseoneonline.java.test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;

public class TestPiccoloDrag extends JFrame {

	public static void main(final String[] args) {
		final TestPiccoloDrag app = new TestPiccoloDrag();
		app.setSize(500, 400);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	public TestPiccoloDrag() {
		final PCanvas canvas = new PCanvas();
		// canvas.setPanEventHandler(null);

		final Rectangle2D hiliteRect = new Rectangle2D.Float(10, 10, 100, 100);
		final PPath highlighter = new PPath(hiliteRect);
		highlighter.setVisible(false);
		canvas.getLayer().addChild(highlighter);

		final PText text = new PText("Aargl");
		final PText text2 = new PText("Hello World!");

		final DragInputHandler dragHandler = new DragInputHandler();
		dragHandler.setHighlightBand(new RubberBand() {

			@Override
			public void setVisible(final boolean v) {
				highlighter.setVisible(v);
			}

			@Override
			public void setFrame(final double x, final double y,
					final double w, final double h) {
				hiliteRect.setFrame(x, y, w, h);
				highlighter.setPathTo(hiliteRect);
			}
		});

		canvas.getLayer().addInputEventListener(dragHandler);

		canvas.getLayer().addChild(text2);
		canvas.getLayer().addChild(text);

		add(canvas);

		final Point2D pt = canvas.getLayer().getBounds().getCenter2D();

		canvas.getCamera().setX(pt.getX());

	}

}

interface RubberBand {

	public void setFrame(double x, double y, double w, double h);

	public void setVisible(boolean v);
}

class DragInputHandler extends PBasicInputEventHandler {

	private RubberBand highlightBand;

	public DragInputHandler() {}

	public void setHighlightBand(final RubberBand highlightBand) {
		this.highlightBand = highlightBand;
	}

	@Override
	public void mouseDragged(final PInputEvent event) {
		final PDimension delta = event.getDelta();
		event.getPickedNode().translate(delta.getWidth(), delta.getHeight());
		event.setHandled(true);
		if (null != highlightBand) {
			final PBounds bds = event.getPickedNode().getFullBounds();
			highlightBand.setFrame(bds.x, bds.y, bds.width, bds.height);
		}
	}

	@Override
	public void mousePressed(final PInputEvent event) {

		event.setHandled(true);
	}

	@Override
	public void mouseReleased(final PInputEvent event) {
		event.setHandled(true);
	}

	@Override
	public void mouseEntered(final PInputEvent event) {
		if (null != highlightBand) {
			highlightBand.setVisible(true);
		}
		event.setHandled(true);
	}

	@Override
	public void mouseExited(final PInputEvent event) {
		if (null != highlightBand) {
			highlightBand.setVisible(false);
		}
		event.setHandled(true);
	}

	@Override
	public void mouseMoved(final PInputEvent event) {
		if (null != highlightBand) {
			final PBounds bds = event.getPickedNode().getFullBounds();
			highlightBand.setFrame(bds.x, bds.y, bds.width, bds.height);
		}
		event.setHandled(true);
	}
}
