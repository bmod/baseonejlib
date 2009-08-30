package com.baseoneonline.java.test;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.baseoneonline.java.jme.SimpleJMEPanel;

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

			@Override
			protected void initJME() {

			}
		};
		add(canvas);

	}
}
