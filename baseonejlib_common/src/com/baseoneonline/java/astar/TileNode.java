package com.baseoneonline.java.astar;

public class TileNode {

	public int x;
	public int y;
	public int index;
	public float cost;

	public TileNode(final int x, final int y, final int index, final float cost) {
		this.x = x;
		this.y = y;
		this.index = index;
		this.cost = cost;
	}

	public boolean isWalkable() {
		return cost != -1;
	}

	public void setWalkable(final boolean b) {
		cost = b ? 0 : -1;
	}

	@Override
	public String toString() {
		return "Node x:" + x + "\ty:" + y;
	}

}
