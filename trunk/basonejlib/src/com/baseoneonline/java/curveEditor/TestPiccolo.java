package com.baseoneonline.java.curveEditor;

import javax.swing.JFrame;

import com.baseoneonline.java.curveEditor.core.Curve;
import com.baseoneonline.java.curveEditor.core.Point;
import com.baseoneonline.java.curveEditor.piccolo.LinearCurve;

import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolox.PFrame;

public class TestPiccolo extends PFrame {

	public static void main(final String[] args) {
		final TestPiccolo app = new TestPiccolo();
		app.setSize(500, 400);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -7649847581254484192L;

	@Override
	public void initialize() {
		// Remove the Default pan event handler and add a drag event handler
		// so that we can drag the nodes around individually.




		getCanvas().setPanEventHandler(null);
		getCanvas().addInputEventListener(new PDragEventHandler());

		final Curve cv = new Curve();
		cv.addPoint(new Point(10, 20));
		cv.addPoint(new Point(30, 100));
		cv.addPoint(new Point(100, 20));
		cv.addPoint(new Point(100, 100));

		final LinearCurve lc = new LinearCurve(cv);
		getCanvas().getLayer().addChild(lc);


	}

}


