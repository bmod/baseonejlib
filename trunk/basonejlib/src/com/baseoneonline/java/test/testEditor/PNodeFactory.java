package com.baseoneonline.java.test.testEditor;

import java.awt.geom.Rectangle2D;

import com.baseoneonline.java.test.testEditor.gnodes.Block;
import com.baseoneonline.java.test.testEditor.gnodes.GNode;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class PNodeFactory {
	public PNodeFactory() {}

	public PNode createNode(GNode gn) {
		if (gn instanceof Block) {
			return createBlock((Block) gn);
		}
		throw new NullPointerException("Node type not defined: "
			+ gn.getClass().getName());

	}

	private PNode createBlock(Block n) {
		PPath path =
			new PPath(new Rectangle2D.Float(0, 0, n.width, n.height));
		return path;
	}

}
