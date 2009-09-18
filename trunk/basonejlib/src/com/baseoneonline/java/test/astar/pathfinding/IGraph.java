package com.baseoneonline.java.test.astar.pathfinding;

import java.util.ArrayList;

public interface IGraph {

	public float getCost(int x, int y);

	public float distance(INode a, INode b);

	public ArrayList<INode> getNeighbors(INode node);

}
