package com.baseoneonline.java.test.astar;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

		final MapModel mdl = new MapModel();
		final JList list = new JList(mdl);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				graph.init(mdl.getURL(list.getSelectedIndex()));
				view.repaint();
			}
		});
		add(new JScrollPane(list), BorderLayout.EAST);

		final PathFinder finder = new PathFinder();

		view.addPropertyChangeListener("mapChange",
			new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					solve(graph, view, finder);
				}
			});
	}

	private void solve(Graph graph, GraphView view, PathFinder finder) {
		GraphNode strt = graph.getNode(view.getStart().x, view.getStart().y);
		GraphNode goal = graph.getNode(view.getGoal().x, view.getGoal().y);

		// Call solver
		ArrayList<INode> solution = finder.solve(strt, goal, graph);
		ArrayList<INode> visitedNodes = finder.getClosed();
		view.markPath(inodesToVec2i(solution));
		view.markVisited(inodesToVec2i(visitedNodes));

		view.repaint(0);
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

class MapModel extends AbstractListModel {

	private final String[] maps = { "map1", "map2" };

	public MapModel() {}

	public URL getURL(int i) {
		return getClass().getResource("maps/" + maps[i] + ".txt");
	}

	@Override
	public Object getElementAt(int arg0) {
		return maps[arg0];
	}

	@Override
	public int getSize() {
		return maps.length;
	}
}
