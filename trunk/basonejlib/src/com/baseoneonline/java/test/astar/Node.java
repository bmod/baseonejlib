package com.baseoneonline.java.test.astar;

public class Node {

	public int x;
	public int y;

	public float g = 0;
	public float h = 0;
	public float cost = 1;

	public Node(final int x, final int y, final float cost) {
		this.x = x;
		this.y = y;
		this.cost = cost;
	}
}
