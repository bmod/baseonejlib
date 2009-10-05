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

import com.baseoneonline.java.pathfinding.AStar;
import com.baseoneonline.java.pathfinding.TileGraph;
import com.baseoneonline.java.pathfinding.TileNode;
import com.baseoneonline.java.swing.BaseFrame;
import com.baseoneonline.java.tools.FileUtils;

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

	private TileGraph graph;
	private AStar finder;

	@Override
	protected void initFrame() {

		graph = new TileGraph();
		graph.init(50, 40);
		finder = new AStar(graph);
		initComponents();
	}

	private void initComponents() {
		final GraphView view = new GraphView(graph);
		add(view);
		final MapModel mdl = new MapModel();
		final JList list = new JList(mdl);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				loadMap(mdl.getURL(list.getSelectedIndex()));
				view.repaint();
			}
		});
		add(new JScrollPane(list), BorderLayout.EAST);

		view.addPropertyChangeListener("mapChange",
			new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					solve(graph, view, finder);
				}
			});
	}

	public void loadMap(URL url) {
		String str = FileUtils.readFile(url);
		char block = "#".charAt(0);
		int i = 0;
		int w = -1;
		int h = -1;
		String[] rows = str.split("\n");
		h = rows.length;
		for (int y = 0; y < h; y++) {

			if (-1 == w) {
				w = rows[y].length() - 1;
				graph.init(w, h);
			}
			for (int x = 0; x < w; x++) {
				char d = rows[y].charAt(x);
				if (d == block) {
					graph.setCost(x, y, -1);
				} else {
					graph.setCost(x, y, 0);
				}
				i++;
			}
		}
	}

	private void solve(TileGraph graph, GraphView view, AStar finder) {
		TileNode strt = graph.getNode(view.getStart().x, view.getStart().y);
		TileNode goal = graph.getNode(view.getGoal().x, view.getGoal().y);

		// Call solver
		ArrayList<Integer> solution = finder.solve(strt.index, goal.index);
		view.markPath(graph.getPositions(solution));

		view.repaint();
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
