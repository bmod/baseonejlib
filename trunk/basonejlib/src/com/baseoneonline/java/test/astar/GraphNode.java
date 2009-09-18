package com.baseoneonline.java.test.astar;

import com.baseoneonline.java.test.astar.pathfinding.INode;

public class GraphNode implements INode {
	public int x;
	public int y;

	public float cost;

	public GraphNode(int x, int y, float cost) {
		this.x = x;
		this.y = y;
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Node x:" + x + "\ty:" + y;
	}

}
