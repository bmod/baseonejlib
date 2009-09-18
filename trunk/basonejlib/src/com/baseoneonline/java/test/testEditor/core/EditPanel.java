package com.baseoneonline.java.test.testEditor.core;

import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.logging.Logger;

import com.baseoneonline.java.test.testEditor.gnodes.GNode;
import com.baseoneonline.java.test.testEditor.tools.SelectionTool;
import com.baseoneonline.java.test.testEditor.tools.Tool;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;

public class EditPanel extends PCanvas implements SceneModelListener {

	private static final String GNODE = "gnode";

	private final Logger log = Logger.getLogger(getClass().getName());

	private final RubberBand rubberBand;

	private Tool currentTool;
	private final SelectionTool selectionTool;
	private final PNodeFactory factory = new PNodeFactory();

	private final HashMap<GNode, PNode> nodeMap = new HashMap<GNode, PNode>();

	private SceneModel model;

	public EditPanel() {
		setPanEventHandler(null);
		setZoomEventHandler(null);

		// Create selection handle / rubber band
		rubberBand = new RubberBand();
		rubberBand.setVisible(false);
		getLayer().addChild(rubberBand);

		selectionTool = new SelectionTool(this);
		setTool(selectionTool);

	}

	public void setRubberBand(PBounds bounds, AffineTransform xf) {
		rubberBand.setBounds(bounds);
		rubberBand.setTransform(xf);
		rubberBand.setVisible(true);
	}

	public void hideRubberBand() {
		rubberBand.setVisible(false);
	}

	public void setModel(SceneModel m) {
		// Ignore setting twice or more
		if (m == model) {
			log.warning("Trying to set same model twice or more, skipping...");
			return;
		}

		// Swap listeners
		if (null != model) {
			model.removeModelListener(this);
		}
		this.model = m;
		model.addModelListener(this);

		// Populate form model. After this, only update from events.
		for (int i = 0; i < model.numNodes(); i++) {
			nodeAdded(model.getNode(i));
		}
	}

	private void setTool(Tool t) {
		if (t == currentTool) {
			log.warning("Setting the same tool twice or more: skipping...");
			return;
		}
		if (null != currentTool) {
			removeInputEventListener(currentTool);
		}
		currentTool = t;

		addInputEventListener(currentTool);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.baseoneonline.java.test.testEditor.SceneModelListener#nodeAdded(com
	 * .baseoneonline.java.test.testEditor.gnodes.GNode)
	 */
	@Override
	public void nodeAdded(GNode gn) {
		PNode pn = factory.createNode(gn);
		nodeMap.put(gn, pn);
		pn.addAttribute(GNODE, gn);
		getLayer().addChild(pn);
		nodeTransformed(gn);
	}

	@Override
	public void nodeRemoved(GNode gn) {
		PNode pn = nodeMap.remove(gn);
		getLayer().removeChild(pn);
	}

	@Override
	public void nodeTransformed(GNode gn) {
		PNode pn = nodeMap.get(gn);
		pn.setTransform(gn.getTransform());
	}

	public GNode getGNode(PNode n) {
		return (GNode) n.getAttribute(GNODE);
	}

}
