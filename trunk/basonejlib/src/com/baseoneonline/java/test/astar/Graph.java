package com.baseoneonline.java.test.astar;

public class Graph {

	private float[][] data;
	private int w;
	private int h;

	public Graph() {

	}

	public void init(final int w, final int h) {
		this.w = w;
		this.h = h;
		data = new float[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				data[x][y] = Math.random() < .1 ? -1 : 0;
			}
		}
	}

	public void set(final int x, final int y, final float v) {
		data[x][y] = v;
	}

	public float get(final int x, final int y) {
		return data[x][y];
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

}
