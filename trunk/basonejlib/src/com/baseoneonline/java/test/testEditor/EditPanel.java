package com.baseoneonline.java.test.testEditor;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.logging.Logger;

import com.baseoneonline.java.test.testEditor.gnodes.GNode;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class EditPanel extends PCanvas implements SceneModelListener {

	private final Logger log = Logger.getLogger(getClass().getName());

	private static final String GNODE = "gnode";

	PText text;

	private final PPath selectionHandle;

	private Tool currentTool;
	private final SelectionTool selectionTool;
	private final PNodeFactory factory = new PNodeFactory();

	private final HashMap<GNode, PNode> nodeMap = new HashMap<GNode, PNode>();

	private SceneModel model;

	public EditPanel() {
		setPanEventHandler(null);
		setZoomEventHandler(null);
		
		// Create selectionhandle/rubberband
		selectionHandle = new PPath(new Rectangle2D.Float(0, 0, 10, 10));
		selectionHandle.setStrokePaint(Color.BLUE);
		selectionHandle.setVisible(false);
		getLayer().addChild(selectionHandle);

		selectionTool = new SelectionTool();
		setTool(selectionTool);
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

		// Populate
		for (int i = 0; i < model.numNodes(); i++) {
			nodeAdded(model.getNode(i));
		}
	}

	private void setTool(Tool t) {
		if (null != currentTool) {
			getLayer().removeInputEventListener(currentTool);
		}
		currentTool = t;
		getLayer().addInputEventListener(currentTool);
	}

	/* (non-Javadoc)
	 * @see com.baseoneonline.java.test.testEditor.SceneModelListener#nodeAdded(com.baseoneonline.java.test.testEditor.gnodes.GNode)
	 */
	@Override
	public void nodeAdded(GNode gn) {
		PNode pn = factory.createNode(gn);
		nodeMap.put(gn, pn);
		pn.addAttribute(GNODE, gn);
		getLayer().addChild(pn);
		nodeTransformed(gn);
	}

	/* (non-Javadoc)
	 * @see com.baseoneonline.java.test.testEditor.SceneModelListener#nodeRemoved(com.baseoneonline.java.test.testEditor.gnodes.GNode)
	 */
	@Override
	public void nodeRemoved(GNode gn) {
		PNode pn = nodeMap.remove(gn);
		getLayer().removeChild(pn);
	}

	/* (non-Javadoc)
	 * @see com.baseoneonline.java.test.testEditor.SceneModelListener#nodeTransformed(com.baseoneonline.java.test.testEditor.gnodes.GNode)
	 */
	@Override
	public void nodeTransformed(GNode gn) {
		PNode pn = nodeMap.get(gn);
		pn.setTransform(gn.getTransform());
	}

}
