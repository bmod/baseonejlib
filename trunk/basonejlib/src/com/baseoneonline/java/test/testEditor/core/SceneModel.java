package com.baseoneonline.java.test.testEditor.core;

import java.util.ArrayList;

import com.baseoneonline.java.test.testEditor.gnodes.GNode;

public class SceneModel {
	
	private final ArrayList<SceneModelListener> listeners = new ArrayList<SceneModelListener>();
	
	private final ArrayList<GNode> nodes = new ArrayList<GNode>();
	
	public SceneModel() {

	}
	
	public GNode getNode(int i) {
		return nodes.get(i);
	}
	
	public int numNodes() {
		return nodes.size();
	}
	
	public void addNode(GNode node) {
		nodes.add(node);
		fireNodeAdded(node);
	}
	
	public void removeNode(GNode node) {
		nodes.remove(node);
		fireNodeRemoved(node);
	}
	
	public void addModelListener(SceneModelListener l) {
		if (!listeners.contains(l)) listeners.add(l);
	}
	
	public void removeModelListener(SceneModelListener l) {
		listeners.remove(l);
	}
	
	void fireNodeAdded(GNode node) {
		for (SceneModelListener l : listeners) {
			l.nodeAdded(node);
		}
	}
	
	private void fireNodeRemoved(GNode node) {
		for (SceneModelListener l : listeners) {
			l.nodeRemoved(node);
		}
	}
	
}
