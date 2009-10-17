package com.baseoneonline.java.media.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.baseoneonline.java.media.NodeTreeModel;
import com.baseoneonline.java.media.nodes.INode;

public class MediaTreePanel extends JPanel {

	List<TreePanelListener> listeners = new ArrayList<TreePanelListener>();

	public MediaTreePanel(final INode rootNode) {
		setLayout(new BorderLayout());
		final JTree tree = new JTree(new NodeTreeModel(rootNode));
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				final TreePath path = tree.getClosestPathForLocation(e.getX(),
						e.getY());
				if (null != path) {
					if (e.getClickCount() == 1) {
						fireNodeClicked((INode) path.getLastPathComponent());
					} else if (e.getClickCount() == 2) {
						fireNodeDoubleClicked((INode) path
								.getLastPathComponent());
					}
				}
			}
		});
		add(new JScrollPane(tree));
	}

	private void fireNodeClicked(final INode node) {
		for (final TreePanelListener l : listeners) {
			l.nodeClicked(node);
		}
	}

	private void fireNodeDoubleClicked(final INode node) {
		for (final TreePanelListener l : listeners) {
			l.nodeDoubleClicked(node);
		}
	}

	public void addListener(final TreePanelListener l) {
		listeners.add(l);
	}

	public static interface TreePanelListener {

		public void nodeClicked(INode node);

		public void nodeDoubleClicked(INode node);

	}

}
