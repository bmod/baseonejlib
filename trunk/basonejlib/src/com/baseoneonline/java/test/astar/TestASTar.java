package com.baseoneonline.java.test.astar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import com.baseoneonline.java.swing.BaseFrame;
import com.baseoneonline.java.test.astar.pathfinding.INode;
import com.baseoneonline.java.test.astar.pathfinding.PathFinder;

public class TestASTar extends BaseFrame {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		new TestASTar();
	}

	@Override
	protected void frameClosing() {
		System.exit(0);
	}

	@Override
	protected void initFrame() {

		final Graph graph = new Graph();
		graph.init(30, 20);
		final GraphView view = new GraphView(graph);
		add(view);

		final PathFinder finder = new PathFinder();

		view.addPropertyChangeListener("mapChange",
			new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					GraphNode strt =
						graph.getNode(view.getStart().x, view.getStart().y);
					GraphNode goal =
						graph.getNode(view.getGoal().x, view.getGoal().y);

					// Call solver
					ArrayList<INode> path = finder.solve(strt, goal, graph);
					path = finder.getClosed();
					Vec2i[] pts = inodesToVec2i(path);
					view.markNodes(pts);
				}
			});
	}

	private Vec2i[] inodesToVec2i(ArrayList<INode> inodes) {
		Vec2i[] pts = new Vec2i[inodes.size()];
		for (int i = 0; i < pts.length; i++) {
			GraphNode n = (GraphNode) inodes.get(i);
			pts[i] = new Vec2i(n.x, n.y);
		}
		return pts;
	}

}
