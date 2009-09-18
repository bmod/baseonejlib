package com.baseoneonline.java.test.testEditor.core;

import com.baseoneonline.java.test.testEditor.gnodes.GNode;

public interface SceneModelListener {

	void nodeAdded(GNode n);
	void nodeRemoved(GNode n);
	void nodeTransformed(GNode n);

}
