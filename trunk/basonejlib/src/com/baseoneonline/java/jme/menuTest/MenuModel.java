package com.baseoneonline.java.jme.menuTest;

public interface MenuModel<T> {
	public T get(int index);
	public void add(T element);
	public int size();
	public int getSelectedIndex();
	public void setSelectedIndex(int i);
	public String getLabel(T element);
}
