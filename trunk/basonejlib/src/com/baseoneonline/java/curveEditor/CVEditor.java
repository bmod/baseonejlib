package com.baseoneonline.java.curveEditor;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.baseoneonline.java.curveEditor.core.Curve;
import com.baseoneonline.java.curveEditor.core.Point;

public class CVEditor extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 6908806890369496949L;
	Logger log = Logger.getLogger(getClass().getName());

	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			final CVEditor app = new CVEditor();
			app.setDefaultCloseOperation(EXIT_ON_CLOSE);
			app.setTitle("CVEditor");
			app.setSize(400, 300);
			app.setVisible(true);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public CVEditor() {
		createUI();

		final Curve cv = new Curve();
		cv.addPoint(new Point((float) Math.random(), (float) Math.random()));
		cv.addPoint(new Point((float) Math.random(), (float) Math.random()));
		cv.addPoint(new Point((float) Math.random(), (float) Math.random()));
		cv.addPoint(new Point((float) Math.random(), (float) Math.random()));
		cv.sortPoints();
		cvPanel.addCurve(cv);
	}

	CVPanel cvPanel;

	private void createUI() {
		cvPanel = new CVPanel();
		add(cvPanel);
	}

}
