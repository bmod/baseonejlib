package com.baseoneonline.java.test.testEditor.core;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.nodes.PPath;

public class RubberBand extends PPath {
	public RubberBand() {
		super(new Rectangle2D.Float(0, 0, 10, 10));
		setStrokePaint(Color.BLUE);

	}
}
