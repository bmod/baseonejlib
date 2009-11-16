package com.baseoneonline.java.test.testEditor.tools;

import java.awt.geom.Point2D;

import com.baseoneonline.java.test.testEditor.core.EditPanel;
import com.baseoneonline.java.test.testEditor.gnodes.GNode;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;

public class SelectionTool extends Tool {

	private Point2D clickPoint;

	public SelectionTool(final EditPanel editor) {
		super(editor);
	}

	@Override
	public void mousePressed(final PInputEvent event) {
		final PNode pn = event.getPickedNode();
		final GNode gn = editor.getGNode(pn);
		clickPoint = event.getPosition();

	}

	@Override
	public void mouseReleased(final PInputEvent event) {
		final PNode pn = event.getPickedNode();
		if (null == pn) {
			// No Node clicked
		} else {
			final GNode gn = editor.getGNode(pn);
			System.out.println(gn);
		}
	}

	@Override
	public void mouseDragged(final PInputEvent event) {

		final Point2D pt = event.getPosition();

	}

	@Override
	public void cleanup() {
	// TODO Auto-generated method stub

	}
}
