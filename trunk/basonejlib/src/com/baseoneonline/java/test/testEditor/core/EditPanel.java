package com.baseoneonline.java.test.testEditor.core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.logging.Logger;

import com.baseoneonline.java.test.testEditor.gnodes.GNode;
import com.baseoneonline.java.test.testEditor.tools.SelectionTool;
import com.baseoneonline.java.test.testEditor.tools.Tool;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.event.PZoomEventHandler;

public class EditPanel extends PCanvas implements SceneModelListener {

	private static final String GNODE = "gnode";

	private final Logger log = Logger.getLogger(getClass().getName());

	private Tool currentTool;
	private final SelectionTool selectionTool;
	private final PNodeFactory factory = new PNodeFactory();

	private final HashMap<GNode, PNode> nodeMap = new HashMap<GNode, PNode>();

	private SceneModel model;
	private final PPanEventHandler panEventHandler;
	private final PZoomEventHandler zoomEventHandler;

	public EditPanel() {
		panEventHandler = getPanEventHandler();
		setPanEventHandler(null);
		zoomEventHandler = getZoomEventHandler();
		setZoomEventHandler(null);

		// Create selection handle / rubber band
		getCamera().setPickable(false);

		selectionTool = new SelectionTool(this);
		setTool(selectionTool);

		addKeyListener(new MyKeyAdapter());
	}

	private class MyKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(final KeyEvent e) {
			if (e.isAltDown()) {
				setPanEventHandler(panEventHandler);
				setZoomEventHandler(zoomEventHandler);
			}
		}

		@Override
		public void keyReleased(final KeyEvent e) {
			if (!e.isAltDown()) {
				setPanEventHandler(null);
				setZoomEventHandler(null);
			}
		}
	}

	public void setModel(final SceneModel m) {
		// Ignore setting twice or more
		if (m == model) return;

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

	private void setTool(final Tool t) {
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

	@Override
	public void nodeAdded(final GNode gn) {
		final PNode pn = factory.createNode(gn);
		nodeMap.put(gn, pn);
		pn.addAttribute(GNODE, gn);
		getLayer().addChild(pn);
		nodeTransformed(gn);
	}

	@Override
	public void nodeRemoved(final GNode gn) {
		final PNode pn = nodeMap.remove(gn);
		getLayer().removeChild(pn);
	}

	@Override
	public void nodeTransformed(final GNode gn) {
		final PNode pn = nodeMap.get(gn);
		pn.setTransform(gn.getTransform());
	}

	public GNode getGNode(final PNode n) {
		return (GNode) n.getAttribute(GNODE);
	}

}
