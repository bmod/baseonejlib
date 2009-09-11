package com.baseoneonline.java.test.testEditor;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;

import com.baseoneonline.java.swing.DockingFrame;
import com.baseoneonline.java.test.testEditor.gnodes.Block;
import com.baseoneonline.java.test.testEditor.gnodes.GNode;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

public class TestEditor extends DockingFrame {

	public static void main(String[] args) {
		new TestEditor();
	}

	@Override
	protected void initFrame() {
		setLayout(new BorderLayout());
		add(new JButton("East"), BorderLayout.EAST);
		EditPanel editPanel = new EditPanel();
		add(editPanel);

		addKeyListener(HotKeyManager.get());
		
		
		SceneModel model = new SceneModel();
		model.addNode(new Block(10,10,30,10));
		model.addNode(new Block(20,30,30,10));
		editPanel.setModel(model);
	}

}



abstract class Tool extends PBasicInputEventHandler {

}

class SelectionTool extends Tool {

	@Override
	public void mousePressed(PInputEvent event) {
		System.out.println(event.getPickedNode());
	}

	@Override
	public void mouseDragged(PInputEvent event) {

	}
}





interface SelectionListener {
	void selectionChanged(List<GNode> selection);
}

class HotKeyManager implements KeyListener {
	private static HotKeyManager instance;

	private boolean altPressed = false;

	public HotKeyManager() {}

	public boolean isAltDown() {
		return altPressed;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		altPressed = e.isAltDown();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		altPressed = e.isAltDown();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	public static HotKeyManager get() {
		if (null == instance)
			instance = new HotKeyManager();
		return instance;
	}

}
