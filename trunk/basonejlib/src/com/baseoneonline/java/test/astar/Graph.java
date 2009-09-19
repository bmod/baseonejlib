package com.baseoneonline.java.test.astar;

import java.net.URL;
import java.util.ArrayList;

import com.baseoneonline.java.test.astar.pathfinding.IGraph;
import com.baseoneonline.java.test.astar.pathfinding.INode;
import com.baseoneonline.java.tools.FileUtils;

public class Graph implements IGraph {

	private GraphNode[][] data;
	private int w;
	private int h;

	public Graph() {

	}

	public void init(final int w, final int h) {
		this.w = w;
		this.h = h;
		data = new GraphNode[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				data[x][y] = new GraphNode(x, y, Math.random() < .3 ? -1 : 0);
			}
		}
	}

	public void set(final int x, final int y, final float v) {
		data[x][y].cost = v;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	@Override
	public float distance(INode a, INode b) {
		GraphNode na = (GraphNode) a;
		GraphNode nb = (GraphNode) b;
		float dx = nb.x - na.x;
		float dy = nb.y - na.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	@Override
	public float getCost(int x, int y) {
		return data[x][y].cost;
	}

	@Override
	public ArrayList<INode> getNeighbors(INode node) {
		GraphNode n = (GraphNode) node;
		ArrayList<INode> nodes = new ArrayList<INode>();
		GraphNode nb;
		if (n.x > 0) {
			nb = data[n.x - 1][n.y];
			if (nb.cost != -1)
				nodes.add(nb);
		}
		if (n.y > 0) {
			nb = data[n.x][n.y - 1];
			if (nb.cost != -1)
				nodes.add(nb);
		}
		if (n.x < w - 1) {
			nb = data[n.x + 1][n.y];
			if (nb.cost != -1)
				nodes.add(nb);
		}
		if (n.y < h - 1) {
			nb = data[n.x][n.y + 1];
			if (nb.cost != -1)
				nodes.add(nb);
		}
		return nodes;
	}

	public GraphNode getNode(int x, int y) {
		return data[x][y];
	}

	@Override
	public int size() {
		return w * h;
	}

	public void init(URL url) {
		String str = FileUtils.readFile(url);
		char block = "#".charAt(0);
		char open = "0".charAt(0);
		w = -1;
		h = -1;
		if (null != data) {
			String[] rows = str.split("\n");
			h = rows.length;
			for (int y = 0; y < h; y++) {

				if (-1 == w) {
					w = rows[y].length() - 1;
					data = new GraphNode[w][h];
				}
				for (int x = 0; x < w; x++) {
					char d = rows[y].charAt(x);
					if (d == block) {
						data[x][y] = new GraphNode(x, y, -1);
					} else {
						data[x][y] = new GraphNode(x, y, 0);
					}
				}
			}
		}
	}
}
