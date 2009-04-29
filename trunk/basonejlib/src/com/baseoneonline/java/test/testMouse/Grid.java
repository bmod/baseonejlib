package com.baseoneonline.java.test.testMouse;

import java.util.ArrayList;

public class Grid<T> {

	private int w;
	private int h;

	private ArrayList<ArrayList<T>> data;

	public Grid(int w, int h) {
		init(w, h);
	}

	private void init(int w, int h) {
		this.w = w;
		this.h = h;
		data = new ArrayList<ArrayList<T>>(w);
		for (ArrayList<T> d : data) {
			d = new ArrayList<T>(h);
		}
	}
	
	public T put(T element, int x, int y) {
		return data.get(x).set(y, element);
	}
	
	public T remove(int x, int y) {
		return data.get(x).remove(y);
	}
	
	public T get(int x, int y) {
		return data.get(x).get(y);
	}

}
