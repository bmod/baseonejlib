package com.baseoneonline.java.test.astar.pathfinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class PathFinder {

	private IGraph graph;
	private final HashMap<INode, Node> nodeMap = new HashMap<INode, Node>();
	private ArrayList<Node> closedSet;
	PriorityQueue<Node> openSet;

	public PathFinder() {

	}

	public ArrayList<INode> solve(INode istart, INode igoal, IGraph graph) {
		if (istart == igoal)
			return new ArrayList<INode>();
		this.graph = graph;
		ArrayList<Node> closed = new ArrayList<Node>();
		ArrayList<Node> open = new ArrayList<Node>();

		Node start = getNode(istart);
		Node goal = getNode(igoal);

		Node node = start;
		node.h = 0;
		open.add(node);

		boolean solved = false;

		while (!solved) {
			if (open.size() < 1)
				break;
			node = lowestIn(open);
			open.remove(node);
			closed.add(node);

			if (node == goal) {
				solved = true;
				break;
			}

			for (Node n : getNeighbors(node)) {
				if (!open.contains(n) && !closed.contains(n)) {
					open.add(n);
					n.parent = node;
					n.h = distance(n, goal);
					n.g = node.g;
				} else {
					float f = n.g + node.g + n.h;
					if (f < n.getF()) {
						n.parent = node;
						n.g = node.g;
					}
				}
			}
		}

		ArrayList<INode> solution = new ArrayList<INode>();
		if (solved) {
			solution.add(node.parent.node);
			while (null != node.parent && node.parent != start) {
				node = node.parent;
				solution.add(node.node);
			}
		}

		closedSet = closed;

		return solution;
	}

	private final Comparator<Node> comparator = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			if (o1.getF() < o2.getF())
				return -1;
			else if (o1.getF() > o2.getF())
				return 1;
			return 0;
		}
	};

	public ArrayList<INode> getClosed() {
		ArrayList<INode> nodes = new ArrayList<INode>();
		for (Node n : closedSet) {
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

	private Node lowestIn(ArrayList<Node> list) {
		float f = Float.POSITIVE_INFINITY;
		Node node = null;
		for (Node n : list) {
			if (n.getF() < f) {
				node = n;
				f = node.getF();
			}
		}
		return node;
	}

	private ArrayList<INode> createPath(Node goal) {
		ArrayList<INode> re = new ArrayList<INode>();
		Node node = goal;
		int i = 0;
		int max = 10;
		while (node.parent != null) {
			re.add(node.node);
			node = node.parent;
			if (i++ > max)
				break;
		}
		return re;
	}

}

class Node {

	public float g = 0;
	public float h = 0;
	// public float f = 0;
	public Node parent;

	public final INode node;

	public Node(INode node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "Node " + node;
	}

	public float getF() {
		return g + h;
	}
}
