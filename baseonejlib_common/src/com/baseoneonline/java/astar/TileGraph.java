package com.baseoneonline.java.astar;

import java.util.ArrayList;

import com.baseoneonline.java.math.Vec2i;

public class TileGraph implements Graph {

	private TileNode[][] data;
	private TileNode[] nodes;
	private int w;
	private int h;

	private static float COST_DIAG = (float) Math.sqrt(2);

	public TileGraph(final int w, final int h) {
		init(w, h);
	}

	public void init(final int w, final int h) {

		this.w = w;
		this.h = h;
		data = new TileNode[w][h];
		nodes = new TileNode[w * h];
		int i = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				data[x][y] = new TileNode(x, y, i, 0);
				nodes[i] = data[x][y];
				i++;
			}
		}
	}

	public TileNode[] getNodes(final int[] a) {
		final TileNode[] nodes = new TileNode[a.length];
		for (int i = 0; i < a.length; i++) {
			nodes[i] = this.nodes[a[i]];
		}
		return nodes;
	}

	public Vec2i[] getPositions(final ArrayList<Integer> a) {
		final Vec2i[] positions = new Vec2i[a.size()];
		for (int i = 0; i < a.size(); i++) {
			final TileNode n = nodes[a.get(i)];
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

	public TileNode getNode(final int index) {
		return nodes[index];
	}

	@Override
	public float cost(final int a, final int b) {
		final TileNode n1 = nodes[a];
		final TileNode n2 = nodes[b];
		final float h_diag = Math.min(Math.abs(n1.x - n2.x),
				Math.abs(n1.y - n2.y));
		final float h_orth = Math.abs(n1.x - n2.x) + Math.abs(n1.y - n2.y);
		return COST_DIAG * h_diag + h_orth - 2 * h_diag;
	}

	public float getCost(final int x, final int y) {
		return data[x][y].cost;
	}

	@Override
	public ArrayList<Integer> getNeighbors(final int node) {
		final int x = nodes[node].x;
		final int y = nodes[node].y;
		final ArrayList<Integer> neighbors = new ArrayList<Integer>();
		TileNode n;
		if (x > 0) {
			n = data[x - 1][y];
			if (n.isWalkable())
				neighbors.add(n.index);
		}
		if (y > 0) {
			n = data[x][y - 1];
			if (n.isWalkable())
				neighbors.add(n.index);
		}
		if (x < w - 1) {
			n = data[x + 1][y];
			if (n.isWalkable())
				neighbors.add(n.index);
		}
		if (y < h - 1) {
			n = data[x][y + 1];
			if (n.isWalkable())
				neighbors.add(n.index);
		}

		// Diagonal, no cutting corners
		// NW
		if (x > 0 && y > 0) {
			n = data[x - 1][y - 1];
			if (n.isWalkable() && data[x - 1][y].isWalkable()
					&& data[x][y - 1].isWalkable()) {
				neighbors.add(n.index);
			}
		}
		// NE
		if (x < w - 1 && y > 0) {
			n = data[x + 1][y - 1];
			if (n.isWalkable() && data[x + 1][y].isWalkable()
					&& data[x][y - 1].isWalkable()) {
				neighbors.add(n.index);
			}
		}
		// SW
		if (x > 0 && y < h - 1) {
			n = data[x - 1][y + 1];
			if (n.isWalkable() && data[x - 1][y].isWalkable()
					&& data[x][y + 1].isWalkable()) {
				neighbors.add(n.index);
			}
		}
		// SE
		if (x < w - 1 && y < h - 1) {
			n = data[x + 1][y + 1];
			if (n.isWalkable() && data[x + 1][y].isWalkable()
					&& data[x][y + 1].isWalkable()) {
				neighbors.add(n.index);
			}
		}

		return neighbors;
	}

	public TileNode getNode(final int x, final int y) {
		return data[x][y];
	}

	@Override
	public int size() {
		return w * h;
	}

}
