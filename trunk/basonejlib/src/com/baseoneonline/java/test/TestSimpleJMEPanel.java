package com.baseoneonline.java.test;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.baseoneonline.java.jme.NavInputHandler;
import com.baseoneonline.java.jme.SimpleJMEPanel;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.state.WireframeState;
import com.jme.system.DisplaySystem;

public class TestSimpleJMEPanel extends JFrame {

	public static void main(final String[] args) {
		final TestSimpleJMEPanel app = new TestSimpleJMEPanel();
		app.setSize(800, 600);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	public TestSimpleJMEPanel() {

		add(new JButton("North"), BorderLayout.NORTH);
		add(new JButton("East"), BorderLayout.EAST);
		add(new JButton("South"), BorderLayout.SOUTH);
		add(new JButton("West"), BorderLayout.WEST);

		final SimpleJMEPanel canvas = new SimpleJMEPanel() {

			Box box;
			NavInputHandler input;

			@Override
			protected void initJME() {
				box = new Box("box", new Vector3f(), 3, 3, 3);
				getRootNode().attachChild(box);
				WireframeState ws = DisplaySystem.getDisplaySystem()
						.getRenderer().createWireframeState();
				box.setRenderState(ws);
				box.updateRenderState();

				input = new NavInputHandler(DisplaySystem.getDisplaySystem()
						.getRenderer().getCamera());

			}

			@Override
			protected void updateJME(final float t) {
				input.update(t);
			}
		};
		add(canvas);

	}
}
