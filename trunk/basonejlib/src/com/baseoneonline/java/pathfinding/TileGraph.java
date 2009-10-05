package com.baseoneonline.java.pathfinding;

import java.util.ArrayList;

import com.baseoneonline.java.math.Vec2i;

public class TileGraph implements Graph {

	private TileNode[][] data;
	private TileNode[] nodes;
	private int w;
	private int h;

	private static float COST_DIAG = (float) Math.sqrt(2);

	public TileGraph() {

	}

	public void init(final int w, final int h) {

		this.w = w;
		this.h = h;
		this.data = new TileNode[w][h];
		nodes = new TileNode[w * h];
		int i = 0;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				this.data[x][y] =
					new TileNode(x, y, i, Math.random() < .3 ? -1 : 0);
				nodes[i] = this.data[x][y];
				i++;
			}
		}
	}

	public TileNode[] getNodes(int[] a) {
		TileNode[] nodes = new TileNode[a.length];
		for (int i = 0; i < a.length; i++) {
			nodes[i] = nodes[a[i]];
		}
		return nodes;
	}

	public Vec2i[] getPositions(ArrayList<Integer> a) {
		Vec2i[] positions = new Vec2i[a.size()];
		for (int i = 0; i < a.size(); i++) {
			TileNode n = nodes[a.get(i)];
			positions[i] = new Vec2i(n.x, n.y);
		}
		return positions;
	}

	public void setCost(final int x, final int y, final float v) {
		data[x][y].cost = v;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	@Override
	public float distance(int a, int b) {
		TileNode n1 = nodes[a];
		TileNode n2 = nodes[b];
		float h_diag = Math.min(Math.abs(n1.x - n2.x), Math.abs(n1.y - n2.y));
		float h_orth = (Math.abs(n1.x - n2.x) + Math.abs(n1.y - n2.y));
		return COST_DIAG * h_diag + h_orth - 2 * h_diag;
	}

	public float getCost(int x, int y) {
		return data[x][y].cost;
	}

	@Override
	public ArrayList<Integer> getNeighbors(int node) {
		int x = nodes[node].x;
		int y = nodes[node].y;
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		TileNode n;
		if (x > 0) {
			n = data[x - 1][y];
			if (n.walkable())
				nodes.add(n.index);
		}
		if (y > 0) {
			n = data[x][y - 1];
			if (n.walkable())
				nodes.add(n.index);
		}
		if (x < w - 1) {
			n = data[x + 1][y];
			if (n.walkable())
				nodes.add(n.index);
		}
		if (y < h - 1) {
			n = data[x][y + 1];
			if (n.walkable())
				nodes.add(n.index);
		}

		// Diagonal, no cutting corners
		// NW
		if (x > 0 && y > 0) {
			n = data[x - 1][y - 1];
			if (n.walkable() && data[x - 1][y].walkable()
				&& data[x][y - 1].walkable()) {
				nodes.add(n.index);
			}
		}
		// NE
		if (x < w - 1 && y > 0) {
			n = data[x + 1][y - 1];
			if (n.walkable() && data[x + 1][y].walkable()
				&& data[x][y - 1].walkable()) {
				nodes.add(n.index);
			}
		}
		// SW
		if (x > 0 && y < h - 1) {
			n = data[x - 1][y + 1];
			if (n.walkable() && data[x - 1][y].walkable()
				&& data[x][y + 1].walkable()) {
				nodes.add(n.index);
			}
		}
		// SE
		if (x < w - 1 && y < h - 1) {
			n = data[x + 1][y + 1];
			if (n.walkable() && data[x + 1][y].walkable()
				&& data[x][y + 1].walkable()) {
				nodes.add(n.index);
			}
		}

		return nodes;
	}

	public TileNode getNode(int x, int y) {
		return data[x][y];
	}

	@Override
	public int size() {
		return w * h;
	}

}
