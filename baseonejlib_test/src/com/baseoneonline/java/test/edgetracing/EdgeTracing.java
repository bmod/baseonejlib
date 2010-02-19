package com.baseoneonline.java.test.edgetracing;

import javax.swing.JFrame;

public class EdgeTracing {

	public static void main(final String[] args) {
		new EdgeTracing();
	}

	NewJFrame gui;

	public EdgeTracing() {
		gui = new NewJFrame();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(600, 400);
		gui.setVisible(true);
	}

}
