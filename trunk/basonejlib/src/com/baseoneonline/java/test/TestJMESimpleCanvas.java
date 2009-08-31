package com.baseoneonline.java.test;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import com.baseoneonline.java.jme.GLPanel;
import com.baseoneonline.java.jme.JMETreeModel;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;

public class TestJMESimpleCanvas extends JFrame {

	public static void main(final String[] args) {
		final TestJMESimpleCanvas app = new TestJMESimpleCanvas();
		app.setSize(800, 600);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	public TestJMESimpleCanvas() {

		final JTree tree = new JTree();
		
		add(new JButton("North"), BorderLayout.NORTH);
		add(new JButton("East"), BorderLayout.EAST);
		add(new JButton("South"), BorderLayout.SOUTH);
		add(new JScrollPane(tree), BorderLayout.WEST);

		final GLPanel canvas = new GLPanel() {
			@Override
			protected void init() {
				display.getRenderer().setBackgroundColor(ColorRGBA.gray);
				Box box = new Box("Blap", new Vector3f(), 1,1,1);
				getRootNode().attachChild(box);
				tree.setModel(new JMETreeModel(getRootNode()));
			}
			@Override
			protected void update(float t) {
				
			}
		};
		add(canvas);

	}
}
