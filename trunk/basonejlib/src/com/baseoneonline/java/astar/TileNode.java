package com.baseoneonline.java.astar;

public class TileNode {
	public int x;
	public int y;
	public int index;
	public float cost;

	public TileNode(int x, int y, int index, float cost) {
		this.x = x;
		this.y = y;
		this.index = index;
		this.cost = cost;
	}

	public boolean walkable() {
		return cost != -1;
	}

	@Override
	public String toString() {
		return "Node x:" + x + "\ty:" + y;
	}

}
