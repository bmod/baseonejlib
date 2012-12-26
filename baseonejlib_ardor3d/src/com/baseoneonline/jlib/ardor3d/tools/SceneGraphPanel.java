package com.baseoneonline.jlib.ardor3d.tools;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import com.ardor3d.scenegraph.Node;

public class SceneGraphPanel extends JPanel {

	private final SceneGraphModel model = new SceneGraphModel();
	private JTree tree;

	public SceneGraphPanel() {
		initComponents();
		tree.setModel(model);
		tree.setRootVisible(true);

	}

	public void setRootNode(final Node root) {
		model.setRootNode(root);
	}

	private void initComponents() {
		setLayout(new BorderLayout(0, 0));

		final JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);

		tree = new JTree();
		scrollPane.setViewportView(tree);
	}

}
