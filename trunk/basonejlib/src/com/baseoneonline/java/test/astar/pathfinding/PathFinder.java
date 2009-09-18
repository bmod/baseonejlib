package com.baseoneonline.java.test.astar.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;

public class PathFinder {

	private IGraph graph;
	private final HashMap<INode, Node> nodeMap = new HashMap<INode, Node>();
	private ArrayList<Node> open;
	private ArrayList<Node> closed;

	public PathFinder() {

	}

	public ArrayList<INode> solve(INode start, INode goal, IGraph graph) {
		this.graph = graph;
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();

		Node goalNode = getNode(goal);
		open.add(getNode(start));

		int i = 0;

		while (open.size() > 0) {
			i++;
			Node x = lowest(open);

			if (x == goalNode) {
				return null;
				// return createPath(x);
			}

			open.remove(x);
			closed.add(x);

			for (Node y : getNeighbors(x)) {

				if (closed.contains(y))
					continue;

				float g = x.g + distance(x, y);

				boolean g_better = false;

				if (!open.contains(y)) {
					open.add(y);
					g_better = true;
				} else if (g < y.g) {
					g_better = true;
				}

				if (g_better) {
					y.cameFrom = x;
					y.g = g;
					y.h = distance(y, goalNode) + .1f;
					y.f = y.g + y.h;
				}

			}

		}
		return null;

	}

	public ArrayList<INode> getClosed() {
		ArrayList<INode> nodes = new ArrayList<INode>();
		for (Node n : closed) {
			nodes.add(n.node);
		}
		return nodes;
	}

	private Node getNode(INode in) {
		Node node = nodeMap.get(in);
		if (null == node) {
			node = new Node(in);
			nodeMap.put(in, node);
		}
		return node;
	}

	private float distance(Node a, Node b) {
		return graph.distance(a.node, b.node);
	}

	private Node[] getNeighbors(Node n) {
		ArrayList<INode> neighbors = graph.getNeighbors(n.node);
		Node[] re = new Node[neighbors.size()];
		for (int i = 0; i < re.length; i++) {
			re[i] = getNode(neighbors.get(i));
		}
		return re;
	}

	private Node lowest(ArrayList<Node> list) {
		float f = Float.MAX_VALUE;
		Node node = null;
		for (Node n : list) {
			if (n.f < f) {
				node = n;
				f = node.f;
			}
		}
		return node;
	}

	private ArrayList<INode> createPath(Node goal) {
		ArrayList<INode> re = new ArrayList<INode>();
		Node node = goal;
		while (node.cameFrom != null) {
			System.out.println(node);
			re.add(node.node);
			node = node.cameFrom;
		}
		return re;
	}

}

class Node {

	public float g = 0;
	public float h = 0;
	public float f = 0;
	public Node cameFrom;

	public final INode node;

	public Node(INode node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "Node " + node;
	}
}
