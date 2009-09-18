package com.baseoneonline.java.test.astar;

import java.util.ArrayList;

import com.baseoneonline.java.test.astar.pathfinding.IGraph;
import com.baseoneonline.java.test.astar.pathfinding.INode;

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
				data[x][y] = new GraphNode(x, y, Math.random() < .1 ? -1 : 0);
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
		if (n.x > 0)
			nodes.add(data[n.x - 1][n.y]);
		if (n.y > 0)
			nodes.add(data[n.x][n.y - 1]);
		if (n.x < w - 1)
			nodes.add(data[n.x + 1][n.y]);
		if (n.y < h - 1)
			nodes.add(data[n.x][n.y + 1]);
		return nodes;
	}

	public GraphNode getNode(int x, int y) {
		return data[x][y];
	}
}
