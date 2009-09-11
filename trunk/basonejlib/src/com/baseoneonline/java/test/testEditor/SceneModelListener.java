package com.baseoneonline.java.test.testEditor;

import com.baseoneonline.java.test.testEditor.gnodes.GNode;

public interface SceneModelListener {

	void nodeAdded(GNode n);
	void nodeRemoved(GNode n);
	void nodeTransformed(GNode n);

}
