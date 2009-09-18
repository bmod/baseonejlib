package com.baseoneonline.java.test.testEditor.tools;

import java.awt.geom.Point2D;

import com.baseoneonline.java.test.testEditor.core.EditPanel;
import com.baseoneonline.java.test.testEditor.gnodes.GNode;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PBounds;

public class SelectionTool extends Tool {

	double cx;
	double cy;

	public SelectionTool(EditPanel editor) {
		super(editor);
	}

	@Override
	public void mousePressed(PInputEvent event) {
		PNode pn = event.getPickedNode();
		GNode gn = editor.getGNode(pn);
		cx = event.getPosition().getX();
		cy = event.getPosition().getY();
		if (null == gn) {
			editor.hideRubberBand();
		} else {
			editor.setRubberBand(pn.getBounds(), pn.getTransform());
		}
	}

	@Override
	public void mouseReleased(PInputEvent event) {
		PNode pn = event.getPickedNode();
		GNode gn = editor.getGNode(pn);
		if (null == gn) {
			editor.hideRubberBand();
		} else {

		}
	}

	@Override
	public void mouseDragged(PInputEvent event) {

		Point2D pt = event.getPosition();

		double x1 = Math.min(cx, pt.getX());
		double y1 = Math.min(cy, pt.getY());
		double x2 = Math.max(cx, pt.getX()) - x1;
		double y2 = Math.max(cy, pt.getY()) - y1;
		PBounds bds = new PBounds(x1, y1, x2, y2);
		editor.setRubberBand(bds, editor.getLayer().getTransform());
	}
}
