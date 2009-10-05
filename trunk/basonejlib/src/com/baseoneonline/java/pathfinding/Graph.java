package com.baseoneonline.java.pathfinding;

import java.util.ArrayList;

public interface Graph {

	public float distance(int start, int goal);

	public ArrayList<Integer> getNeighbors(int x);

	public int size();

}
