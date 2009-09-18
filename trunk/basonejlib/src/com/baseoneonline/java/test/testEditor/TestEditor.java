package com.baseoneonline.java.test.testEditor;

import java.awt.BorderLayout;

import javax.swing.JButton;

import com.baseoneonline.java.swing.BaseFrame;
import com.baseoneonline.java.test.testEditor.core.EditPanel;
import com.baseoneonline.java.test.testEditor.core.SceneModel;
import com.baseoneonline.java.test.testEditor.gnodes.Block;

public class TestEditor extends BaseFrame {

	public static void main(String[] args) {
		new TestEditor();
	}

	public TestEditor() {
		initFrame();
		restoreFrame();
	}

	@Override
	protected void initFrame() {
		add(new JButton("East"), BorderLayout.EAST);

		EditPanel editPanel = new EditPanel();
		add(editPanel);

		SceneModel model = new SceneModel();
		model.addNode(new Block(10, 10, 30, 10));
		model.addNode(new Block(20, 30, 30, 10));
		editPanel.setModel(model);
	}

	@Override
	protected void frameClosing() {

	}

}
