package com.baseoneonline.java.astar;

import java.util.ArrayList;

public class AStar {

	private Graph userGraph;

	private float[] g;
	private float[] h;
	private float[] f;
	private int[] parent;

	private final ArrayList<Integer> open = new ArrayList<Integer>();
	private final ArrayList<Integer> closed = new ArrayList<Integer>();

	public AStar(Graph graph) {
		setGraph(graph);
	}

	public void setGraph(Graph graph) {
		this.userGraph = graph;
		updateGraph();
	}

	private void updateGraph() {
		int len = userGraph.size();
		g = new float[len];
		h = new float[len];
		f = new float[len];
		parent = new int[len];
	}

	public ArrayList<Integer> solve(int start, int goal) {
		open.clear();
		closed.clear();

		open.add(start);

		g[start] = 0;
		h[start] = userGraph.distance(start, goal);
		f[start] = h[start];
		parent[start] = -1;

		while (open.size() > 0) {

			// Find node with lowest f
			float tf = Float.POSITIVE_INFINITY;
			int x = -1;
			for (int i = 0; i < open.size(); i++) {
				int e = open.get(i);
				if (f[e] < tf) {
					tf = f[e];
					x = e;
				}
			}

			if (x == goal)
				return createPath(goal);

			open.remove((Integer) x);
			closed.add(x);

			for (int y : userGraph.getNeighbors(x)) {
				if (closed.contains(y))
					continue;

				float tg = g[x] + userGraph.distance(x, y);
				boolean better = false;

				if (!open.contains(y)) {
					open.add(y);
					better = true;
				} else if (tg < g[y]) {
					better = true;
				}
				if (better) {
					parent[y] = x;
					g[y] = tg;
					h[y] = userGraph.distance(y, goal);
					f[y] = g[y] + h[y];
				}
			}

		}
		// No solution found, return shortest alternative
		float min = Float.POSITIVE_INFINITY;
		int ng = -1;
		for (int n : closed) {
			float d = userGraph.distance(goal, n);
			if (d < min) {
				min = d;
				ng = n;
			}
		}
		return createPath(ng);
	}

	private ArrayList<Integer> createPath(int n) {
		ArrayList<Integer> solution = new ArrayList<Integer>();
		while (parent[n] != -1) {
			solution.add(n);
			n = parent[n];
		}
		return solution;
	}

}
