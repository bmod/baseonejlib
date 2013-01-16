package com.baseoneonline.java.astar;

import java.util.ArrayList;

public interface Graph {

	public float cost(int start, int goal);

	public ArrayList<Integer> getNeighbors(int x);

	public int size();

}
